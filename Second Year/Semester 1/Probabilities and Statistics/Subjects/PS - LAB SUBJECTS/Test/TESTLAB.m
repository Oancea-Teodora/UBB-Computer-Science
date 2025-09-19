clear all
clc

% point a
% CI (confidence interval) = 95% =  100(1-alpha)%
alpha = 0.05; % significance level
x = [3.26 1.89 2.42 2.03 3.07 2.95 1.39 3.06 2.46 3.35 1.56 1.79 1.76 3.82 2.42 2.96];
xb = mean(x); % average
s = std(x); % standard deviation
n = length(x); % length of n

tl = xb - (s/sqrt(n)) * tinv(1-alpha/2, n-1) 
tu = xb - (s/sqrt(n)) * tinv(alpha/2, n-1)
fprintf("the confidence interval is (%3.4f, %3.4f)\n", tl, tu);

% point b
% alpha is the significance level
alpha = 0.01;
% we are doing a left tailed test
% H0 theta1 = theta2
% H1 theta1 < theta2
%tail values: -1 for left tailed
%           : 0 for both, default
%           : 1 for rigth tailed
% STATS -.tstat = TS0 ->value of statistics stat
% We used ttest because sigma is unknown and we have to test the mean of a
% population.
% h tells us if we reject the null hypothesis or not
% h = 0 -> do not reject H0
% h = 1 -> reject H0
% 3 is the testing value;
[h, p, ci, stats] = ttest(x, 3, alpha, -1);
if h == 0
    fprintf("H0 is not rejected, nickle particles seem to be HIGHER than 3.\n");
else
    fprintf("H0 is rejected, nickle particles seem to be SMALLER than 3.\n");
end

q1 = tinv(alpha, n-1);
fprintf("The rejection region is (-inf, %3.4f)\n", q1);
fprintf('The value of the test statistic is %1.4f\n', stats.tstat);
fprintf("The P-value of the test is %3.4f\n", p);


