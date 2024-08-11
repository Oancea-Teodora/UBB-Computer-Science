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
    a db 32
    b dw 56
    c dd 0
; our code starts here
segment code use32 class=code
    start:
        ; Se da octetul A si cuvantul B. Sa se formeze dublucuvantul C:
;bitii 24-31 ai lui C sunt bitii lui A
;bitii 16-23 ai lui C sunt inversul bitilor din octetul cel mai putin semnificativ al lui B
;bitii 10-15 ai lui C sunt 1
;bitii 2-9 ai lui C sunt bitii din octetul cel mai semnificativ al lui B
;bitii 0-1 se completeaza cu valoarea bitului de semn al lui A
        
        mov eax, 0
        mov ebx, 0
        mov ecx, 0
        mov edx, 0
        
        mov al, [a]
        shl eax, 24
        or ecx, eax
        
        mov eax, 0
        mov ax, [b]
        shl ax, 16
        not ax
        mov ah, 0
        shl eax, 16
        or ecx, eax
        
        or ecx, 0b1111110000000000
        mov eax, 0
        mov ax, [b]
        mov al, 0
        shr ax, 6
        or ecx, eax
        
        mov eax, 0
        mov al, [a]
        shr al, 7
        or ecx, eax
        shl al, 1
        or ecx, eax
        
        
        
        
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
