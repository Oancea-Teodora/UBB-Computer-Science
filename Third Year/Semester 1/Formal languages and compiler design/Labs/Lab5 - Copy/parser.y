%{
#include <stdio.h>

void yyerror(const char *s);
int yylex(void);
%}

%token LET PRINT IF THEN ELSE END AND OR
%token SIN COS SQRT POW
%token ID NUM
%token EQ NEQ LE GE

%left OR
%left AND
%nonassoc EQ NEQ '<' LE '>' GE
%left '+' '-'
%left '*' '/'
%right UMINUS

%%

program
    : stmt_list   { printf("program -> stmt_list\n"); }
    ;

stmt_list
    :	             { printf("stmt_list -> ε\n"); }
    | stmt_list stmt   { printf("stmt_list -> stmt_list stmt\n"); }
    ;

stmt
    : let_stmt
    | assign_stmt
    | print_stmt
    | if_stmt
    ;

let_stmt
    : LET ID '=' expr ';'     { printf("let_stmt -> LET ID = expr ;\n"); }
    ;

assign_stmt
    : ID '=' expr ';'  { printf("assign_stmt -> ID = expr ;\n"); }
    ;

print_stmt
    : PRINT expr ';'   { printf("print_stmt -> PRINT expr ;\n"); }
    ;

if_stmt
    : IF expr THEN stmt_list else_part END
                              { printf("if_stmt -> IF expr THEN stmt_list else_part END\n"); }
    ;

else_part
    : /* empty */             { printf("else_part -> ε\n"); }
    | ELSE stmt_list          { printf("else_part -> ELSE stmt_list\n"); }
    ;

expr
    : expr OR expr            { printf("expr -> expr OR expr\n"); }
    | expr AND expr           { printf("expr -> expr AND expr\n"); }
    | expr EQ expr            { printf("expr -> expr EQ expr\n"); }
    | expr NEQ expr           { printf("expr -> expr NEQ expr\n"); }
    | expr '<' expr           { printf("expr -> expr < expr\n"); }
    | expr LE expr            { printf("expr -> expr LE expr\n"); }
    | expr '>' expr           { printf("expr -> expr > expr\n"); }
    | expr GE expr            { printf("expr -> expr GE expr\n"); }
    | expr '+' expr           { printf("expr -> expr + expr\n"); }
    | expr '-' expr           { printf("expr -> expr - expr\n"); }
    | expr '*' expr           { printf("expr -> expr * expr\n"); }
    | expr '/' expr           { printf("expr -> expr / expr\n"); }
    | '-' expr %prec UMINUS   { printf("expr -> - expr\n"); }
    | primary                 { printf("expr -> primary\n"); }
    ;

primary
    : NUM                     { printf("primary -> NUM\n"); }
    | ID                      { printf("primary -> ID\n"); }
    | '(' expr ')'            { printf("primary -> ( expr )\n"); }
    | call                    { printf("primary -> call\n"); }
    ;

call
    : SIN  '(' arg_list_opt ')' { printf("call -> SIN ( arg_list_opt )\n"); }
    | COS  '(' arg_list_opt ')' { printf("call -> COS ( arg_list_opt )\n"); }
    | SQRT '(' arg_list_opt ')' { printf("call -> SQRT ( arg_list_opt )\n"); }
    | POW  '(' arg_list_opt ')' { printf("call -> POW ( arg_list_opt )\n"); }
    ;

arg_list_opt
    :             { printf("arg_list_opt -> ε\n"); }
    | arg_list                { printf("arg_list_opt -> arg_list\n"); }
    ;

arg_list
    : expr                    { printf("arg_list -> expr\n"); }
    | arg_list ',' expr       { printf("arg_list -> arg_list , expr\n"); }
    ;

%%

void yyerror(const char *s)
{
    fprintf(stderr, "Parse error: %s\n", s);
}
