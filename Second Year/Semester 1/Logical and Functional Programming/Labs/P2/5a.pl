% Base case: An empty list produces an empty result.
subst([], _, _, []).

% If the head of the list matches the element to replace,
% substitute it with the entire replacement list.
subst([H|T], E, Rep, R) :-
    H == E,
    subst(T, E, Rep, R2),
    append(Rep, R2, R).

% If the head of the list does not match the element to replace,
% keep the head and continue processing the tail.
subst([H|T], E, Rep, [H|R]) :-
    H \= E,
    subst(T, E, Rep, R).
