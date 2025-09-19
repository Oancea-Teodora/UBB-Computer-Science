
map_coloring(N, M, AdjList, Solution) :-
    length(Solution, N),                 
    assign_colors(Solution, M),     
    valid_coloring(Solution, AdjList).     

assign_colors([], _).                   
assign_colors([Color | Rest], M) :-   
    my_between(1, M, Color),             
    assign_colors(Rest, M).          

my_between(Lower, Upper, Value) :-
    Lower =< Upper,              
    Value = Lower.              
my_between(Lower, Upper, Value) :-
    Lower < Upper,             
    Next is Lower + 1,          
    my_between(Next, Upper, Value). 

valid_coloring(_, []).
valid_coloring(Solution, [A-B|AdjList]) :-
    get_element_at(A, Solution, ColorA),    
    get_element_at(B, Solution, ColorB),   
    ColorA \= ColorB,                     
    valid_coloring(Solution, AdjList).   

get_element_at(1, [Element | _], Element).  
get_element_at(Index, [_ | Rest], Element) :-
    Index > 1,                              
    NextIndex is Index - 1,               
    get_element_at(NextIndex, Rest, Element). 

