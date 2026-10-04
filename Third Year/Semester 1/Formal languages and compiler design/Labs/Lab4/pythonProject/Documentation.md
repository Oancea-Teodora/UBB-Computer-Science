# Lab 4 
FA & RG for Identifiers and Numeric Constants (DSL)

## 1) Overview
This document defines Finite Automata (AF/DFAs) and Regular Grammars (RGs) for two token classes of the DSL from Lab 1:
- **Identifiers**: `[A-Za-z][A-Za-z0-9_]*`
- **Numeric constants**: `[0-9]+(\.[0-9]+)?`

It also describes a transformation between AF and RG (DFA → RG) and the reverse (RG → NFA), and summarizes a small Python script that demonstrates these constructions and membership tests.

## 2) Alphabets & Token Shapes
- **Identifiers**
  - Alphabet Σ: letters `{A…Z, a…z}`, digits `{0…9}`, underscore `{_}`
  - Regex: `[A-Za-z][A-Za-z0-9_]*` (must start with a letter)
- **Numeric constants**
  - Alphabet Σ: digits `{0…9}` plus the dot `{.}`
  - Regex: `[0-9]+(\.[0-9]+)?` (integers or decimals with at least one digit after the dot)

## 3) Formal Definitions

### 3.1 AF (DFA) - Identifiers
Let Σ be letters, digits, and `_`.  
DFA \(Q, Σ, δ, q_0, F\):
- **States**: Q = { q0, q1, qdead }
- **Start**: q0
- **Finals**: F = { q1 }
- **Transitions**:
  - δ(q0, letter) = q1; δ(q0, digit) = qdead; δ(q0, `_`) = qdead
  - δ(q1, letter|digit|`_`) = q1
  - Any other / missing transition → qdead; δ(qdead, a)=qdead

### 3.2 AF (DFA) - Numeric constants
Let Σ be digits and `.`.  
DFA \(Q, Σ, δ, q_0, F\):
- **States**: Q = { q0, qI, qDot, qF, qdead }
- **Start**: q0
- **Finals**: F = { qI, qF }
- **Transitions**:
  - δ(q0, digit) = qI
  - δ(qI, digit) = qI
  - δ(qI, `.`) = qDot
  - δ(qDot, digit) = qF
  - δ(qF, digit) = qF
  - Else → qdead; δ(qdead, a)=qdead  
This accepts integers (`qI`) and decimals with ≥1 digit after the dot (`qF`). It **rejects** `1.` and `.5`, matching the DSL grammar.

### 3.3 RG (Right-linear Grammar) - Identifiers
Let terminals Σ be letters, digits, `_`. Let nonterminals N = { S, A }.
- S → L A
- A → L A | D A | `_` A | ε  
(Here L = any letter; D = any digit.)

### 3.4 RG (Right-linear Grammar) - Numeric constants
Terminals Σ = digits ∪ {`.`}; Nonterminals N = { S, I, F, G }.
- S → D I
- I → D I | `.` F | ε
- F → D G
- G → D G | ε

