
sublist([],_,_,_,[]).
sublist([H|T],M,N,NR,[H|R]):-
    M =< NR,
    NR =< N,
    NR2 is NR+1,
    sublist(T,M,N,NR2,R).
sublist([_|T],M,N,NR,R):-
    M > NR,
    NR2 is NR+1,
    sublist(T,M,N,NR2,R).
sublist([_|T],M,N,NR,R):-
    NR > N,
    NR2 is NR+1,
    sublist(T,M,N,NR2,R).