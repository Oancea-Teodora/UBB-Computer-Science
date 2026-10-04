%option noyywrap yylineno
%{
#define YY_NO_UNISTD_H 1
#ifdef _WIN32
  #include <io.h>
  #define isatty  _isatty
  #define fileno  _fileno
#endif

#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>
#include "st.h"


#define ID_CODE    100
#define CONST_CODE 101

static FILE*  PIF = NULL;
static FILE*  ERR = NULL;
static HashST* IDT = NULL;
static BST*    CST = NULL;

#define MAXTOK 128
static char TOKS[MAXTOK][32];
static int  NTOK = 0;

static void tolower_str(char* s){ for(; *s; ++s) *s=(char)tolower(*s); }
static void trim_end(char* s){ size_t n=strlen(s); while(n && (s[n-1]=='\n'||s[n-1]=='\r'||s[n-1]==' '||s[n-1]=='\t')) s[--n]='\0'; }
static int code_of(const char* t){ for(int i=0;i<NTOK;i++) if(strcmp(TOKS[i],t)==0) return i+1; return 0; }

void scanner_set(FILE* pif, FILE* err, HashST* ids, BST* consts){ PIF=pif; ERR=err; IDT=ids; CST=consts; }
void scanner_load(const char* path){
    FILE* f=fopen(path,"r"); if(!f){ perror("tokens.in"); exit(1); }
    char buf[64];
    while(fgets(buf,sizeof buf,f)){
        trim_end(buf);
        if(!buf[0] || buf[0]=='#') continue;
        if(NTOK<MAXTOK){ strncpy(TOKS[NTOK],buf,31); TOKS[NTOK][31]='\0'; NTOK++; }
    }
    fclose(f);
}

static void emit_reserved(const char* lex){
    int c=code_of(lex);
    if(!c){ char low[64]; strncpy(low,lex,63); low[63]='\0'; tolower_str(low); c=code_of(low); }
    if(!c){ fprintf(ERR,"line %d: unknown token '%s'\n", yylineno, lex); return; }
    fprintf(PIF,"%d -1 -1\n", c);
}
static void emit_id(const char* lex){ STPos p=hash_insert(IDT,lex); fprintf(PIF,"%d %d %d\n", ID_CODE, p.a, p.b); }
static void emit_const(const char* lex){ STPos p=bst_insert(CST,lex); fprintf(PIF,"%d %d %d\n", CONST_CODE, p.a, p.b); }
static void emit_err(const char* lex){ fprintf(ERR,"line %d: invalid token '%s'\n", yylineno, lex); }
%}

WS      [ \t\r\n]+
ID      [A-Za-z][A-Za-z0-9_]*
NUM     [0-9]+(\.[0-9]+)?

%%
{WS} ;

"==" { emit_reserved("=="); }
"!=" { emit_reserved("!="); }
"<=" { emit_reserved("<="); }
">=" { emit_reserved(">="); }

[(),;] { emit_reserved(yytext); }
[=+\-*/] { emit_reserved(yytext); }
[<>] { emit_reserved(yytext); }

{NUM} { emit_const(yytext); }

{ID} {
    char tmp[128]; strncpy(tmp, yytext, 127); tmp[127]='\0';
    char low[128]; strncpy(low,tmp,127); low[127]='\0'; tolower_str(low);
    int c = code_of(tmp); if(!c) c = code_of(low);
    if(c) fprintf(PIF,"%d -1 -1\n", c);
    else  emit_id(tmp);
}

.                         { emit_err(yytext); }
%%   /* <= Keep this final %% to avoid 'premature EOF' */
