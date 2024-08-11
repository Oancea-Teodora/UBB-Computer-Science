bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, scanf               ; tell nasm that exit exists even if we won't be   defining it
import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll

extern start2
; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    
    format db "%s", 0
    format2 db "%s", 0
    sir1 resb 100
    sir2 resb 100
    sir3 resb 100

; our code starts here
segment code use32 class=code
    start:
        ; ...
        push sir1
        push format
        call [scanf]
        add esp, 4*2
        
        push sir2
        push format
        call [scanf]
        add esp, 4*2
        
        push sir3
        push format
        call [scanf]
        add esp, 4*2
        
        push sir3
        push sir2
        push sir1
        
        call start2
        
        push eax
        push format2
        call [printf]
        add esp, 4*2
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program