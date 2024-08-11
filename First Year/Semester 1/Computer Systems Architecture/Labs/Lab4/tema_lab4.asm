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
    a dw 10101101b
    b dw 10001101b
    c dd 0
; our code starts here
segment code use32 class=code
    start:
        ;Given the words A and B, compute the doubleword C as follows:
;the bits 0-5 of C are the same as the bits 3-8 of A
;the bits 6-8 of C are the same as the bits 2-4 of B
;the bits 9-15 of C are the same as the bits 6-12 of A
;the bits 16-31 of C have the value 0

    mov eax, [c]
    
    ;the bits 0-5 of C are the same as the bits 3-8 of A
    mov bx, [a]
    shr bx, 3
    and bx, 00011111b
    or ax, bx
    
    ;the bits 6-8 of C are the same as the bits 2-4 of B
    mov bx, [b]
    shl bx, 4
    and bx, 111000000b
    or ax, bx
    
    ;the bits 9-15 of C are the same as the bits 6-12 of A
    mov bx, [a]
    shl bx, 3
    and bx, 1111111000000000b
    or ax, bx
    
    ;the bits 16-31 of C have the value 0
    and eax, 11111111111111110000000000000000b
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
