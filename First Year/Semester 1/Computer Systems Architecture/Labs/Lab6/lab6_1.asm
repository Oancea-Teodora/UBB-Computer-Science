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
    a dd 127F5678h, 0ABCDABCDh
    len EQU ($-a)/4
    res times len dd 0
    
; our code starts here
segment code use32 class=code
    start:
        ; ...
;1. Se da un sir de dublucuvinte continand date impachetate (4 octeti scrisi ca un singur dublucuvant). Sa se obtina un nou sir de dublucuvinte, in care fiecare dublucuvant se va obtine dupa regula: suma octetilor de ordin impar va forma cuvantul de ordin impar, iar suma octetilor de ordin par va forma cuvantul de ordin par. Octetii se considera numere cu semn, astfel ca extensiile pe cuvant se vor realiza corespunzator aritmeticii cu semn.
        mov esi, a
        mov ecx, len
        repeta:
        mov edx, 0
        mov ebx, 0
        lodsw;ax=56 78
        mov bx, ax
        lodsw;ax= 12 7f
        mov dl, al
        add dl, bl ;d1=f7
        add bh, ah ;bh=68
        ; 00 68 00 7f        
        shl ebx, 8
        or edx, ebx
        stosd
        loop repeta
        
        
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
