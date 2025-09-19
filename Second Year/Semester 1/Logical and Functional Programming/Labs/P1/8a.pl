
even([]).
even([_,_|T]):-
    even(T).