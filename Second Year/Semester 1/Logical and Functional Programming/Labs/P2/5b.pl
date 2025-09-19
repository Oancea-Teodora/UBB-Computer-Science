% Base case: An empty list produces an empty result.
replace([], _, []).

% If the head is a sublist, use `subst/4` to replace occurrences of the first element
% in the sublist with the replacement list, then process the rest of the list.
replace([[H|T]|Rest], Rep, [[NewH|NewT]|NewRest]) :-
    subst([H|T], H, Rep, [NewH|NewT]),
    replace(Rest, Rep, NewRest).

% If the head is not a sublist, keep it and process the tail.
replace([H|T], Rep, [H|NewT]) :-
    \+ is_list(H),
    replace(T, Rep, NewT).

% Base case for substitution: An empty list produces an empty result.
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
