
subst([],_,_,[]).
subst([H|T],E,N,[H|R]):-
    H \= E,
    subst(T,E,N,R).
subst([H|T],E,N,[N|R]):-
    H == E,
    subst(T,E,N,R).