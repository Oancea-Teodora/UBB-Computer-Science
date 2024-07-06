import matplotlib.pyplot as plt
import numpy as np


def custom_print(*values, color: str, end: str = "\n") -> None:
    formated_message = "".join([str(value) for value in values])
    print(f"{color}{formated_message}\033[0m", end=end)


def read_ranged_number(left, right, message) -> int:
    number_count_in_list = -1
    while True:
        number_count_in_list = input(message)
        try:
            number_count_in_list = float(number_count_in_list)
            if number_count_in_list < left or number_count_in_list > right:
                raise ValueError
            break
        except:
            custom_print(
                f"\nInvalid input. Input must be a number between {left} and {right}.\n", color="\033[31m")
            continue
    return number_count_in_list


def display_menu():
    print("\n1. (a)Take a convex function and show that for small learning rate method converges to the minimum off.")
    print("2  (b) Show that by increasing learning rate the method can converge faster (in fewer steps).")
    print("3. (c) Show that taking learning rate too large might lead to the divergence of the method.")
    print("4. (d) Take a nonconvexfand show that the method can get stuck in a local minimum.")
    print("5.  Exit the program\n")

# (a)Take a convex function and show that for small learning rate method converges to the minimum off.


def gradient_descent_convex(lr):
    def f(x):
        return x ** 2

    def df(x):
        return 2 * x
    x = -5

    n_iter = 100

    x_range = np.linspace(-5, 5, 100)
    plt.plot(x_range, f(x_range))
    plt.xlabel("x")
    plt.ylabel("f(x)")
    plt.title(
        f"Gradient descent method visualization\nLearning rate = {lr}\nNumber of iterations = {n_iter}")

    for _ in range(n_iter):
        x = x - lr * df(x)
        plt.scatter(x, f(x), color="red")
        plt.plot([x, x - lr * df(x)], [f(x), f(x - lr * df(x))], color="red")

    plt.show()


def gradient_descent_nonconvex(lr):
    def f(x):
        return x * 4 - 2 * x * 3

    def df(x):
        return 4 * x * 3 - 6 * x * 2
    x = -1

    n_iter = 100

    x_range = np.linspace(-1, 2, 200)
    plt.plot(x_range, f(x_range))
    plt.xlabel("x")
    plt.ylabel("f(x)")
    plt.title(
        f"Gradient descent method visualization\nLearning rate = {lr}\nNumber of iterations = {n_iter}")

    for _ in range(n_iter):
        x = x - lr * df(x)
        plt.scatter(x, f(x), color="red")
        plt.plot([x, x - lr * df(x)], [f(x), f(x - lr * df(x))], color="red")

    plt.show()


def a_problem():
    gradient_descent_convex(0.05)


def b_problem():
    gradient_descent_convex(0.15)


def c_problem():
    gradient_descent_convex(0.9)


def d_problem():
    gradient_descent_nonconvex(0.01)


def app():
    while True:
        display_menu()
        user_input = read_ranged_number(1, 5, "Enter your choice: ")
        if user_input == 0:
            return
        elif user_input == 1:
            a_problem()
        elif user_input == 2:
            b_problem()
        elif user_input == 3:
            c_problem()
        elif user_input == 4:
            d_problem()


if __name__ == "__main__":

    app()