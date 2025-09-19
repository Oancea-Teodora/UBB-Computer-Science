nrOcc([],_,0).
nrOcc([H|T],E,R):-
    H == E,
    nrOcc(T,E,R2),
    R is R2+1.
nrOcc([H|T],E,R):-
    H \=E,
    nrOcc(T,E,R).

removeElem([],_,_,[]).
removeElem(L,_,3,L).
removeElem([H|T],E,NR,R):-
    NR =< 3,
    H == E,
    NR2 is NR + 1,
    removeElem(T,E,NR2,R).  
removeElem([H|T],E,NR,[H|R]):-
    NR =< 3,
    H \= E,
    removeElem(T,E,NR,R).
   