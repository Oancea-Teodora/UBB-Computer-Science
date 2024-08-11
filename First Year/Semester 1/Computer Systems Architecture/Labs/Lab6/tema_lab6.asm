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
    s dd 12345607h, 1A2B3C15h
    len equ ($-s)/4
    d times len*4 db 0

; our code starts here
segment code use32 class=code
    start:
        ; Se da un sir S de dublucuvinte.
;Sa se obtina sirul D format din octetii dublucuvintelor din sirul D sortati in ordine crescatoare in interpretarea fara semn.
        mov esi, s 
        mov edi, d
        mov ecx, len*4
        jecxz sari1
        repeta1:
            lodsb
            stosb
        loop repeta1
        sari1:
        
        mov ecx, 0
        repeta2:
            mov edx, ecx
            repeta3:
                inc ecx
                mov bl, [d+ecx]
                cmp [d+edx], bl
                jl maimare
                    xchg bl, [d+edx]
                    mov [d+ecx], bl
                maimare:
            cmp ecx, len*4
            jl repeta3
        mov ecx, edx
        inc ecx
        cmp ecx, len*4
        jl repeta2
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
