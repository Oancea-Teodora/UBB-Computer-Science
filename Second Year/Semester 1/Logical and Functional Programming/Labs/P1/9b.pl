
gcd_two(A,0,A).
gcd_two(A,B,R):-
    M is A mod B,
    gcd_two(B,M,R).

gcd([],[]).
gcd([A],A).
gcd([A,B],R):-
    gcd_two(A,B,R).
gcd([H|T],R2):-
    gcd(T,R),
    gcd_two(H,R,R2).
    