% 1. For the given matrices A and B print the matrices C = A-B, D = A*B and E, where Eij = Aij * Bij
A = [1 0 -2; 2 1 3; 0 1 0];
B = [2 1 1; 1 0 -1; 1 1 0];
C = A - B;
D = A * B;
E = A.*B;
disp(C); disp("\n");
disp(D); disp("\n");
disp(E); disp("\n");

% 2. For x in [0, 3] graph on the same axes the functions (x^5)/10, x*sin(x) and cos(x) in different
% colors and linestyles. Display a title and a legend on your graph. Then plot them on different
% pictures, but in the same window.
x = 0:0.01:3;
plot(x, (x.^5)/10, "-b;x^5/10;", x, x.*sin(x), "--g;x*sin(x);", x, cos(x), ":r;cos(x);")
title("Graph")
xlabel("x")
ylabel("sin(x)")
