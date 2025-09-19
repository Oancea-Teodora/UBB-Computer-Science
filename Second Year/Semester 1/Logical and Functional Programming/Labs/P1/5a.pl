nrOcc([],_,0).
nrOcc([H|T],E,N):-
    H=E,
    nrOcc(T,E,N1),
    N is N1+1.
nrOcc([H|T],E,N):-
    H\=E,
    nrOcc(T,E,N).

union(L1,[],L1).
union(L1,[H|T],R):-
    nrOcc(L1,H,0),
    union([H|L1],T,R).
union(L1,[H|T],R):-
    nrOcc(L1,H,0)\=0 ,
    union(L1,T,R).
