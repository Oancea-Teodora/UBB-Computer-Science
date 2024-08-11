bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, scanf            
import exit msvcrt.dll     
import printf msvcrt.dll     ; indicating to the assembler that the printf fct can be found in the msvcrt.dll library
import scanf msvcrt.dll                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import fclose msvcrt.dll  
import fopen msvcrt.dll  
import fread msvcrt.dll  

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; a dd 7
    ; push a (scanf)
    ; push [a] (printf)
    nume db 'fisier', 0
    w db 'w', 0
    s db 'salut', 0
; our code starts here
segment code use32 class=code
    start:
        ; ...
        ;FILE *fopen(const char * nume, const char * w)
        push w
        push nume
        call [fopen]
        add esp, 2*4
        mov ebx, eax
        ;fprint(eax, "%s %d %c", "string", -2, 'a');
        push s
        push eax
        call [printf]
        add esp, 2*4        
        
        ;int fclose(file *eax)
        push eax
        call [fclose]
        add esp, 1*4
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
