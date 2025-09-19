
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

remove([],[]).
remove([H|T], R):-
    nrOcc([H|T],H,NR),
    NR > 1,
    removeElem([H|T], H, NewList),
    remove(NewList, R).
remove([H|T], [H|R]):-
    nrOcc([H|T],H,NR),
    NR =< 1,
    remove(T, R).
