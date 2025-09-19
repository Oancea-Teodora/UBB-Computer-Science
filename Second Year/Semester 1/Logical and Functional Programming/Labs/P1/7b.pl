
list(N,N,N).
list(M,N,[M|R]):-
    MM is M+1,
    list(MM,N,R).