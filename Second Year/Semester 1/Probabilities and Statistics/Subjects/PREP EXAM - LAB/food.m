clear all
clc

% Load the Statistics package
pkg load statistics;

% Data
A = [1021, 980, 1017, 988, 1005, 1014, 985, 995, 1030, 1015, 995, 1023];
B = [1070, 970, 993, 1013, 1006, 1014, 997, 1002, 1010, 975];

n1 = length(A);
n2 = length(B);

% Part (a): Test if population variances differ
fprintf('Part (a): Test if population variances differ\n');
alpha = 0.05; % Significance level
tail = "both"; % Two-tailed test for vartest2

% Perform the F-test
[h_var, p_var, ci_var, stats_var] = vartest2(A, B, "alpha", alpha, "tail", tail);

% Output results
if h_var == 0
    fprintf('H0 is not rejected. Population variances are equal.\n');
else
    fprintf('H0 is rejected. Population variances differ.\n');
end

% Calculate rejection region for the F-test
q1 = finv(alpha / 2,n1-1, n2-1); % Lower critical value
q2 = finv(1 - alpha / 2, n1-1, n2-1); % Upper critical value

fprintf('Observed F-statistic = %.4f\n', stats_var.fstat);
fprintf('P-value = %.4f\n', p_var);
fprintf('Rejection region R = (-inf, %.4f) U (%.4f, +inf)\n', q1, q2);

% Part (b): Test if Supplier A is more reliable
fprintf('\nPart (b): Test if Supplier A is more reliable\n');

% Null and alternative hypotheses
% H0: mu_A = mu_B (means are equal)
% H1: mu_A < mu_B (Supplier A has smaller mean weight)
tail = "left"; % Left-tailed test

% Perform two-sample t-test
[h_mean, p_mean, ci_mean, stats_mean] = ttest2(A, B, "alpha", alpha, "tail", tail, "vartype", "equal");

% Output results
if h_mean == 0
    fprintf('H0 is not rejected. Supplier A does NOT seem to be more reliable.\n');
else
    fprintf('H0 is rejected. Supplier A DOES seem to be more reliable.\n');
end

% Calculate rejection region for the t-test
t_critical = tinv(alpha, n1+n2-2); % Critical t-value for left-tailed test
fprintf('Observed t-statistic = %.4f\n', stats_mean.tstat);
fprintf('P-value = %.4f\n', p_mean);
fprintf('Rejection region R = (-inf, %.4f)\n', t_critical);

