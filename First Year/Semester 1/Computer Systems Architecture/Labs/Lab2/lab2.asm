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
    b db 1
    a db 2
    c db 3
    d dw 4
; our code starts here
segment code use32 class=code
    start:
        ; ... 
        ; (100*a+d+5-75*b)/(c-5)
        mov eax, 0
        mov ecx, 0
        mov edx, 0
        mov ebx, 0
        mov esi, 0
        mov edi, 0
        
        mov al, [a]
        mov bl, 100
        mul bl
        
        add ax, [d]
        add ax, 5
        push eax
        
        mov al, [b]
        mov bl, 75
        mul bl
        mov bx, ax
        pop eax
        sub ax, bx
        push eax
        
        mov eax, 0
        mov al, [c]
        sub ax, 5
        mov bx, ax
        pop eax 
        
        div ebx
        
        
        
       
        
        
        
        
        
        
        
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
