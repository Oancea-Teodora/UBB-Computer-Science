% 2. Plot the graphs of the pdf and the cdf of a random variable X having a Binomial Distribution
% of parameters n and p (given by the user).

clf; % clears the figure
n = input("Give no. of trials n = "); % n - natural integer
p = input("Give prob. of success p = "); % real number in [0, 1]
x = 0:1:n; % x - no. of successses
px = binopdf(x, n, p);
plot(x, px, 'r*');
hold on; % it keeps the previous graph
xx = 0:0.01:n; % simulation of continuity
cx = binopdf(xx, n, p);
plot(xx, cx, 'b');
legend('pdf', 'cdf');

% Application: A coin is tossed 3 times. Let X denote the number of heads that appear.
% a) Find the probability distribution function of X. What type of distribution does X have?
% X has a Binomial Distribution: Bino (3, 0.5)
% (0, 1, 2, 3)
% (1/8, 3/8, 3/8, 1/8)  -> these two vectors represent the PDF
% binopdf ([0,1,2,3], 3, 0.5) -> the parameters: no.of successses, trials, probability

% b) Find the cumulative distribution function of X. (denoted Fx)
% binocdf([0,1,2,3], 3, 0.5)

% c) Find P(X = 0) and P(X != 1)
p1 = binopdf(0, 3, 0.5); % P(X = 0)
p2 = 1 - binopdf(1, 3, 0.5); % P(X != 1) = 1 - P(X = 1)
printf("P(X = 0) = %1.6f \n", p1);
printf("P(X != 1) = %1.6f\n", p2);

% d) Find P(X <= 2) and P (X < 2)
p3 = binocdf(2, 3, 0.5); % P(X <= 2)
p4 = binocdf(1, 3, 0.5); % P(X < 2) = P(X <= 1)
printf("P(X <= 2) = %1.6f \n", p3);
printf("P(X < 2) = %1.6f\n", p4);

% e) Find P (X >= 1) and P(X > 1)
p5 = 1 - binocdf(0, 3, 0.5); % P (X >= 1) = 1 - P (X <= 0)
p6 = 1 - binocdf(1, 3, 0.5); % P(X > 1) = 1 - P(X <= 1)
printf("P(X >= 1) = %1.6f\n", p5);
printf("P(X > 1) = %1.6f\n", p6);

% f) Write a Matlab code that simulates 3 coin tosses and computes the value of the variable X
N = input("Give no. of simulations N = ");
U = rand(3, N); % matrix with random elements in [0, 1]
Y = (U < 0.5);
X = sum(Y); % sum the elements on the columns
clf;
hist(X);






