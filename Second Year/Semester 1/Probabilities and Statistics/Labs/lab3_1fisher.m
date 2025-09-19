% 1. Let X have the following distribution: X ∈ F(n1, n2) - Fisher. Compute the following:
% Fisher - continuous distribution => > ~ >= and < ~ <=
n1 = input("Give the first value of degrees of freedom: ");
n2 = input("Give the second value of degrees of freedom: ");
alpha = input("Give the value of alpha, between 0 and 1: ");
beta = input("Give the value of beta, between 0 and 1: ");

% a) P(X <= 0) and P (X >= 0)
printf("a)\n");
pna1 = fcdf(0, n1, n2); % P(X <= 0)
pna2 = 1 - pna1; % P(X >= 0) = 1 - P(X <= 0)
printf("P(X <= 0) = %1.6f \n", pna1);
printf("P(X >= 0) = %1.6f \n", pna2);

% b) P(-1 <= X <= 1) and P(X <= -1 or X >= 1)
printf("b)\n");
pnb1 = fcdf(1, n1, n2) - fcdf(-1, n1, n2); % P(X <= 1) - P(X < -1)
pnb2 = 1 - pnb1; % everything BUT -1 <= X <= 1, so it's 1 - P(-1 <= X <= 1)
printf("P(-1 <= X <= 1) = %1.6f \n", pnb1);
printf("P(X <= -1 or X >= 1) = %1.6f \n", pnb2);

% c) the value xα such that P(X < xα) = P(X <= xα) = α, α ∈ (0, 1)
% ~ the inverse of the CDF (inv), we don't compute the probability, but the quantile for a given prob.
% ~ xα is the quantile of order α
printf("c)\n");
pnc = finv(alpha, n1, n2);
printf("Value of xa = %1.6f \n", pnc);

% d) the value xβ such that P(X > xβ) = P(X >= xβ) = β, β ∈ (0, 1)
% ~ xβ is the quantile of order 1 - β
printf("d)\n");
pnd = finv(1 - beta, n1, n2);
printf("Value of xb = %1.6f \n", pnd);



