bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, scanf           ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import printf msvcrt.dll
import scanf msvcrt.dll

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    a dd 0
    b dd 0
    f dd '%d',0
    rezultat dw 0
; our code starts here
segment code use32 class=code
    start:
        ;Sa se citeasca de la tastatura doua numere a si b (in baza 10) si sa se calculeze: (a+b) / (a-b). Catul impartirii se va salva in memorie in variabila "rezultat" (definita in segmentul de date). Valorile se considera cu semn.
        push dword a
        push dword f 
        call [scanf]
        add esp, 2*4
        
        push dword b
        push dword f 
        call [scanf]
        add esp, 2*4
        
        mov eax, [a]
        mov ebx, [b]
        add eax, ebx
        
        mov ecx, [a]
        sub ecx, ebx
        
        mov edx, 0
        idiv ecx
        mov [rezultat], ax
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
