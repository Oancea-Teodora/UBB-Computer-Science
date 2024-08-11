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
    a db 12
    b db 3
    c db 5
    e dd -34
    x dq -91
    
; our code starts here
segment code use32 class=code
    start:
        ; (a+a+b*c*100+x)/(a+10)+e*a; a,b,c-byte; e-doubleword; x-qword
        mov eax, 0
        mov ebx, 0
        mov ecx, 0
        mov edx, 0
       
        mov al, [a]
        cbw
        add ax, ax
        mov bx, ax
        mov al, [b]
        mov cl, [c]
        imul cl
        mov cl, 100
        imul cl 
        add ax, bx        
        
        push dx
        push ax
        pop eax
        cdq
        
        clc
        add eax, [x]
        adc edx, [x+4] ; edx:eax = (a+a+b*c*100+x)
        mov ecx, eax   ; edx:ecx = (a+a+b*c*100+x)
        push edx
        mov edx, 0

        mov eax, 0
        mov al, [a]
        add al, 10
        cbw
        cwde
        mov ebx, eax
        mov eax, ecx
        idiv ebx ; eax = (a+a+b*c*100+x)/(a+10)
        
        
        mov ebx, eax
        mov al, [a]
        cbw
        cwde
        mov ecx, [e]
        pop edx
        imul ecx ; edx:eax=e*a
        
        clc
        add eax, ebx
        adc edx, 0
     
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
