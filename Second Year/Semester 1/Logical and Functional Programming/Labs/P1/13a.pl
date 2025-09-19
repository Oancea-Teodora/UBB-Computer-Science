to_set([], []).

to_set([H|T], R) :-
    to_set(T, R2),           
    member(H, R2),           
    R = R2.

to_set([H|T], [H|R]) :-
    to_set(T, R),               
    not(member(H, R)).         

