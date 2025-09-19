clear all
clc

% Load the Statistics package
pkg load statistics;

% data
x = [3.26, 1.89, 2.42, 2.03, 3.07, 2.95, 1.39, 3.06, 2.46, 3.35, 1.56, 1.79, 1.76, 3.82, 2.42, 2.96];

% Part (a): Hypothesis Test
fprintf('Part (a): Hypothesis Test\n');
alpha = 0.05; % Significance level
mu0 = 3; % Null hypothesis mean
n = length(x) % Sample size

% Perform one-tailed t-test
[h, p, ci, stats] = ttest(x, mu0, "alpha", alpha, "tail", "left");

% Output results
if h == 1 % the particle seem to be bigger than the expected mean of 3
  printf("we reject the null hypothesis\n")
  printf("The x suggests that particles are on average bigger than size 3\n")
else
  printf("we do not reject the null hypothesis\n")
  printf("The x suggests that particles are on average smaller than size 3\n")
endif

fprintf('Observed t-statistic = %4.3f\n', stats.tstat);
fprintf('P-value = %.4f\n', p);

% Rejection region
t_critical = tinv(alpha, n - 1); % Critical t-value for left-tailed test
fprintf('Rejection region R = (-inf, %4.3f)\n', t_critical);

% Part (b): Confidence Interval for Standard Deviation
fprintf('\nPart (b): Confidence Interval for the Standard Deviation\n');
oneminusalpha = 0.99; % 99% confidence interval
alpha = 1 - oneminusalpha;

s = std(x); % Sample standard deviation

% Critical chi-squared values
chi2_lower = chi2inv(alpha / 2, n - 1); % Lower critical value
chi2_upper = chi2inv(1 - alpha / 2, n - 1); % Upper critical value

% Confidence interval for the variance
variance_lower = (n - 1) * s^2 / chi2_upper;
variance_upper = (n - 1) * s^2 / chi2_lower;

% Confidence interval for the standard deviation
std_lower = sqrt(variance_lower);
std_upper = sqrt(variance_upper);

fprintf('99%% confidence interval for the standard deviation: (%4.3f, %4.3f)\n', std_lower, std_upper);

