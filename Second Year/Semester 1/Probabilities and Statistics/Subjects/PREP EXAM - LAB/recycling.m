clear all
clc

% Load the Statistics package
pkg load statistics;

% Data
X1 = [3.1, 2.9, 3.8, 3.3, 2.7, 3.0, 2.8, 2.5, 2.6, 2.0, 3.2, 2.4, 2.3, 3.1, 2.1, 3.4];
X2 = [6.9, 6.4, 4.7, 4.3, 5.1, 6.3, 5.9, 5.4, 5.3, 5.2, 5.1, 5.9, 5.8, 4.9];

n1 = length(X1);
n2 = length(X2);

% Significance level
alpha = 0.05;

% Part (a): Testing if population variances differ
% H0: sigma1 = sigma2 (variances are equal)
% H1: sigma1 != sigma2 (variances are different)

fprintf('Part (a): Test if population variances differ\n');
tail = "both"; % Two-tailed test for vartest2
[h_var, p_var, ci_var, stats_var] = vartest2(X1, X2, "alpha", alpha, "tail", tail);

if h_var == 0
    fprintf('H0 is not rejected. Population variances are equal.\n');
else
    fprintf('H0 is rejected. Population variances differ.\n');
end

% Calculate rejection region for the F-test
q1 = finv(alpha / 2, n1-1, n2-1); % Lower critical value
q2 = finv(1 - alpha / 2, n1-1, n2-1); % Upper critical value

fprintf('Observed F-statistic = %.4f\n', stats_var.fstat);
fprintf('P-value = %.4f\n', p_var);
fprintf('Rejection region R = (-inf, %.4f) U (%.4f, +inf)\n', q1, q2);

% Part (b): Testing if other employees dispose more paper than bank employees
% H0: mu1 = mu2 (means are equal)
% H1: mu1 < mu2 (other employees dispose more paper)


fprintf('\nPart (b): Test if other employees dispose more paper than bank employees\n');
tail = "left"; % Left-tailed test for ttest2
[h_mean, p_mean, ci_mean, stats_mean] = ttest2(X1, X2, "alpha", alpha, "tail", tail, "vartype", "equal");
% Use 'equal' as variances are equal from part (a)

if h_mean == 0
    fprintf('H0 is not rejected. Other employees do NOT dispose more paper than bank employees.\n');
else
    fprintf('H0 is rejected. Other employees DO dispose more paper than bank employees.\n');
end

% Calculate rejection region for the t-test
q1 = tinv(alpha, n1+n2-2); % Critical value for left-tailed test

fprintf('Observed t-statistic = %.4f\n', stats_mean.tstat);
fprintf('P-value = %.4f\n', p_mean);
fprintf('Rejection region R = (-inf, %.4f)\n', q1);

