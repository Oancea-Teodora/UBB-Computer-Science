bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ;a - byte, b - word, c - double word, d - qword - Interpretare cu semn
    a db 2
    b dw -3
    c dd 1
    d dq -4

; our code starts here
segment code use32 class=code
    start:
        ; (a-b)+(c-b-d)+d
        mov eax, 0
        mov ebx, 0
        mov ecx, 0
        mov edx, 0
        
        mov al, [a]
        cbw 
        sub ax, [b]
        
        cwde
        add eax, [c]
        sub eax, [b]
        
        clc
        cdq ; eax:edx=(a-b)+(c-b)
        sub eax, [d+4]
        sbb edx, [d] ; eax:edx=(a-b)+(c-b-d)
        
        clc
        add eax, [d+4]
        adc edx, [d]
        
        mov [d], eax
        mov [d+4], edx; d=(a-b)+(c-b-d)+d
        
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
