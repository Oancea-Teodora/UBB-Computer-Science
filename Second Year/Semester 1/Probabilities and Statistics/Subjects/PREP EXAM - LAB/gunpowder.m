pkg load statistics; % Load Statistics package for ttest and chi2inv

% Data
data = [1001.7, 975.0, 978.3, 988.3, 978.7, 988.9, 1000.3, 979.2, 968.9, 983.5, 999.2, 985.6];

% Part (a): Hypothesis Test
mu0 = 995; % Null hypothesis mean
alpha = 0.05; % Significance level
n = length(data); % Sample size
x_bar = mean(data); % Sample mean
s = std(data); % Sample standard deviation

% Perform one-tailed t-test
[h, p, ci, stats] = ttest(data, mu0, "alpha", alpha, "tail", "right");

% Display results for hypothesis test
printf("Part (a): Hypothesis Test\n");
if h == 1
    printf("Reject the null hypothesis: The mean velocity is significantly greater than 995 m/s.\n");
else
    printf("Fail to reject the null hypothesis: The data does not suggest the mean velocity is greater than 995 m/s.\n");
endif

talpha = tinv(1-alpha, n-1);

rr = [talpha, inf];
printf("The rejection region is (%4.3f, %4.3f).\n", rr);
printf("Observed t-statistic = %4.3f\n", stats.tstat);
printf("p-value = %.4f\n", p);

% Part (b): Confidence Interval for Standard Deviation
oneminusalpha = 0.99;
alpha = 1 - oneminusalpha; % Confidence level = 99%
chi2_lower = chi2inv(alpha / 2, n-1); % Lower critical value
chi2_upper = chi2inv(1 - alpha / 2, n-1); % Upper critical value

% Confidence interval for the variance
variance_lower = (n - 1) * s^2 / chi2_upper;
variance_upper = (n - 1) * s^2 / chi2_lower;

% Confidence interval for the standard deviation
std_lower = sqrt(variance_lower);
std_upper = sqrt(variance_upper);

printf("\nPart (b): Confidence Interval for Standard Deviation\n");
fprintf("confidence interval for the standard deviation: (%4.3f, %4.3f)\n", std_lower, std_upper);




