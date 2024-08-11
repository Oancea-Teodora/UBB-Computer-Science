bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; (d+d)-(a+a)-(b+b)-(c+c)
    ;a - byte, b - word, c - double word, d - qword - Unsigned representation
    a db 123
    b dw 250
    c dd 300
    d dq 400
    x dd 0
; our code starts here
segment code use32 class=code
    start:
        mov eax, 0
        mov ebx, 0
        mov ecx, 0
        mov edx, 0
        
        mov cx ,[b]
        add cx, cx
                
        mov bl,[a]
        add bl, bl
        mov bh, 0         
       
        sub bx, cx
        mov dx, 0
        mov edx, [c]
        add edx, edx
        sub ebx, edx
        mov eax, ebx
        mov ebx, [d] 
        mov ecx, [d+4] 
        add ebx, [d]
        adc ecx, [d+4]    
        sub eax, ebx
        sbb edx, ecx           
                              
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program