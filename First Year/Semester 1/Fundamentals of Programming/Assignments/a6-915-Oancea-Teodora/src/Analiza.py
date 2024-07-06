import numpy as np
import matplotlib.pyplot as plt
def generate_points_inside_unit_ball(p, num_points=1000):
    # Generate random points in the range [-1, 1] for both dimensions
    points = np.random.uniform(-1, 1, size=(num_points, 2))
    norms = np.linalg.norm(points, ord=p, axis=1)
    inside_points = points[norms <= 1]
    return inside_points
def plot_unit_ball(p, inside_points):
    fig, ax = plt.subplots()
    ax.set_aspect('equal', adjustable='box')  # Set aspect ratio to be equal
    # Plot points inside the unit ball
    ax.scatter(inside_points[:, 0], inside_points[:, 1], color='blue', marker='.')
    theta = np.linspace(0, 2*np.pi, 100)
    x = np.cos(theta)
    y = np.sin(theta)
    ax.plot(x, y, color='black', linestyle='--')
    ax.set_title(f'Unit Ball for p={p}-norm')
    ax.set_xlim([-1.5, 1.5])
    ax.set_ylim([-1.5, 1.5])
    ax.grid(True)
    plt.show()
p_values = [1.25, 1.5, 3, 8]
for p in p_values:
    inside_points = generate_points_inside_unit_ball(p)
    plot_unit_ball(p, inside_points)