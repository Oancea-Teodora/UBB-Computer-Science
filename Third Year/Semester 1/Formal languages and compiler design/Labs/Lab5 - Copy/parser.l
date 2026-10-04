%option noyywrap

%{
#include "parser.tab.h"
#include <stdio.h>
%}

WS      [ \t\r\n]+
ID      [A-Za-z][A-Za-z0-9_]*
NUM     [0-9]+(\.[0-9]+)?

%%

{WS}      ;

"let"     { return LET; }
"print"   { return PRINT; }
"if"      { return IF; }
"then"    { return THEN; }
"else"    { return ELSE; }
"end"     { return END; }
"and"     { return AND; }
"or"      { return OR; }

"sin"     { return SIN; }
"cos"     { return COS; }
"sqrt"    { return SQRT; }
"pow"     { return POW; }

"=="      { return EQ; }
"!="      { return NEQ; }
"<="      { return LE; }
">="      { return GE; }

{NUM}     { return NUM; }
{ID}      { return ID; }

"("       { return '('; }
")"       { return ')'; }
","       { return ','; }
";"       { return ';'; }
"="       { return '='; }
"<"       { return '<'; }
">"       { return '>'; }
"+"       { return '+'; }
"-"       { return '-'; }
"*"       { return '*'; }
"/"       { return '/'; }

.         { printf("Unknown character: %s\n", yytext); }

%%
