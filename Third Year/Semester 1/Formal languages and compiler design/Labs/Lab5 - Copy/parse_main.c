#include <stdio.h>

int yyparse(void);
extern FILE* yyin;

int main(int argc, char** argv)
{
    if (argc < 2) {
        fprintf(stderr, "Usage: %s source.src\n", argv[0]);
        return 1;
    }

    yyin = fopen(argv[1], "r");
    if (!yyin) {
        perror(argv[1]);
        return 2;
    }

    if (yyparse() == 0) {
        printf("Parsing finished successfully.\n");
    }

    fclose(yyin);
    return 0;
}
