# Mini-DSL → C Translator (Yacc/Bison + Flex)

This guide shows how to translate and run mini-DSL programs using the provided Yacc (Bison) grammar and Flex lexer.

## Components
- Grammar: `parser.y`
- Lexer: `parser.l`
- Driver: `parse_main.c`
- Sample programs: `sample.src`, `test_valid1.src`, `test_valid2.src`, `test_valid3.src`

## Build the translator
Requires Bison, Flex, and a C compiler with math library (`-lm`).

```bash
# Generate parser and lexer
bison -d parser.y          # produces parser.tab.c / parser.tab.h
flex  parser.l             # produces lex.yy.c

# Compile translator
gcc parser.tab.c lex.yy.c parse_main.c -o mini_dsl_translator -lm
```

On Windows with MinGW, run the same commands in MSYS2/MinGW shell (ensure `bison`, `flex`, `gcc` are installed).

## Translate and run two sample programs
Example using the provided DSL files:

```bash
# 1) Translate test_valid1.src
./mini_dsl_translator test_valid1.src test_valid1.c
gcc test_valid1.c -o test_valid1.exe -lm
./test_valid1.exe

# 2) Translate sample.src
./mini_dsl_translator sample.src sample.c
gcc sample.c -o sample.exe -lm
./sample.exe
```

`mini_dsl_translator` defaults to writing `translated.c` if an output path is not supplied.

## DSL features supported
- Declarations/assignments: `let id = expr;` and `id = expr;`
- Printing: `print expr;`
- Conditionals: `if expr then ... else ... end` (else optional)
- Expressions: `+ - * /`, comparisons (`== != < <= > >=`), logical `and`, `or`, unary `-`
- Functions: `sin(x)`, `cos(x)`, `sqrt(x)`, `pow(a, b)`

## Notes
- Variables are emitted as `double` in the generated C code.
- The generated code includes `math.h`; always link with `-lm`.
- Whitespace and newlines are ignored by the lexer; each statement ends with `;` (except `if`/`end`).


