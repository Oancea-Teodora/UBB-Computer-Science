bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, fopen, fclose, fread, fwrite          ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import fopen msvcrt.dll
import fclose msvcrt.dll
import fread msvcrt.dll
import fwrite msvcrt.dll 
; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    nume db 'nume1.txt',0
    sir db 'Am castraveti de culoare rosie 1, 2, 3 $', 0
    modscriere db 'w+', 0
    filedescriptor dd 0
    caracter db 0
; our code starts here
segment code use32 class=code
    start:
        ; ... Se dau un nume de fisier si un text (definite in segmentul de date). Textul contine litere mici, litere mari, cifre si caractere speciale. Sa se inlocuiasca toate CIFRELE din textul dat cu caracterul 'C'. Sa se creeze un fisier cu numele dat si sa se scrie textul obtinut prin inlocuire in fisier.
        push dword modscriere
        push dword nume
        call [fopen]
        mov [filedescriptor], eax
        add esp, 2*4
        mov esi, sir
        repeta:
            cmp byte [esi], 0
            je sfarsit
            lodsb
            cmp al, '0'
            jl skip
            cmp al, '9'
            jg skip
            mov al, 'C'
            skip:
            mov [caracter], al
            push dword [filedescriptor]
            push dword 1
            push dword 1
            push dword [caracter]
            call [fwrite]
            add esp, 4*4
        jmp repeta
        sfarsit:        
        
        push dword nume
        call [fclose]
        add esp, 1*4
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
