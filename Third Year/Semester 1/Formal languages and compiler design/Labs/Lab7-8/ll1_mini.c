#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

enum
{
    T_EOF,
    T_LET,
    T_PRINT,
    T_IF,
    T_THEN,
    T_ELSE,
    T_END,
    T_AND,
    T_OR,
    T_SIN,
    T_COS,
    T_SQRT,
    T_POW,
    T_LPAR,
    T_RPAR,
    T_COMMA,
    T_SEMI,
    T_ASSIGN,
    T_EQ,
    T_NEQ,
    T_LT,
    T_LE,
    T_GT,
    T_GE,
    T_PLUS,
    T_MINUS,
    T_MUL,
    T_DIV,
    T_ID,
    T_NUM
};

static int *tok, ntok, pos;
static char *res[64];
static int nres;

typedef struct
{
    char info[32];
    int parent;
    int sibling;
} Node;
static Node tree[2000];
static int nnodes = 0;

static int add_node(const char *info, int parent)
{
    int idx = ++nnodes;
    strcpy(tree[idx].info, info);
    tree[idx].parent = parent;
    tree[idx].sibling = 0;
    return idx;
}

static void set_sibling(int node, int sib)
{
    if (node > 0)
        tree[node].sibling = sib;
}

static int look() { return pos < ntok ? tok[pos] : T_EOF; }
static void adv()
{
    if (pos < ntok)
        pos++;
}
static void expect(int t)
{
    if (look() != t)
    {
        fprintf(stderr, "Error at %d\n", pos);
        exit(1);
    }
    adv();
}

static int map(const char *s)
{
    const char *k[] = {"let", "print", "if", "then", "else", "end", "and", "or", "sin", "cos", "sqrt", "pow",
                       "(", ")", ",", ";", "=", "==", "!=", "<", "<=", ">", ">=", "+", "-", "*", "/"};
    for (int i = 0; i < 27; i++)
        if (!strcmp(s, k[i]))
            return i + 1;
    return -1;
}

static void load(const char *tf, const char *pf)
{
    FILE *f = fopen(tf, "r");
    char ln[256];
    while (fgets(ln, 256, f))
    {
        char *s = ln;
        while (isspace(*s))
            s++;
        char *e = s + strlen(s);
        while (e > s && isspace(e[-1]))
            *--e = 0;
        if (*s)
        {
            for (char *p = s; *p; p++)
                *p = tolower(*p);
            res[nres++] = strdup(s);
        }
    }
    fclose(f);
    f = fopen(pf, "r");
    int cap = 256;
    tok = malloc(cap * sizeof(int));
    int c, a, b;
    while (fgets(ln, 256, f))
    {
        sscanf(ln, "%d%d%d", &c, &a, &b);
        if (ntok >= cap)
        {
            cap *= 2;
            tok = realloc(tok, cap * sizeof(int));
        }
        tok[ntok++] = (c == 100) ? T_ID : (c == 101) ? T_NUM
                                                     : map(res[c - 1]);
    }
    fclose(f);
    tok[ntok++] = T_EOF;
}

static int expr(int parent);
static int stmt_list(int parent);

static int arg_tail(int parent)
{
    int n = add_node("arg_tail", parent);
    if (look() == T_COMMA)
    {
        int c = add_node(",", n);
        adv();
        int e = expr(n);
        set_sibling(c, e);
        int t = arg_tail(n);
        set_sibling(e, t);
    }
    else
        add_node("e", n);
    return n;
}
static int call(int parent)
{
    int n = add_node("call", parent);
    int f = add_node("func", n);
    adv();
    int lp = add_node("(", n);
    expect(T_LPAR);
    set_sibling(f, lp);
    int args = 0;
    if (look() != T_RPAR)
    {
        args = expr(n);
        int at = arg_tail(n);
        set_sibling(args, at);
        set_sibling(lp, args);
    }
    int rp = add_node(")", n);
    expect(T_RPAR);
    if (args)
        set_sibling(arg_tail(n) - 1 > 0 ? nnodes - 1 : args, rp);
    else
        set_sibling(lp, rp);
    return n;
}
static int primary(int parent)
{
    int n = add_node("primary", parent);
    int t = look();
    if (t == T_NUM)
    {
        add_node("NUM", n);
        adv();
    }
    else if (t == T_ID)
    {
        add_node("ID", n);
        adv();
    }
    else if (t == T_LPAR)
    {
        int lp = add_node("(", n);
        adv();
        int e = expr(n);
        set_sibling(lp, e);
        int rp = add_node(")", n);
        expect(T_RPAR);
        set_sibling(e, rp);
    }
    else if (t >= T_SIN && t <= T_POW)
    {
        int c = call(n);
        (void)c;
    }
    else
    {
        fprintf(stderr, "Error primary\n");
        exit(1);
    }
    return n;
}
static int unary(int parent)
{
    int n = add_node("unary", parent);
    if (look() == T_MINUS)
    {
        int m = add_node("-", n);
        adv();
        int u = unary(n);
        set_sibling(m, u);
    }
    else
    {
        int p = primary(n);
        (void)p;
    }
    return n;
}
static int mul_tail(int parent)
{
    int n = add_node("mul_tail", parent);
    int t = look();
    if (t == T_MUL || t == T_DIV)
    {
        int op = add_node(t == T_MUL ? "*" : "/", n);
        adv();
        int u = unary(n);
        set_sibling(op, u);
        int mt = mul_tail(n);
        set_sibling(u, mt);
    }
    else
        add_node("e", n);
    return n;
}
static int mul_expr(int parent)
{
    int n = add_node("mul_expr", parent);
    int u = unary(n);
    int mt = mul_tail(n);
    set_sibling(u, mt);
    return n;
}
static int add_tail(int parent)
{
    int n = add_node("add_tail", parent);
    int t = look();
    if (t == T_PLUS || t == T_MINUS)
    {
        int op = add_node(t == T_PLUS ? "+" : "-", n);
        adv();
        int me = mul_expr(n);
        set_sibling(op, me);
        int at = add_tail(n);
        set_sibling(me, at);
    }
    else
        add_node("e", n);
    return n;
}
static int add_expr(int parent)
{
    int n = add_node("add_expr", parent);
    int me = mul_expr(n);
    int at = add_tail(n);
    set_sibling(me, at);
    return n;
}
static int cmp_tail(int parent)
{
    int n = add_node("cmp_tail", parent);
    int t = look();
    const char *ops[] = {"==", "!=", "<", "<=", ">", ">="};
    if (t >= T_EQ && t <= T_GE)
    {
        int op = add_node(ops[t - T_EQ], n);
        adv();
        int ae = add_expr(n);
        set_sibling(op, ae);
    }
    else
        add_node("e", n);
    return n;
}
static int cmp_expr(int parent)
{
    int n = add_node("cmp_expr", parent);
    int ae = add_expr(n);
    int ct = cmp_tail(n);
    set_sibling(ae, ct);
    return n;
}
static int and_tail(int parent)
{
    int n = add_node("and_tail", parent);
    if (look() == T_AND)
    {
        int op = add_node("and", n);
        adv();
        int ce = cmp_expr(n);
        set_sibling(op, ce);
        int at = and_tail(n);
        set_sibling(ce, at);
    }
    else
        add_node("e", n);
    return n;
}
static int and_expr(int parent)
{
    int n = add_node("and_expr", parent);
    int ce = cmp_expr(n);
    int at = and_tail(n);
    set_sibling(ce, at);
    return n;
}
static int or_tail(int parent)
{
    int n = add_node("or_tail", parent);
    if (look() == T_OR)
    {
        int op = add_node("or", n);
        adv();
        int ae = and_expr(n);
        set_sibling(op, ae);
        int ot = or_tail(n);
        set_sibling(ae, ot);
    }
    else
        add_node("e", n);
    return n;
}
static int or_expr(int parent)
{
    int n = add_node("or_expr", parent);
    int ae = and_expr(n);
    int ot = or_tail(n);
    set_sibling(ae, ot);
    return n;
}
static int expr(int parent)
{
    int n = add_node("expr", parent);
    int oe = or_expr(n);
    (void)oe;
    return n;
}

static int else_part(int parent)
{
    int n = add_node("else_part", parent);
    if (look() == T_ELSE)
    {
        int el = add_node("else", n);
        adv();
        int sl = stmt_list(n);
        set_sibling(el, sl);
    }
    else
        add_node("e", n);
    return n;
}
static int if_stmt(int parent)
{
    int n = add_node("if_stmt", parent);
    int k1 = add_node("if", n);
    expect(T_IF);
    int e = expr(n);
    set_sibling(k1, e);
    int k2 = add_node("then", n);
    expect(T_THEN);
    set_sibling(e, k2);
    int sl = stmt_list(n);
    set_sibling(k2, sl);
    int ep = else_part(n);
    set_sibling(sl, ep);
    int k3 = add_node("end", n);
    expect(T_END);
    set_sibling(ep, k3);
    return n;
}
static int print_stmt(int parent)
{
    int n = add_node("print_stmt", parent);
    int k = add_node("print", n);
    expect(T_PRINT);
    int e = expr(n);
    set_sibling(k, e);
    int s = add_node(";", n);
    expect(T_SEMI);
    set_sibling(e, s);
    return n;
}
static int let_stmt(int parent)
{
    int n = add_node("let_stmt", parent);
    int k = add_node("let", n);
    expect(T_LET);
    int id = add_node("ID", n);
    expect(T_ID);
    set_sibling(k, id);
    int eq = add_node("=", n);
    expect(T_ASSIGN);
    set_sibling(id, eq);
    int e = expr(n);
    set_sibling(eq, e);
    int s = add_node(";", n);
    expect(T_SEMI);
    set_sibling(e, s);
    return n;
}
static int assign_stmt(int parent)
{
    int n = add_node("assign_stmt", parent);
    int id = add_node("ID", n);
    expect(T_ID);
    int eq = add_node("=", n);
    expect(T_ASSIGN);
    set_sibling(id, eq);
    int e = expr(n);
    set_sibling(eq, e);
    int s = add_node(";", n);
    expect(T_SEMI);
    set_sibling(e, s);
    return n;
}
static int stmt(int parent)
{
    int n = add_node("stmt", parent);
    int t = look(), child;
    if (t == T_LET)
        child = let_stmt(n);
    else if (t == T_ID)
        child = assign_stmt(n);
    else if (t == T_PRINT)
        child = print_stmt(n);
    else if (t == T_IF)
        child = if_stmt(n);
    else
    {
        fprintf(stderr, "Error stmt\n");
        exit(1);
    }
    (void)child;
    return n;
}
static int starts_stmt()
{
    int t = look();
    return t == T_LET || t == T_PRINT || t == T_IF || t == T_ID;
}
static int stmt_list(int parent)
{
    int n = add_node("stmt_list", parent);
    if (starts_stmt())
    {
        int s = stmt(n);
        int sl = stmt_list(n);
        set_sibling(s, sl);
    }
    else
        add_node("e", n);
    return n;
}
static int program()
{
    int n = add_node("program", 0);
    int sl = stmt_list(n);
    (void)sl;
    expect(T_EOF);
    return n;
}

static void print_tree()
{
    printf("\nPARSE TREE (Parent-Sibling Table) \n");
    printf("%5s | %-15s | %6s | %7s\n", "index", "Info", "Parent", "Sibling");
    printf("------------------------------------------------------\n");
    for (int i = 1; i <= nnodes; i++)
        printf("%5d | %-15s | %6d | %7d\n", i, tree[i].info, tree[i].parent, tree[i].sibling);
}

int main(int argc, char **argv)
{
    if (argc < 3)
    {
        fprintf(stderr, "Usage: %s tokens.in pif.out\n", argv[0]);
        return 1;
    }
    load(argv[1], argv[2]);
    program();
    print_tree();
    printf("\nOK - Parsed %d nodes\n", nnodes);
    return 0;
}
