bits 32

global start
extern exit
import exit msvcrt.dll

segment data use32 class=data
    a db 5
    b db 3
    c db 2
    e dd 1000000000  ; 
    x dq 9999999999999999h  ; 
;(a+a+b*c*100+x)/(a+10)+e*a; a,b,c-byte; e-doubleword; x-qword
segment code use32 class=code
start:
    mov al, [a] 
    cbw
    cwde  
    add eax, eax         

    mov bl, [b]  
    cbw
    cwde
    
    mov cl, [c]  
    cbw
    cwde
    imul ebx
    imul ecx
    mov ebx, 0
    mov ebx, 100
    imul ebx    

    add eax, ebx         

    mov edx, 0
    mov ebx, 0
    mov ecx, 0

    mov eax, [x]          
    mov ebx, [a]         
    add ebx, 10           

    idiv ebx             
    add eax, [e]          

    push    eax           
    call    [exit]