
nrOcc([],_,0).
nrOcc([H|T], E, R):-
    H == E,
    nrOcc(T, E, R2),
    R is R2 + 1.
nrOcc([H|T], E, R):-
    H \= E,
    nrOcc(T, E, R).

removeElem([],_,[]).
removeElem([H|T], E, [H|R]):-
    H \= E,
    removeElem(T,E,R).
removeElem([H|T], E, R):-
    H == E,
    removeElem(T,E,R).   

difference(L,[],L).
difference(L,[H2|T2],R):-
    nrOcc(L,H2,NR),
    NR > 0,
    removeElem(L,H2,NewList),
    difference(NewList,T2,R).
difference(L,[H2|T2],R):-
    nrOcc(L,H2,NR),
    NR =:= 0,
    difference(L,T2,R).
    