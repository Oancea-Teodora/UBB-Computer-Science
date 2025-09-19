valley([], 1).
valley([_], 1).
valley([H1, H2 | T], 0) :-
    H1 > H2,
    valley([H2 | T], 0).
valley([H1, H2 | T], 0) :-
    H1 < H2,
    valley([H2 | T], 1).
valley([H1, H2 | T], 1) :-
    H1 < H2,
    valley([H2 | T], 1).
