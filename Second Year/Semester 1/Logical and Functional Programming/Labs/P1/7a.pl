
nrOcc([],_,0).
nrOcc([H|T], E, R):-
    H == E,
    nrOcc(T,E,R2),
    R is R2+1.
nrOcc([H|T], E, R):-
    H \= E,
    nrOcc(T,E,R).

intersection([],_,[]).
intersection([H|T],L,[H|R]):-
    nrOcc([H|T],H,NR),
    nrOcc(L,H,NR2),
    NR>0,
    NR2>0,
    intersection(T,L,R).
intersection([H|T],L,R):-
    nrOcc([H|T],H,NR),
    nrOcc(L,H,NR2),
    NR>0,
    NR2 == 0,
    intersection(T,L,R).
    