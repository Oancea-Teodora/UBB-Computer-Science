% 2. Using a U(0,1) (standard Uniform) random number generator, generate the common
% discrete probability distributions:

% a) Bernoulli Distribution Bern(p), with parameter p ∈ (0, 1)
% X ( 0  1 )
%   (1-p p )

p = input("Give a probabilty: ");
N = input("Give a number of simulations: ");
U = rand(1, N); % 1st arg: no. of trials, 2nd arg: no. of simulations
X = sum(U < p); % no. of successes in each simulation
k = 0:1;
pk = binopdf(k, 1, p);
U_X = [0 1];
n_X = hist(X, length(U_X)); % 2nd arg: no.of bins
rel_freq = n_X/N;
plot(U_X, rel_freq, "r*", k, pk, "ro");
legend("sin", "bino");


% b) Binomial Distribution Bino(p), with parameters n ∈ IN, p ∈ (0, 1)
% X (         k          )
%   (Cnk * p^k * q^(n−k) )  -> the sum of n independent Bern(p) variables

p = input("Give a probabilty: ");
n = input("Give a no. of trials: ");
N = input("Give a number of simulations: ");
U = rand(n, N);
X = sum(U < p);
k = 0:n;
pk = binopdf(k, n, p);
U_X = unique(X);
n_X = hist(X, length(U_X));
rel_freq = n_X/N;
clf;
plot(U_X, rel_freq, "b*", k, pk, "ro");
legend("sin", "bino");

% c) Geometric Distribution Geo(p), with parameter p ∈ (0,1)
% X (   k   )
%   ( p*q^k ) -> the no. of Bernoulli trials failures needed to get the first success

p = input("Give a probabilty: ");
N = input("Give a number of simulations: ");
X = zeros(1,N);

for i = 1:N
    X(i) = 0;
    while rand >= p
        X(i) = X(i) + 1;
    endwhile
endfor

k = 0:20; % 20 is an arbitrary number, since the no. of trials is infinite
pk = geopdf(k, p);
U_X = unique(X);
n_X = hist(X, length(U_X));
rel_freq = n_X/N;

plot(U_X, rel_freq, "b*", k, pk, "ro");
legend("sin", "geo");






