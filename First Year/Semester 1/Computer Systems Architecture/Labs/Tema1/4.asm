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
    a db 2
    b db 3
    c db 2
    f dw 1
    g dw 3
    h dw 4
    e dw 2
    
; our code starts here
segment code use32 class=code
    start:
        ; (e + g) * 2 / (a * c) + (h – f) + b * 3
        mov eax, 0
        mov ebx, 0
        mov ecx, 0
        mov edx, 0
        
        mov ax, [e]
        add ax, [g]
        mov bl, 2
        mul bl
        
        push eax
        mov eax, 0
        mov al, [a]
        mov bl, [c]
        mul bl
        
        mov ebx, eax
        pop eax 
        div ebx
        
        add ax, [h]
        sub ax, [f]
        
        push eax
        mov eax, 0
        mov al, [b]
        mov bl, 3
        mul bl
        mov ebx, eax
        
        pop eax
        add eax, ebx
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
