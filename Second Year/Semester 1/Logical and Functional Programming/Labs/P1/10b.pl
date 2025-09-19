
sum([],_,0).
sum([H|T],0,R):-
    sum(T,1,R2),
    R is R2+H.
sum([H|T],1,R):-
    sum(T,0,R2),
    R is R2-H.