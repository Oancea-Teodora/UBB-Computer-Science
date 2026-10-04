/* LL(1) Parser for Seminar Grammar: S->BA, A->+BA|ε, B->DC, C->*DC|ε, D->(S)|a */
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

static const char *src;
static int p;
static char t;

static void next(void) {
    while (src[p] && isspace(src[p])) p++;
    t = src[p] ? src[p++] : '$';
}

static void expect(char c) {
    if (t != c) { fprintf(stderr, "Error: expected '%c', got '%c'\n", c, t); exit(1); }
    next();
}

static void S(void);
static void A(void) { if (t == '+') { printf("A -> + B A\n"); next(); S(); A(); } else printf("A -> e\n"); }
static void D(void) {
    if (t == '(') { printf("D -> ( S )\n"); next(); S(); A(); expect(')'); }
    else if (t == 'a') { printf("D -> a\n"); next(); }
    else { fprintf(stderr, "Error: expected '(' or 'a'\n"); exit(1); }
}
static void C(void) { if (t == '*') { printf("C -> * D C\n"); next(); D(); C(); } else printf("C -> e\n"); }
static void B(void) { printf("B -> D C\n"); D(); C(); }
static void S(void) { printf("S -> B A\n"); B(); }

int main(int argc, char **argv) {
    if (argc < 2) { printf("Usage: %s \"expression\"\n", argv[0]); return 1; }
    src = argv[1]; p = 0;
    next();
    S(); A();
    if (t != '$') { fprintf(stderr, "Error: extra input\n"); return 1; }
    printf("OK\n");
    return 0;
}
