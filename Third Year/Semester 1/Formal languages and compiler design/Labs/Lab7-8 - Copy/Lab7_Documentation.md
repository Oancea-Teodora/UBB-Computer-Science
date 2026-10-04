# Lab 7 - LL(1) Parser Implementation

## Overview

LL(1) recursive-descent parsers for syntactical analysis with parent-sibling parse tree output.

## Files

| File | Description |
|------|-------------|
| `ll1_seminar.c` | Parser for seminar grammar (Req. 1) |
| `ll1_mini.c` | Parser for mini DSL with parse tree (Req. 2) |

## Requirement 1: Seminar Grammar

**Grammar:**
```
S → B A      A → + B A | ε
B → D C      C → * D C | ε
D → ( S ) | a
```

**Terminals:** `a`, `+`, `*`, `(`, `)`

**Usage:** `ll1_seminar "a*(a+a)"`

**Output:** String of productions used during parsing.

**Example:**
```
S -> B A
B -> D C
D -> a
C -> * D C
...
OK
```

## Requirement 2: Mini DSL Parser

**Output format:** Parent-Sibling Table (grade 10)

**Usage:** `ll1_mini tokens.in out/pif.out`

**Prerequisites:** Generate PIF first:
```bash
lexer.exe tokens.in sample.src
```

**Output format:**
```
index | Info            | Parent | Sibling
------------------------------------------------------
    1 | program         |      0 |       0
    2 | stmt_list       |      1 |       0
    3 | stmt            |      2 |      28
    ...
```

**Table columns:**
- **index**: Node number (1-based)
- **Info**: Symbol name (terminal or non-terminal)
- **Parent**: Parent node index (0 = root)
- **Sibling**: Right sibling index (0 = none)

**Example input (`sample.src`):**
```c
let a = 3.5;
let b = 2;
let hyp = sqrt(a*a + b*b);
print hyp;
if hyp > 4 then
  print pow(hyp, 2);
else
  print 0;
end
```

## Grammar (Mini DSL)

**Key rules:**
- `program → stmt_list`
- `stmt → let_stmt | assign_stmt | print_stmt | if_stmt`
- `let_stmt → 'let' ID '=' expr ';'`
- `expr → or_expr → and_expr → cmp_expr → add_expr → mul_expr → unary → primary`
- `primary → NUM | ID | '(' expr ')' | call`
- `call → func '(' arg_list ')'`

**Operators:** `+`, `-`, `*`, `/`, `==`, `!=`, `<`, `<=`, `>`, `>=`, `and`, `or`  
**Functions:** `sin`, `cos`, `sqrt`, `pow`

## Testing

**Seminar parser:** Change program arguments to test different expressions:
- `"a+a"`, `"(a+a)*a"`, `"a*a+a"`

**Mini DSL parser:** Edit `sample.src`, then:
1. Regenerate PIF: `lexer.exe tokens.in sample.src`
2. Run parser: `ll1_mini tokens.in out/pif.out`

## Implementation Details

### Parsing Algorithm

**Recursive-Descent LL(1):**
- Each non-terminal maps to a function (e.g., `expr()`, `stmt_list()`)
- Functions use single-token lookahead (`look()`) to choose productions
- Epsilon productions chosen when no terminal matches FIRST set
- Token stream advanced with `adv()` after consuming tokens

**Token Management:**
- `ll1_seminar`: Scans characters directly from input string
- `ll1_mini`: Reads tokens from PIF file (codes 100=ID, 101=NUM, 1..N=reserved)

### Parse Tree Construction

**Node Structure:**
```c
typedef struct {
    char info[32];    // Symbol name
    int parent;       // Parent node index (0 = root)
    int sibling;      // Right sibling index (0 = none)
} Node;
```

**Tree Building:**
- Each grammar rule application creates a node via `add_node()`
- Parent-child relationships: child nodes store parent index
- Sibling relationships: `set_sibling()` links right siblings
- Tree built bottom-up during recursive descent
- Root node (program) has parent=0

**Example:** For `let_stmt → let ID = expr ;`:
- Creates `let_stmt` node
- Creates children: `let`, `ID`, `=`, `expr`, `;`
- Links siblings: `let` → `ID` → `=` → `expr` → `;`

### Error Handling

- Syntax errors: Reports expected vs actual token at position
- Invalid PIF codes: Exits with error message
- Extra input: Checks EOF after successful parse

### Key Functions

**ll1_seminar:**
- `next()`: Scans next token, skips whitespace
- `expect(c)`: Verifies token matches, advances on success

**ll1_mini:**
- `load()`: Reads `tokens.in` and `pif.out`, builds token array
- `map()`: Converts lexeme strings to terminal enum values
- `look()`: Returns current token without advancing
- `expect(t)`: Verifies token type, advances on success
