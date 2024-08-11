bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, scanf            
import exit msvcrt.dll     
import printf msvcrt.dll     ; indicating to the assembler that the printf fct can be found in the msvcrt.dll library
import scanf msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    s db "abcdabca"
    l equ $-s
    c db 0
    d db "%c", 0
    e db "%d"

; our code starts here
segment code use32 class=code
    start:
        ;A character string is given (defined in the data segment). Read one character from the keyboard, then count the number of occurences of that character in the given string and display the character along with its number of occurences.
        ; scanf("%c",c)
        push c
        push d
        call [scanf]
        pop eax
        pop eax
        ;add esp, 4*2
        mov esi, s
        mov bx, 0
        mov ecx,l
        lop:  ; esi->a  esi->b ...
            lodsb ; mov al, [esi]  add esi,1
            cmp al, [c]
            jne skip
            add bx, 1
            skip:
        loop lop ;ecx=ecx-1  ecx>0 -> loop
        
        ;printf ("the number is %d", c)
        push ebx 
        push e
        call [printf]
        add esp, 4*2
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
