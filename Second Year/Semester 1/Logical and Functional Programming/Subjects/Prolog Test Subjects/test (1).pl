len([],0).
len([_|T],R):-
    len(T,R1),
    R is R1+1.

%merge(i,i,o)
merge([],[],[]):-!.
merge(A,[],A):-!.
merge([],B,B):-!.
merge([A|TA], [B|TB], [A|R]):-
    A<B,
    !,
    merge(TA,[B|TB],R).
merge([A|TA],[B|TB],[B|R]):-
    merge([A|TA],TB,R).

%merge_sort(i,o)
merge_sort([],[]):-!.
merge_sort([A],[A]):-!.
merge_sort(L,R):-
    split(L,Left,Right),
    merge_sort(Left, LR),
    merge_sort(Right, RR),
    merge(LR,RR,R).

%splitAux(i,i,o,o)
splitAux([],_,_,[],[]):-!.
splitAux([H|T], N, I, [H|Left], Right):-
    I<N,
    !,
    I1 is I+1,
    splitAux(T,N,I1, Left,Right).
splitAux([H|T],N,I,Left,[H|Right]):-
    I1 is I+1,
    splitAux(T,N,I1, Left, Right).

%my_sort(i,o)
my_sort([],[]):-!.
my_sort(L,R):-
    merge_sort(L,R).

%split(i,o,o)
split([],[],[]).
split(L, Left, Right):-
    len(L,N),
    N1 is div(N,2),
    splitAux(L,N1,0,Left,Right).








