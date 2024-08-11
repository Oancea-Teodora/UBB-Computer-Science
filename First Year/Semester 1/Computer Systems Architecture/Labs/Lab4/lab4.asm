bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    a dw 10
    b dw 89
    c dd 0
; our code starts here
segment code use32 class=code
    start:
    ;Se dau cuvintele A si B. Sa se obtina dublucuvantul C:
    ;bitii 0-2 ai lui C coincid cu bitii 12-14 ai lui A
    ;bitii 3-8 ai lui C coincid cu bitii 0-5 ai lui B
    ;bitii 9-15 ai lui C coincid cu bitii 3-9 ai lui A
    ;bitii 16-31 ai lui C coincid cu bitii lui A
        ; ...
        mov ax, [a]
        mov ecx, 0
        shr ax, 12
        or cx, ax
        mov ax, [b]
        shl ax, 3
        or cx, ax
        mov ax, [a]
        shl ax, 6
        or cx, ax
        mov ax, [a]
        cwde
        shl eax, 16
        or ecx, eax ;  :(
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
