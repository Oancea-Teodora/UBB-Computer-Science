clear all
clc

% Load the Statistics package
pkg load statistics;

% Data
X1 = [46, 37, 39, 48, 47, 44, 35, 31, 44, 37]; % Standard method
X2 = [35, 33, 31, 35, 34, 30, 27, 32, 31, 31]; % New method

n1 = length(X1);
n2 = length(X2);

% Significance level for hypothesis tests
alpha = 0.05;

% Part (a): Testing if population variances differ
% H0: sigma1 = sigma2 (variances are equal)
% H1: sigma1 != sigma2 (variances are different)

fprintf('Part (a): Test if population variances differ\n');
tail = "both"; % Two-tailed test for vartest2
[h_var, p_var, ci_var, stats_var] = vartest2(X1, X2, "alpha", alpha, "tail", "both");

if h_var == 0
    fprintf('H0 is not rejected. Population variances are equal.\n');
else
    fprintf('H0 is rejected. Population variances differ.\n');
end

% Calculate rejection region for the F-test
q1 = finv(alpha / 2, n1-1, n2-1); % Lower critical value
q2 = finv(1 - alpha / 2, n1-1, n2-1); % Upper critical value

fprintf('Observed F-statistic = %4.3f\n', stats_var.fstat);
fprintf('P-value = %4.3f\n', p_var);
fprintf('Rejection region R = (-inf, %4.3f) U (%4.3f, +inf)\n', q1, q2);

% Part (b): Confidence Interval for the Difference of Means
% Using two-sample t-test for difference of means with equal variances
% Confidence level = 95%
alphaminusone = 0.95;
alpha = 1 - alphaminusone;

fprintf('\nPart (b): Confidence Interval for the Difference of Means\n');

% Pooled standard deviation (as variances are assumed equal from part (a))
s1 = std(X1);
s2 = std(X2);

if h_var == 0
    % Pooled standard deviation (equal variances)
    sp = sqrt(((n1 - 1) * s1^2 + (n2 - 1) * s2^2) / (n1 + n2 - 2));  % Pooled standard deviation
    t_crit = tinv(1 - alpha / 2, n1 + n2 - 2);
    margin_of_error = t_crit * sp * sqrt(1 / n1 + 1 / n2);
else
    % Separate standard deviations (unequal variances)
    c = (s1^2/n1) / (s1^2/n1 + s2^2/n2);
    n = 1 / (c^2/(n1-1) + (1-c)^2/(n2-1));
    t_crit = tinv(1 - alpha / 2, n);
    margin_of_error = t_crit * sqrt(s1^2/n1 + s2^2/n2);
end

mean_diff = mean(X1) - mean(X2); % Difference of means
ci_lower = mean_diff - margin_of_error;
ci_upper = mean_diff + margin_of_error;

fprintf('95%% confidence interval for the difference of means: (%.4f, %.4f)\n', ci_lower, ci_upper);

