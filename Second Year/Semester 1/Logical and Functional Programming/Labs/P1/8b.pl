
min([A],A).
min([H|T],R):-
    min(T, R),
    H > R.
min([H|T],H):-
    min(T, R),
    H =< R.

removeElem([],_,1,[]).
removeElem([H|T],E,NRR,R):-
    E == H,
    NRR == 0,
    NR2 is NRR+1,
    removeElem(T,E,NR2,R).
removeElem([H|T],E,NRR,[H|R]):-
    E \= H,
    removeElem(T,E,NRR,R).
removeElem([H|T],E,NRR,[H|R]):-
    E == H,
    NRR >0,
    removeElem(T,E,NRR,R).

remove(L,R):-
    min(L,NR),
    removeElem(L,NR,0,R).