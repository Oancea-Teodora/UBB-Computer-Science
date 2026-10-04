#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int yyparse(void);
extern FILE* yyin;
extern char* generated_code;

int main(int argc, char** argv)
{
    if (argc < 2) {
        fprintf(stderr, "Usage: %s source.src [out.c]\n", argv[0]);
        return 1;
    }

    yyin = fopen(argv[1], "r");
    if (!yyin) {
        perror(argv[1]);
        return 2;
    }

    if (yyparse() == 0) {
        const char* out_path = (argc >= 3) ? argv[2] : "translated.c";
        FILE* out = fopen(out_path, "w");
        if (!out) {
            perror(out_path);
            fclose(yyin);
            return 3;
        }
        if (generated_code) {
            fputs(generated_code, out);
        }
        fclose(out);
        printf("Translation complete. Wrote C code to %s\n", out_path);
    }

    fclose(yyin);
    return 0;
}
