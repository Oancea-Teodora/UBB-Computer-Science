import numpy as np

def integrand(x):
    return np.exp(-x**2)

def trapezoidal_rule(func, a, b, n):
    h = (b - a) / n
    x_values = np.linspace(a, b, n + 1)
    y_values = func(x_values)
    result = h * (0.5 * y_values[0] + 0.5 * y_values[-1] + np.sum(y_values[1:-1]))
    return result
a = 100  # Modify a
n = 1000
integral_result = trapezoidal_rule(integrand, -a, a, n)
expected_result = np.sqrt(np.pi)
print(f"Numerical result: {integral_result}")
print(f"Expected result (sqrt(pi)): {expected_result}")