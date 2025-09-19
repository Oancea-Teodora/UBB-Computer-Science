
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

maxOcc([],0).
maxOcc([H|T], NR):-
    nrOcc([H|T],H,NR),
    maxOcc(T, R),
    NR > R.
maxOcc([H|T], R):-
    nrOcc([H|T],H,NR),
    maxOcc(T, R),
    NR =< R.

remove([],[]).
remove([H|T], NewList) :-
    nrOcc([H|T],H,NR),
    maxOcc([H|T],NRE),
    NR == NRE,
    removeElem([H|T], H, NewList).
remove([H|T], [H|R]):-
    nrOcc([H|T],H,NR),
    maxOcc([H|T],NRE),
    NR \= NRE,
    remove(T, R).
