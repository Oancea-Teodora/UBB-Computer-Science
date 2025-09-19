clear all
clc

% Load the Statistics package
pkg load statistics;

% Data
steel = [4.6, 0.7, 4.2, 1.9, 4.8, 6.1, 4.7, 5.5, 5.4];
glass = [2.5, 1.3, 2.0, 1.8, 2.7, 3.2, 3.0, 3.5, 3.4];

n1 = length(steel);
n2 = length(glass);

% Part (a): Test if population variances differ
fprintf('Part (a): Test if population variances differ\n');
alpha = 0.05; % Significance level
tail = "both"; % Two-tailed test for vartest2

% Perform the F-test
[h_var, p_var, ci_var, stats_var] = vartest2(steel, glass, "alpha", alpha, "tail", tail);

% Output results
if h_var == 0
    fprintf('H0 is not rejected. Population variances are equal. The variances of heat loss in steel and glass pipes are statistically similar.\n');
else
    fprintf('H0 is rejected. Population variances differ.  The variances of heat loss in steel and glass pipes are statistically different.\n');
end

% Calculate rejection region for the F-test
q1 = finv(alpha / 2, n1-1, n2-1); % Lower critical value
q2 = finv(1 - alpha / 2, n1-1, n2-1); % Upper critical value

fprintf('Observed F-statistic = %.4f\n', stats_var.fstat);
fprintf('P-value = %.4f\n', p_var);
fprintf('Rejection region R = (-inf, %.4f) U (%.4f, +inf)\n', q1, q2);

% Part (b): Test if Steel loses more heat than Glass
fprintf('\nPart (b): Test if Steel loses more heat than Glass\n');

% Null and alternative hypotheses
% H0: mu_steel = mu_glass (means are equal)
% H1: mu_steel > mu_glass (steel loses more heat)
tail = "right"; % Right-tailed test


% Perform two-sample t-test
[h_mean, p_mean, ci_mean, stats_mean] = ttest2(steel, glass, "alpha", alpha, "tail", tail, "vartype", "unequal");

% Output results
if h_mean == 0
    fprintf('H0 is not rejected. Steel does NOT lose more heat than Glass.\n');
else
    fprintf('H0 is rejected. Steel DOES lose more heat than Glass.\n');
end

% Calculate rejection region for the t-test
v1=var(steel);
v2=var(glass);
c=(v1/n1)/((v1/n1)+(v2/n2));
n=1/((c^2/(n1-1))+((1-c)^2)/(n2-1))
t_critical = tinv(1 - alpha, n); % Critical t-value for right-tailed test
fprintf('Observed t-statistic = %.4f\n', stats_mean.tstat);
fprintf('P-value = %.4f\n', p_mean);
fprintf('Rejection region R = (%.4f, +inf)\n', t_critical);

