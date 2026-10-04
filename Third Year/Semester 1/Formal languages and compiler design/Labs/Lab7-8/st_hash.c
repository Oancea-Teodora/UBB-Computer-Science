#include "st.h"
#include <stdlib.h>
#include <string.h>

typedef struct Entry {
    char* key;
    int   slot;
    struct Entry* next;
} Entry;

struct HashST {
    int buckets;
    Entry** table;
};

static unsigned hashf(const char* s, int m) {
    unsigned h = 5381u;
    while (*s) { h = ((h << 5) + h) + (unsigned char)*s++; }
    return (m>0) ? (h % (unsigned)m) : 0u;
}

HashST* hash_create(int buckets) {
    if (buckets < 4) buckets = 17;
    HashST* H = (HashST*)calloc(1, sizeof(HashST));
    H->buckets = buckets;
    H->table = (Entry**)calloc((size_t)buckets, sizeof(Entry*));
    return H;
}

int hash_contains(HashST* st, const char* key) {
    if (!st || !key) return 0;
    unsigned b = hashf(key, st->buckets);
    for (Entry* p = st->table[b]; p; p = p->next)
        if (strcmp(p->key, key) == 0) return 1;
    return 0;
}

STPos hash_insert(HashST* st, const char* key) {
    STPos pos = {-1,-1};
    if (!st || !key) return pos;
    unsigned b = hashf(key, st->buckets);
    int idx = 0;
    for (Entry* p = st->table[b]; p; p = p->next, ++idx) {
        if (strcmp(p->key, key) == 0) { pos.a = (int)b; pos.b = p->slot; return pos; }
    }
    Entry* n = (Entry*)calloc(1, sizeof(Entry));
    n->key = strdup(key);
    n->slot = idx;
    n->next = st->table[b];
    st->table[b] = n;
    pos.a = (int)b; pos.b = idx;
    return pos;
}

void hash_dump(HashST* st, FILE* out, const char* label) {
    if (!st || !out) return;
    fprintf(out, "# Symbol Table (HASH) — %s\n", label?label:"");
    for (int b = 0; b < st->buckets; ++b) {
        for (Entry* p = st->table[b]; p; p = p->next) {
            fprintf(out, "(%d,%d) : %s\n", b, p->slot, p->key);
        }
    }
}

void hash_free(HashST* st) {
    if (!st) return;
    for (int b = 0; b < st->buckets; ++b) {
        Entry* p = st->table[b];
        while (p) { Entry* q = p->next; free(p->key); free(p); p = q; }
    }
    free(st->table);
    free(st);
}
