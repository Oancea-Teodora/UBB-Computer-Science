
insert([],_,_,_,[]).
insert([H|T],E,NR,N,[H,E|R]):-
    NR == N,
    NR2 is NR+1,
    insert(T,E,NR2,N,R).
insert([H|T],E,NR,N,[H|R]):-
    NR \= N,
    NR2 is NR+1,
    insert(T,E,NR2,N,R).