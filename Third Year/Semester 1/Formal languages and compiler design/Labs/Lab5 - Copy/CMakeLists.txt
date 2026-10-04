cmake_minimum_required(VERSION 3.27)
project(Lab3 C)

set(CMAKE_C_STANDARD 11)

# Set paths to win_bison and win_flex
set(BISON_EXECUTABLE "C:/Users/teodo/AppData/Local/Microsoft/WinGet/Packages/WinFlexBison.win_flex_bison_Microsoft.Winget.Source_8wekyb3d8bbwe/win_bison.exe")
set(FLEX_EXECUTABLE "C:/Users/teodo/AppData/Local/Microsoft/WinGet/Packages/WinFlexBison.win_flex_bison_Microsoft.Winget.Source_8wekyb3d8bbwe/win_flex.exe")

# Generate parser files with custom commands
add_custom_command(
        OUTPUT ${CMAKE_CURRENT_SOURCE_DIR}/parser.tab.c ${CMAKE_CURRENT_SOURCE_DIR}/parser.tab.h
        COMMAND ${BISON_EXECUTABLE} -d ${CMAKE_CURRENT_SOURCE_DIR}/parser.y
        DEPENDS ${CMAKE_CURRENT_SOURCE_DIR}/parser.y
        WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}
        COMMENT "Generating parser with Bison"
)

add_custom_command(
        OUTPUT ${CMAKE_CURRENT_SOURCE_DIR}/lex.yy.c
        COMMAND ${FLEX_EXECUTABLE} ${CMAKE_CURRENT_SOURCE_DIR}/parser.l
        DEPENDS ${CMAKE_CURRENT_SOURCE_DIR}/parser.l ${CMAKE_CURRENT_SOURCE_DIR}/parser.tab.h
        WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}
        COMMENT "Generating lexer with Flex"
)

# Original Lab3 executable (scanner)
add_executable(Lab3 main.c
        st_bst.c
        st_hash.c
        fa.h
        fa.c)

# New parser executable
add_executable(parser 
        parse_main.c
        parser.tab.c
        lex.yy.c
)
