#include <stdio.h>
#include <stdlib.h>
#include "st.h"

void scanner_set(FILE* pif, FILE* err, HashST* ids, BST* consts);
void scanner_load(const char* path);
int  yylex(void);
extern FILE* yyin;

int main(int argc, char** argv) {
    if (argc < 3) {
        fprintf(stderr, "Usage: %s tokens.in source.src\n", argv[0]);
        return 1;
    }

    const char* tokens_path = argv[1];
    const char* source_path = argv[2];

    system("mkdir -p out");
    FILE* pif = fopen("out/pif.out", "w");
    FILE* err = fopen("out/errors.out", "w");
    FILE* st_i= fopen("out/st_id.out", "w");
    FILE* st_c= fopen("out/st_const.out", "w");
    if (!pif || !err || !st_i || !st_c) { perror("open outputs"); return 2; }

    HashST* IDT = hash_create(17);
    BST* CST = bst_create();

    scanner_set(pif, err, IDT, CST);
    scanner_load(tokens_path);

    yyin = fopen(source_path, "r");
    if (!yyin) { perror(source_path); return 3; }

    yylex();

    hash_dump(IDT, st_i, "Identifiers");
    bst_dump(CST,  st_c, "Constants");

    fclose(yyin);
    fclose(pif); fclose(err); fclose(st_i); fclose(st_c);
    hash_free(IDT); bst_free(CST);

    printf("Lexical analysis complete. See ./out for results.\n");
    return 0;
}
