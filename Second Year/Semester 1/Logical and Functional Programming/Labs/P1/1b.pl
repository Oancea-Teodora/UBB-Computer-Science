
is_power_of_two(1).
is_power_of_two(N):-
    N>1,
    0 is N mod 2,
    N2 is N//2,
    is_power_of_two(N2).

add([],_,_,[]).
add([H|T], E, NR, [H,E|Res]):-
    is_power_of_two(NR),
    NR2 is NR+1,
    add(T, E, NR2, Res).
add([H|T], E, NR, [H|Res]):-
    not(is_power_of_two(NR)),
    NR2 is NR+1,
    add(T, E, NR2, Res).