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
    S db 1,2,3,4
    L equ $-S
    D times L-1 dw 0
; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov ecx, L-1
        mov ebx, 0
        repeta:
            mov al, [S+ebx]
            mul byte[S+ebx+1]
            mov [D+ebx], ax
            add ebx, 1
            loop repeta
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
