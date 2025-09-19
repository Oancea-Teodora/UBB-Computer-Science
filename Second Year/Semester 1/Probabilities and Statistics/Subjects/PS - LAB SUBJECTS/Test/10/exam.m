#
A = [1021 980 1017 988 1005 998 1014 985 995 1004 1030 1015 995 1023];
B = [1070 970 993 1013 1006 1002 1014 997 1002 1010 975];

alpha = input("Please enter the significance level: ");

printf("\na)\n");
# vartest2 - check for comparing variances of two populations
# H0: var(A) = var(B)
# H1: var(A) != var(B)
# both tail test because we check if they differ

[h, pval, ci, stats] = vartest2(A, B, 'alpha', alpha, 'tail', 'both');

# compute the quantiles for the rejection region
q1 = finv(alpha/2, stats.df1, stats.df2);
q2 = finv(1 - alpha/2, stats.df1, stats.df2);

if h == 1
  printf("The null hypothesis is rejected, ie. variances do differ!\n");
  printf("The data suggests that the standard is not met\n");

else
  printf("The null hypothesis is not rejected, ie. variances do not differ!\n");
  printf("The data suggests that the standard is met\n");
end

printf("The rejection region is (-inf, %4.3f) U (%4.3f, +inf)\n", q1, q2);
printf("The observed value of the test statistic is %4.3f\n", stats.fstat);
printf("The pval of the test is %4.3f\n\n\n", pval);

# b
# we check if mean(A) > mean(B)
# we do a right tail test
# H0: mean(A) <= mean(B)
# H1: mean(A) > mean(B)

printf("b)\n");

vartype = 'equal';

if h == 1
  vartype = 'unequal';
end

[h, pval, ci, stats] = ttest2(A, B, 'alpha', alpha, 'tail', 'right', 'vartype', vartype);

# compute the quantile for the rejection region
q = tinv(1-alpha, stats.df);

if h == 1
  printf("The null hypothesis is rejected, ie. supplier A seem more reliable\n");
  printf("The data suggests that the standard is not met\n");

else
  printf("The null hypothesis is not rejected, ie. supplier A do not seem more reliable\n");
  printf("The data suggests that the standard is met\n");
end

printf("The rejection region is (%4.3f, +inf)\n", q);
printf("The observed value of the test statistic is %4.3f\n", stats.tstat);
printf("The pval of the test is %4.3f\n", pval);



