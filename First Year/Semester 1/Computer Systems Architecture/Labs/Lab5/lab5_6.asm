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
    s db 1, 2, 3, 4, 5, 6, 7, 8
    l equ ($-s)/2
    d times l db 0
    

; our code starts here
segment code use32 class=code
    start:
        ; A byte string S is given. Obtain the string D by concatenating the elements found on the even positions of S and then the elements found on the odd positions of S
        mov esi, 0
        mov edi, 0
        mov ecx, l
        jecxz end
        repeat:
            mov al, [s+esi]
            mov [d+edi], al
            add esi, 2
            inc edi
        loop repeat
        end:
        mov esi, 1
        mov edi, l
        mov ecx, l
        jecxz end2
        repeat2:
            mov al, [s+esi]
            mov [d+edi], al
            add esi, 2
            inc edi
        loop repeat2
        end2:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program