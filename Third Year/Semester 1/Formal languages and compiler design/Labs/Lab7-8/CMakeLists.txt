cmake_minimum_required(VERSION 3.27)
project(Lab3 C)

set(CMAKE_C_STANDARD 99)

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
