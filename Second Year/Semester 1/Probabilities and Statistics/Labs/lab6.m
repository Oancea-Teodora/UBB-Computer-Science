% the idea: past asssumptions vs present assumptions (or possibloe questions)
% H0 <- null hyp <- what was previously known
% H1 <- alt hyp <- the new bit of information
% we build H0 and H1 for an unknown statistical parameter
% the sought parameters are: pop. mean, pop. variance, difference of 2 pop. means
% and/or ratio of two pop. variances
% we write the hyp as follows:
% H0: statistical parameter = some known value
% H1: (the same) statistical parameter > (or < or different) (the same) known value
% usually, average => mean

clear all;
x = [7, 7, 4, 5, 9, 9, ...
     4, 12, 8, 1, 8, 7, ...
     3, 13, 2, 1, 17, 7, ...
     12, 5, 6, 2, 1, 13, ...
     14, 10, 2, 4, 9, 11, ...
     3, 5, 12, 6, 10, 7];

n = length(x);

% significance level
alpha = input("Please give the significance level:");

% 1. a)
% H0: m = 8.5 (or greater) -> standard is ok (null hyp)
% H1: m < 8.5 -> standard is not met (alt hyp)
% This is a left tailed Z-test for the mean

sigma = 5;
m0 = 8.5; % the observed mean

% in octave, i should type help ztest after overwritting the file
% as it's mentioned in teams

% instead of left, it could also be right or both
[h, p, ci, z] = ztest(x, m0, sigma, "alpha", alpha, "tail", "left");


% constructing the rejection region
zalpha = norminv(alpha, 0, 1);
RR = [-inf zalpha];

if h == 1 % h = 1 -> we reject H0, h = 0 -> we don't reject H0
    printf("The value of h is %d. The null hypothesis is rejected.\n", h);
    printf("The data suggests that the standard is not met.\n");

else % this one is with h = 0
    printf("The value of h is %d. The null hypothesis is not rejected.\n", h);
    printf("The data suggests that the standard is met.\n");
endif

printf("The rejection region is (%4.3f, %4.3f).\n", RR);
printf("The observed value of the test statistic is %4.3f.\n", z);
printf("The P-value of the test is %4.3f.\n", p);

% 1. b)
% H0? H1? -> for this we have ttest stats.tstat

% 2. a)
% we have to use 4 from the cheat sheet
% H0? H1? -> vartest2 => the variances are equal or different
% two-tailed test

% 2. b)
% we have to use 3, second/third line from the cheat sheet
% the solution is influenced by the answer at 2. a)
% ttest2











