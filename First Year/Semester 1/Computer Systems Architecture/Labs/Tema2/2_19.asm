bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; a - byte, b - word, c - double word, d - qword - Interpretare cu semn
    a db 12
    b dw 23
    c dd -4
    d dq -12

; our code starts here
segment code use32 class=code
    start:
        ; (d+a)-(c-b)-(b-a)+(c+d)
        mov eax, 0
        mov ebx, 0
        mov ecx, 0
        mov edx, 0
        
        mov al, [a]
        cbw
        cwde
        cdq
        
        add eax, [d]
        adc edx, [d+4] ;edx:eax=d+a
        mov ecx, eax   ;edx:ecx=d+a
        clc
        
        mov ebx, 0
        mov eax, 0
        mov ax, [b]
        cwde
        mov ebx, [c]
        sub ebx, eax ; ebx = c-b
        mov eax, ebx ; eax = c-b
        mov ebx, 0
        mov ebx, edx ; ebx:ecx=d+a
        mov edx, 0
        cdq
        sub ecx, eax
        sbb ebx, edx ; ebx:ecx = (d+a)-(c-b)
        clc
        
        ; (d+a)-(c-b)-(b-a)+(c+d)
        
        mov eax, 0
        mov al,  [a]
        cbw 
        mov dx, [b]
        sub dx, ax
        mov ax, dx
        mov edx, 0
        cwde
        cdq
        sub ecx, eax
        sbb ebx, edx ; ebx:ecx = (d+a)-(c-b)-(b-a)
        clc
        
        add ecx, [d]
        adc ebx, [d+4]
        clc
        
        mov eax, [c]
        cdq
        add eax, ecx
        adc edx, ebx
        ; edx:eax = (d+a)-(c-b)-(b-a)+(c+d)
        
        
        
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
