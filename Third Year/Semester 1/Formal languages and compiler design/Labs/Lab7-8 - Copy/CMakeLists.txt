cmake_minimum_required(VERSION 3.27)
project(Lab3 C)

set(CMAKE_C_STANDARD 99)

# Flex/Bison for DSL translator
find_package(BISON REQUIRED)
find_package(FLEX REQUIRED)

# Generate parser and scanner
BISON_TARGET(DslParser parser.y ${CMAKE_CURRENT_BINARY_DIR}/parser.tab.c)
FLEX_TARGET(DslScanner parser.l ${CMAKE_CURRENT_BINARY_DIR}/lexer.yy.c)
ADD_FLEX_BISON_DEPENDENCY(DslScanner DslParser)

# Existing scanner target (Lab 3-6)
add_executable(Lab3 main.c
        st_bst.c
        st_hash.c
        fa.h
        fa.c)

# Lab 7: LL(1) parser for seminar grammar (Requirement 1)
add_executable(ll1_seminar
        ll1_seminar.c)

# Lab 7: LL(1) parser for mini DSL using PIF (Requirement 2)
add_executable(ll1_mini
        ll1_mini.c)

# Lab 8: AI-Generated parser for mini DSL
add_executable(mini_ai_parser
        mini_ai_parser.c
        mini_ai_parser.c)

# Lab 9: YACC-based translator from mini-DSL to C
add_executable(dsl_translator
        parse_main.c
        ${BISON_DslParser_OUTPUTS}
        ${FLEX_DslScanner_OUTPUTS})
target_include_directories(dsl_translator PRIVATE ${CMAKE_CURRENT_BINARY_DIR})
