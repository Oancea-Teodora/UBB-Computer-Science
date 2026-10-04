#include "st.h"
#include <stdlib.h>
#include <string.h>


typedef struct Node {
    char* key;
    double val;
    int id;
    struct Node* L;
    struct Node* R;
} Node;

struct BST {
    Node* root;
    int next_id;
};

static double to_num(const char* s) { return strtod(s, NULL); }

BST* bst_create(void) {
    BST* T = (BST*)calloc(1, sizeof(BST));
    T->root = NULL; T->next_id = 0;
    return T;
}

static int contains_node(Node* p, double x) {
    while (p) {
        if (x < p->val) p = p->L;
        else if (x > p->val) p = p->R;
        else return 1;
    }
    return 0;
}

int bst_contains(BST* st, const char* key) {
    if (!st || !key) return 0;
    return contains_node(st->root, to_num(key));
}

STPos bst_insert(BST* st, const char* key) {
    STPos pos = {-1,-1};
    if (!st || !key) return pos;
    double x = to_num(key);

    if (!st->root) {
        Node* n = (Node*)calloc(1, sizeof(Node));
        n->key = strdup(key); n->val = x; n->id = st->next_id++;
        st->root = n; pos.a = n->id; pos.b = -1; return pos;
    }

    Node* p = st->root;
    while (1) {
        if (x < p->val) {
            if (p->L) { p = p->L; }
            else {
                Node* n = (Node*)calloc(1, sizeof(Node));
                n->key = strdup(key); n->val = x; n->id = st->next_id++;
                p->L = n; pos.a = n->id; pos.b = -1; return pos;
            }
        } else if (x > p->val) {
            if (p->R) { p = p->R; }
            else {
                Node* n = (Node*)calloc(1, sizeof(Node));
                n->key = strdup(key); n->val = x; n->id = st->next_id++;
                p->R = n; pos.a = n->id; pos.b = -1; return pos;
            }
        } else {
            pos.a = p->id; pos.b = -1; return pos;
        }
    }
}

static void inorder(Node* p, FILE* out) {
    if (!p) return;
    inorder(p->L, out);
    fprintf(out, "%d : %s\n", p->id, p->key);
    inorder(p->R, out);
}

void bst_dump(BST* st, FILE* out, const char* label) {
    if (!st || !out) return;
    fprintf(out, "# Symbol Table (BST) — %s\n", label?label:"");
    inorder(st->root, out);
}

static void free_bst(Node* p) {
    if (!p) return;
    free_bst(p->L); free_bst(p->R);
    free(p->key); free(p);
}

void bst_free(BST* st) {
    if (!st) return;
    free_bst(st->root);
    free(st);
}
