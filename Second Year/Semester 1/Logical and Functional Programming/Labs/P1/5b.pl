% Helper predicate to generate pairs with a given element
pairs(_, [], []).
pairs(E, [H|T], [[E, H] | R]) :-
    pairs(E, T, R).

% Main predicate to form all pairs in the list, without auxiliary functions
form_pairs([], []).
form_pairs([_], []).  % If there's only one element, no pairs can be formed
form_pairs([H|T], [R2 | R]) :-
    pairs(H, T, R2),  % Generate pairs for the head element
    form_pairs(T, R).         % Continue forming pairs with the tail
