clear
clc

X1 = [4.6, 0.7, 4.2, 1.9, 4.8, 6.1, 4.7, 5.5, 5.4]; % steel
X2 = [2.5, 1.3, 2.0, 1.8, 2.7, 3.2, 3.0, 3.5, 3.4]; % glass

% we compute the lengths of the vectors
n1 = length(X1);
n2 = length(X2);

% we compute the means of the vectors
m1 = mean(X1);
m2 = mean(X2);

% we compute the variances of the vectors
v1 = var(X1);
v2 = var(X2);

% a)

% H0 = population variances are equal
% H1 = population variances differ
% because we must test if the variances differ and we have 2 populations => we
% will use "vartest2", a two-tailed test
fprintf('a)\n');
fprintf("The null hypothesis H0: population variances are equal\n");
fprintf("The alternative hypothesis H1: population variances differ\n");
fprintf('We will perform a two-tailed test using vartest2\n');

alpha = input("Please input alpha (the significance level): ");

% we perform the variances test for 2 populations
[H, P, CI, STATS] = vartest2(X1, X2, alpha, 0);

% if h is 0, the null hypothesis is not rejected and if it's 1 it is
% rejected
if H == 0
    fprintf("\nThe null hypothesis is not rejected!\n");
    fprintf("The population variances are equal!\n");
else
    fprintf("\nThe null hypothesis is rejected!\n");
    fprintf("The population variances seem to differ!\n");
end

% here we compute the fischer inverse distribution and we will use it for 
% the rejection region
f1 = finv(alpha/2, n1-1, n2-1);
f2 = finv(1-alpha/2, n1-1, n2-1);

fprintf("The value of the variances test vartest2 is %.4f\n", STATS.fstat);
fprintf("The rejection region of the variances test is (%.4f, %.4f) U (%.4f, %.4f)\n", -inf, f1, f2, inf);
fprintf("The P-value of the variances test is %.4f\n",P);
fprintf("The value of H of the variances test is %.4f", H);

% b)
% we will use the formulas from 3. from the conf int file, the one where
% sigmas are equal, and the one where sigmas are different
% actually because we must compute the 95% confidence interval, the alpha
% will be still 0.05 as in point a
fprintf("\nb)\n");
fprintf('We have to find a 95 confidence interval for the difference of 2 population means\n')
confidence = input('Please input the confidence interval: ');
alpha = 1 - confidence;
if H == 0
    fprintf('The sigmas are equal!\n');
    % if H=0 (from point a) it means that sigmas are equal
    % we will use the formula from the file to compute the confidence interval
    n = n1+n2-2;
    % we compute the student inverse distribution
    t = tinv(1-alpha/2, n);
    sp = sqrt(((n1-1)*v1 + (n2-1)*v2)/n);
    left = m1 - m2 - t*sp*sqrt(1/n1+1/n2);
    right = m1 - m2 + t*sp*sqrt(1/n1+1/n2);
else 
    fprintf('The sigmas are different!\n');
    % if H=1 (from point a) it means that sigmas are different
    % we will use the formula from the file to compute the confidence interval
    c = (v1/n1)/(v1/n1+v2/n2);
    n = c^2/(n1-1)+(1-c)^2/(n2-1);
    n = 1/n;
    % we compute the student inverse distribution
    t = tinv(1-alpha/2, n);
    left = m1-m2-t*sqrt(v1/n1+v2/n2);
    right = m1-m2+t*sqrt(v1/n1+v2/n2);
end
fprintf("The 95 confidence interval is: (%.4f, %.4f)\n", left, right);