
gcd(A, 0, A).
gcd(A, B, R):-
    M is A mod B,
    gcd(B, M, R).

lcm_two(A,B, R):-
    gcd(A, B, R2),
    R is (A*B)//R2.

lcm([H], H).
lcm([H|T], R):-
    lcm(T, R2),
	lcm_two(H, R2, R).