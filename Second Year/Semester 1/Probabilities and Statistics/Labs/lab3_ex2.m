% 2. Approximations of the Binomial distribution

% -> Normal approximation of the Binomial distribution:
% For moderate values of p (0.05 ≤ p ≤ 0.95) and large values of n (n → ∞)
% Bino (n, p) ≈ Norm (mu = np, sigma = sqrt(np * (1 - p)) )
% Write a Matlab code to visualize how the Binomial distribution gradually takes
% the shape of the Normal distribution as n → ∞.

p = input("Give the value of p between 0.05 and 0.95: ");

for (n = 1:5:150)
    k = 0:n;
    px = binopdf(k, n, p);
    plot(k, px, "r*");
    pause(0.05);
endfor

clf;
% Poisson approximation of the Binomial distribution: If n ≥ 30 and p ≤ 0.05,
% then Bino (n, p) = Poisson (λ = np)
% Compare graphically the two pdf’s.

n = input("Give the value of n, >= 30: ");
p = input("Give the value of p, <= 0.05: ");
k = 0:n;
px = binopdf(k, n, p);
py = poisspdf(k, n*p);
plot(k, px, "r*");
hold on;
plot(k, py, "b-");


