nrOcc([],_,0).
nrOcc([H|T],E,R):-
    H == E,
    nrOcc(T,E,R2),
    R is R2+1.
nrOcc([H|T],E,R):-
    H \=E,
    nrOcc(T,E,R).

isSet([]).
isSet([H|T]):-
    nrOcc([H|T], H, NR),
    NR =< 1,
    isSet(T).