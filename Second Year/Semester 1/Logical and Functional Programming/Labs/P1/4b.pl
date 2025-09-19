
add([],[]).
add([H|T],[H,1|R]):-
    0 is H mod 2,
    add(T, R).
add([H|T],[H|R]):-
    1 is H mod 2,
    add(T, R).
    
    