%Given a numerical linear list consisting of integers, 
%write a predicate to compute the lowest common multiple 
%of all elements from the list.

%greater(X Integer, Y Integer, R Integer)
%(i,i,o)

greater(0,0,0).
greater(X,Y,X):-
	X > Y.
greater(X,Y,Y):-
	X =< Y.

%lcm(X Integer, Y Integer, Greater G, R Result-Integer)
%lcm(i,i,i,o)

lcm(0,0,0,0).
lcm(X,Y,Greater,Greater):-
	0 is mod(Greater,X),
	0 is mod(Greater,Y),!.
lcm(X,Y,Greater,R):-
	G1 is Greater + 1,
	lcm(X,Y,G1,R).
	
%lcmL(List List, R Result-List)
%(i,o)

lcmL([H1,H2|T],Z):-
	greater(H1,H2,O),	
	lcm(H1,H2,O,X),
	lcmL([X|T],Z).
lcmL([H1,H2],Z):-
	greater(H1,H2,O),
	lcm(H1,H2,O,Z).
