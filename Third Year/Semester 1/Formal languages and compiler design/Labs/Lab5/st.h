#ifndef ST_H
#define ST_H

#include <stdio.h>

typedef struct { int a; int b; } STPos;

typedef struct HashST HashST;

//identifiers
HashST* hash_create(int buckets);
STPos   hash_insert(HashST* st, const char* key);
int     hash_contains(HashST* st, const char* key);
void    hash_dump(HashST* st, FILE* out, const char* label);
void    hash_free(HashST* st);

typedef struct BST BST;

//constants
BST*  bst_create(void);
STPos bst_insert(BST* st, const char* key);
int   bst_contains(BST* st, const char* key);
void  bst_dump(BST* st, FILE* out, const char* label);
void  bst_free(BST* st);

#endif
