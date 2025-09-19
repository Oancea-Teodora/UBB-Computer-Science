% Prolog Lab Exam

% allowed: is_list, atomic, number
% Define a predicate that merges two sorted lists of integers and removes duplicate values
% [1 2 3 5 7] , [2 4 6] - > [1 2 3 4 5 6 7]

% Mathematical Model
%   merge_lists(A(1,..,n),B(1,..n), Result) - A = [] & B = []       => Result = []
%                                           - A = [] & B nonempty   => Result = B
%                                           - A nonempty & B = []   => Result = A
%                                           - A1 < B1 => Result + A1
%                                           - B1 < A1 => Result + B1
%                                           - A1 = B1 => Result + A1 (only adds the value once)


% merge_lists(+List1, +List2, Result)
% flow_model: (i,i,o)
merge_lists([], [], []).
merge_lists([], List, List).
merge_lists(List, [], List).
merge_lists([FirstH|FirstT], [SecondH|SecondT], Result) :-
    FirstH < SecondH,
    merge_lists(FirstT, [SecondH|SecondT], TemporaryResult),
    Result = [FirstH|TemporaryResult].
merge_lists([FirstH|FirstT], [SecondH|SecondT], Result) :-
    FirstH > SecondH,
    merge_lists([FirstH|FirstT], SecondT, TemporaryResult),
    Result = [SecondH|TemporaryResult].
merge_lists([FirstH|FirstT], [SecondH|SecondT], Result) :-
    FirstH = SecondH,
    merge_lists(FirstT, SecondT, TemporaryResult),
    Result = [FirstH|TemporaryResult].
