% Load the Statistics package
pkg load statistics

% Define the data vector
x = [3.26, 1.89, 2.42, 2.03, 3.07, 2.95, 1.39, 3.06, ...
        2.46, 3.35, 1.56, 1.79, 1.76, 3.82, 2.42, 2.96];

n = length(x)
%a)
oneminusalpha = 0.95;
alpha = 1 - oneminusalpha;

s = std(x); %standard deviation for the particle sizes
m1b = mean(x)-(s/sqrt(n))*tinv(1-alpha/2,n-1);
m2b = mean(x)-(s/sqrt(n))*tinv(alpha/2,n-1);
printf("the confidence interval for the mean particle size (%4.3f, %4.3f)\n", m1b, m2b)

printf("b)\n")
%b)
alpha = 0.01;

m0 = 3;%particle size mean of 3

[h, p, ci, z] = ttest(x, m0, "alpha", alpha, "tail", "left")
if h == 1 % the particle seem to be bigger than the expected mean of 3
  printf("we reject the null hypothesis\n")
  printf("The data suggests that particles are on average bigger than size 3\n")
else
  printf("we do not reject the null hypothesis\n")
  printf("The data suggests that particles are on average smaller than size 3\n")
endif

talpha = tinv(alpha, n-1);

rr = [-inf, talpha];
printf("The rejection region is (%4.3f, %4.3f).\n", rr);
printf("The observed value of the test statistic is %4.3f.\n", z.tstat);
printf("The p-value of the test %4.3f.\n", p);
