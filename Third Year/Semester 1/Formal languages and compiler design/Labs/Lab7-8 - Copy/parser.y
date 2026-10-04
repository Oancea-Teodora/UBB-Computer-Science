%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>

void yyerror(const char *s);
int yylex(void);

/* -------- code generation helpers -------- */

/* Accumulated generated C code */
char* generated_code = NULL;

/* Declarations for variables (built from let/assign) */
static char* decls_buf = NULL;
static size_t decls_len = 0;

/* symbol table for declared identifiers */
static char* declared_ids[256];
static int declared_count = 0;

static int is_declared(const char* id){
    for(int i=0;i<declared_count;i++){
        if(strcmp(declared_ids[i], id)==0) return 1;
    }
    return 0;
}

static void ensure_decl(const char* id){
    if(is_declared(id)) return;
    if(declared_count < 256){
        declared_ids[declared_count++] = strdup(id);
    }
    const char* fmt = "double %s = 0;\n";
    size_t need = snprintf(NULL,0,fmt,id);
    decls_buf = realloc(decls_buf, decls_len + need + 1);
    snprintf(decls_buf + decls_len, need + 1, fmt, id);
    decls_len += need;
}

static char* strf(const char* fmt, ...){
    va_list ap;
    va_start(ap, fmt);
    int n = vsnprintf(NULL, 0, fmt, ap);
    va_end(ap);
    char* buf = (char*)malloc((size_t)n + 1);
    va_start(ap, fmt);
    vsnprintf(buf, (size_t)n + 1, fmt, ap);
    va_end(ap);
    return buf;
}

/* -------- Bison declarations -------- */
%}

%union {
    char* str;
}

/* tokens from the lexer */
%token LET PRINT IF THEN ELSE END AND OR
%token SIN COS SQRT POW
%token <str> ID NUM
%token EQ NEQ LE GE

/* precedence and associativity */
%left OR
%left AND
%nonassoc EQ NEQ '<' LE '>' GE
%left '+' '-'
%left '*' '/'
%right UMINUS

%type <str> program stmt_list stmt let_stmt assign_stmt print_stmt if_stmt else_part expr primary call arg_list_opt arg_list

%%

program
    : stmt_list
    {
        /* build final C translation */
        const char* prologue = "#include <stdio.h>\\n#include <math.h>\\n\\nint main(void) {\\n";
        const char* epilogue = "    return 0;\\n}\\n";
        const char* decls = decls_buf ? decls_buf : "";
        generated_code = strf("%s%s%s%s", prologue, decls, $1, epilogue);
        $$ = generated_code;
    }
    ;

stmt_list
    : /* empty */             { $$ = strdup(""); }
    | stmt_list stmt          { $$ = strf("%s%s", $1, $2); }
    ;

stmt
    : let_stmt
    | assign_stmt
    | print_stmt
    | if_stmt
    ;

let_stmt
    : LET ID '=' expr ';'
      {
        ensure_decl($2);
        $$ = strf("    %s = %s;\\n", $2, $4);
      }
    ;

assign_stmt
    : ID '=' expr ';'
      {
        ensure_decl($1);
        $$ = strf("    %s = %s;\\n", $1, $3);
      }
    ;

print_stmt
    : PRINT expr ';'
      {
        $$ = strf("    printf(\"%%g\\\\n\", %s);\\n", $2);
      }
    ;

if_stmt
    : IF expr THEN stmt_list else_part END
      {
        $$ = strf("    if (%s) {\\n%s    }%s", $2, $4, $5);
      }
    ;

else_part
    : /* empty */             { $$ = strdup("\\n"); }
    | ELSE stmt_list          { $$ = strf(" else {\\n%s    }\\n", $2); }
    ;

expr
    : expr OR expr            { $$ = strf("(%s || %s)", $1, $3); }
    | expr AND expr           { $$ = strf("(%s && %s)", $1, $3); }
    | expr EQ expr            { $$ = strf("(%s == %s)", $1, $3); }
    | expr NEQ expr           { $$ = strf("(%s != %s)", $1, $3); }
    | expr '<' expr           { $$ = strf("(%s < %s)", $1, $3); }
    | expr LE expr            { $$ = strf("(%s <= %s)", $1, $3); }
    | expr '>' expr           { $$ = strf("(%s > %s)", $1, $3); }
    | expr GE expr            { $$ = strf("(%s >= %s)", $1, $3); }
    | expr '+' expr           { $$ = strf("(%s + %s)", $1, $3); }
    | expr '-' expr           { $$ = strf("(%s - %s)", $1, $3); }
    | expr '*' expr           { $$ = strf("(%s * %s)", $1, $3); }
    | expr '/' expr           { $$ = strf("(%s / %s)", $1, $3); }
    | '-' expr %prec UMINUS   { $$ = strf("(-%s)", $2); }
    | primary                 { $$ = $1; }
    ;

primary
    : NUM                     { $$ = strdup($1); }
    | ID                      { $$ = strdup($1); }
    | '(' expr ')'            { $$ = strf("(%s)", $2); }
    | call                    { $$ = $1; }
    ;

call
    : SIN  '(' arg_list_opt ')' { $$ = strf("sin(%s)",  $3); }
    | COS  '(' arg_list_opt ')' { $$ = strf("cos(%s)",  $3); }
    | SQRT '(' arg_list_opt ')' { $$ = strf("sqrt(%s)", $3); }
    | POW  '(' arg_list_opt ')' { $$ = strf("pow(%s)",  $3); }
    ;

arg_list_opt
    : /* empty */             { $$ = strdup(""); }
    | arg_list                { $$ = $1; }
    ;

arg_list
    : expr                    { $$ = strdup($1); }
    | arg_list ',' expr       { $$ = strf("%s, %s", $1, $3); }
    ;

%%

void yyerror(const char *s)
{
    fprintf(stderr, "Parse error: %s\n", s);
}
