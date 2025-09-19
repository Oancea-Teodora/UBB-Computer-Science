
remove([],_,[]).
remove([H|T], E, R):-
    H == E,
    remove(T, E, R).
remove([H|T], E, [H|R]):-
    H \= E,
    remove(T, E, R).

nrOcc([],_,0).
nrOcc([H|T],E,R):-
    H == E,
    nrOcc(T,E,R2),
    R is R2+1.
nrOcc([H|T],E,R):-
    H \= E,
    nrOcc(T,E,R).

numberatom([], []).
numberatom([H|T], [[H,NR]|R]):-
    nrOcc([H|T], H, NR),
    remove([H|T], H, NewList),
    numberatom(NewList, R).