bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start2      

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    
    sir1 resb 100
    sir2 resb 100
    sir3 resb 100
    ; ...

; our code starts here
segment code use32 class=code
    start2: 
        ; ...
        mov esi, [esp+4]
    
        get_to_end_loop:
            lodsb
            cmp al, 0
            jz exit_first_loop
        jmp get_to_end_loop
        exit_first_loop:
        
        dec esi
        
        mov edi, esi
        mov esi, [esp+8]
        
        concatenate_sir2_loop:
            lodsb
            
            cmp al, 0
            jz exit_sir2_loop
            
            stosb
        jmp concatenate_sir2_loop
        exit_sir2_loop:
        
        mov esi, [esp+12]
        
        concatenate_sir3_loop:
            lodsb
            
            cmp al, 0
            jz exit_sir3_loop
            
            stosb
        jmp concatenate_sir3_loop
        exit_sir3_loop:
        mov edi, 0
        mov eax, [esp+4]
        
        ret 12