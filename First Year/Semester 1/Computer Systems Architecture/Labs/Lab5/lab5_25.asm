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
    S1 db '+', '4', '2', 'a', '8', '4', 'X', '5'
    s1l equ $-s1
    S2 db 'a', '4', '5'
    s2l equ $-s2
    res times s1l db 0
; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov ecx, s1l
        mov esi, s1
        mov edi, res
        l1:
            push ecx
            l2:
                mov ebx, [eax]
                cmp ebx, [esi]
                je continue
                mov [res], ebx
                inc edi
                continue:
                dec ecx
                jnz 12
            pop ecx
            inc esi
            dec ecx 
            jnz 12
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
