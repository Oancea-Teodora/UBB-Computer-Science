cmake_minimum_required(VERSION 3.27)
project(Lab3)

set(CMAKE_CXX_STANDARD 17)

add_executable(Lab3 main.c
        st_bst.c
        st_hash.c
        fa.h
        fa.c
        parse_main.c)
