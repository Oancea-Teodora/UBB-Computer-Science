# Lab 8 - AI-Generated Parser Documentation

## Introduction

**Goal:** Use GenAI to generate a C parser for the mini DSL, validate it, and compare with existing yacc parser.

**Tools used:** Cursor AI (Claude)

---

## Requirement 1: Using GenAI to Generate Parser

### Prompt Sequence

**Prompt 1 - Initial Request:**
```
For the mini DSL, implement parsing (syntactical analysis) using LL(1) or LR(0) algorithms.
[... full grammar specification ...]
Generate C code for a recursive-descent LL(1) parser for this mini-DSL.
The parser must read tokens.in and pif.out, map the PIF to tokens, then parse 
and print the sequence of productions used.
```

**AI Response:** Generated initial `ll1_mini.c` with recursive-descent parser.

**Prompt 2 - Simplification:**
```
make everything shorter, less code, less everything, it needs to be easy not complicated
```

**AI Response:** Condensed the code from 624 lines to 95 lines.

**Prompt 3 - Grade 10 Feature:**
```
i want 10 [referring to parent-sibling table output]
```

**AI Response:** Added parse tree construction with parent-sibling table output.

**Prompt 4 - Lab 8 Parser:**
```
[Lab 8 requirements specification]
```

**AI Response:** Generated `mini_ai_parser.c` with:
- Better structured code with clear sections
- Improved error messages
- Productions string output
- Parent-sibling parse tree output
- Quiet mode option (-q flag)

### Generated Files

| File | Description |
|------|-------------|
| `mini_ai_parser.c` | AI-generated recursive-descent LL(1) parser |

---

## Requirement 2: Validation

### Test Programs

**Valid Programs:**

| File | Description | Expected |
|------|-------------|----------|
| `test_valid1.src` | Simple variable declaration and print | PASS |
| `test_valid2.src` | Function calls (sin, cos) | PASS |
| `test_valid3.src` | If-else statement | PASS |
| `sample.src` | Full example from previous labs | PASS |

**Invalid Programs:**

| File | Description | Expected Error |
|------|-------------|----------------|
| `test_invalid1.src` | Missing semicolon | Syntax error |
| `test_invalid2.src` | Missing `end` keyword | Syntax error |
| `test_invalid3.src` | Missing expression | Syntax error |

### Validation Process

1. **Compile** the AI-generated parser:
   ```bash
   cmake --build . --target mini_ai_parser
   ```

2. **Generate PIF** for each test file:
   ```bash
   ./Lab3.exe tokens.in test_valid1.src
   ```

3. **Run parser** on PIF:
   ```bash
   ./mini_ai_parser.exe tokens.in out/pif.out
   ```

4. **Check results:**
   - Valid programs → "PARSING SUCCESSFUL"
   - Invalid programs → "SYNTAX ERROR" with location

### Bug Report

| Bug | Description | Cause | Fix |
|-----|-------------|-------|-----|
| #1 | MSVC compile error: `inp` reserved | Variable name conflict | Renamed to `src` |
| #2 | IDE reformatted code to 446 lines | Auto-formatting | Rewrote compact version |

**Note:** Most generated code worked correctly on first attempt.

---

## Requirement 3: Comparison with Yacc Parser

### Test Setup

- **AI Parser:** `mini_ai_parser.exe tokens.in out/pif.out`
- **Yacc Parser:** `parser.exe` (from Lab 6)

### Comparison Table

| Test | AI Parser Result | Yacc Parser Result | Notes |
|------|------------------|-------------------|-------|
| `sample.src` | ✅ PASS, 607 nodes | ✅ PASS | Both accept |
| `test_valid1.src` | ✅ PASS | ✅ PASS | Same result |
| `test_valid2.src` | ✅ PASS | ✅ PASS | Same result |
| `test_valid3.src` | ✅ PASS | ✅ PASS | Same result |
| `test_invalid1.src` | ❌ Error at token X | ❌ Syntax error | Both reject |
| `test_invalid2.src` | ❌ Expected 'end' | ❌ Syntax error | Both reject |
| `test_invalid3.src` | ❌ Expected expression | ❌ Syntax error | Both reject |

### Key Differences

| Aspect | AI Parser | Yacc Parser |
|--------|-----------|-------------|
| **Approach** | Recursive-descent LL(1) | LALR(1) table-driven |
| **Error messages** | Descriptive (expected X, got Y) | Generic "syntax error" |
| **Output** | Productions + parse tree table | Parse result only |
| **Code size** | ~500 lines C | ~100 lines .y + generated |
| **Dependencies** | None (pure C) | Requires yacc/bison |
| **Maintainability** | Easy to read/modify | Requires yacc knowledge |

### Pros and Cons

**AI-Generated Parser:**
- ✅ No external tools needed
- ✅ Better error messages
- ✅ Easy to understand and modify
- ✅ Produces detailed parse tree
- ❌ Manual grammar implementation
- ❌ Larger source code

**Yacc Parser:**
- ✅ Compact grammar specification
- ✅ Automatic parser generation
- ✅ Well-tested algorithm
- ❌ Requires yacc/bison tools
- ❌ Generic error messages
- ❌ Generated code hard to read

---

## Conclusion

The AI-generated parser successfully parses the mini DSL and produces correct results matching the yacc parser. Key observations:

1. **Quality:** AI generated functional code with minimal bugs
2. **Speed:** Parser was generated in minutes vs. hours of manual coding
3. **Flexibility:** Easy to modify for custom output (parse tree table)
4. **Trust:** Should be validated and tested, but generally reliable for standard parsing tasks

**When to use AI-generated parsers:**
- Rapid prototyping
- Learning/educational purposes
- Simple grammars
- When detailed error messages needed

**When to prefer yacc/ANTLR:**
- Production compilers
- Complex grammars with conflicts
- Standardized toolchain required

---

## Appendix: Running the Parser

**Build:**
```bash
cd cmake-build-debug
cmake --build . --target mini_ai_parser
```

**Run:**
```bash
# First generate PIF with scanner
./Lab3.exe tokens.in sample.src

# Then run AI parser
./mini_ai_parser.exe tokens.in out/pif.out

# Quiet mode (no productions)
./mini_ai_parser.exe tokens.in out/pif.out -q
```

**CLion Configuration:**
- Target: `mini_ai_parser`
- Program arguments: `tokens.in out/pif.out`
- Working directory: `$ProjectFileDir$`
