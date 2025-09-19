% Șorecău Adrian-Vasile
% group 927/1
% extracted problem: 18

% mathematical model

% merge_lists(l1, ..., ln, k1, ..., km) =   | k1, ..., km, n == 0
%											| l1, ..., ln, m == 0
%											| k1 U merge_lists(l1, ..., ln, k2, ..., km), k1 <= l1
%											| l1 U merge_lists(l2, ..., ln, k1, ..., km), k1 > l1

% merge_lists(FirstList: list, SecondList: list, Result: list)
% (i, i, o)
merge_lists([], SecondList, SecondList):- !.
merge_lists(FirstList, [], FirstList):- !.
merge_lists([X|FirstList], [Y|SecondList], [X|Result]):- X =< Y, !, merge_lists(FirstList, [Y|SecondList], Result).
merge_lists([X|FirstList], [Y|SecondList], [Y|Result]):- X > Y, merge_lists([X|FirstList], SecondList, Result).








