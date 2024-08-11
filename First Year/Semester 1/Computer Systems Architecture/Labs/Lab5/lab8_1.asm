bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, scanf  ; adaugam printf si scanf ca functii externe           
import exit msvcrt.dll     
import printf msvcrt.dll     ; indicam asamblorului ca functia printf se gaseste in libraria msvcrt.dll
import scanf msvcrt.dll 
    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a dd 0
    b dd 0
    f db "%d", 0

; our code starts here
segment code use32 class=code
    start:
        ; Read two numbers a and b (in base 10) from the keyboard and calculate their product. This value will be stored in a variable called "result" (defined in the data segment).
    
        ; scanf(f, a)
        push dword a
        push dword f
        call [scanf]
        add esp, 2*4
        push dword b
        push dword f
        call [scanf]
        add esp, 2*4
        mov eax, [a]
        mov ebx, [b]
        add eax, ebx
        push dword eax
        push dword f
        call [printf]
        add eax, 2*4
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
