
remove([],_,_,[]).
remove([_|T],NR,N,R):-
    NR == N,
    NR2 is NR+1,
    remove(T,NR2,N,R).
remove([H|T],NR,N,[H|R]):-
    NR \= N,
    NR2 is NR+1,
    remove(T,NR2,N,R).