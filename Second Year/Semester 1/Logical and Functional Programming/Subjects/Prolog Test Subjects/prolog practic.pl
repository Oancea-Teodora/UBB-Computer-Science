% define a predicate that merges two sorted lists of integers numbers and removes
% duplicates values
% [1,2,3,4,7], [2,5,6,8] -> [1,2,3,4,5,6,7,8]

% merge(l1,...,ln, p1,...,pm): [], n = 0 and m = 0
%							   [l1,...,ln], m = 0
%							   [p1,...,pm], n = 0
%							   l1 U merge(l2,...,ln, p2,...,pm), l1 = p1
%							   l1 U merge(l2,...,ln, p1,...,pm), l1 < p1
%							   p1 U merge(l1,...,ln, p2,...,pm), otherwise

% flow model: merge(L1, L2, R) -> (i, i, o).

merge([], [], []).
merge(L, [], L).
merge([], L, L).

merge([H|T1], [H|T2], R):-
    merge(T1, T2, R1),
    R = [H|R1].

merge([H1|T1], [H2|T2], R):-
    H1 < H2,
    merge(T1, [H2|T2], R1),
    R = [H1|R1].

merge([H1|T1], [H2|T2], R):-
    H1 > H2,
    merge([H1|T1], T2, R1),
    R = [H2|R1].
    
