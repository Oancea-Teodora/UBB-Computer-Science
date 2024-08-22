import random
from copy import deepcopy
from collections import deque


class Graph:
    def __init__(self, n=0):
        '''Constructs a graph with n vertices numbered from 0 to n and no edges
        '''

        self.__n = n

        self.__in_edges = {}
        for i in range(n):
            self.__in_edges[i] = set()

        self.__out_edges = {}
        for i in range(n):
            self.__out_edges[i] = set()

        self.__cost = {}
        self.edge_count = 0
        # self.num = 4000000
        # for i in range(n):
        # self.__cost[i] = set()

    def add_vertex(self, x):
        '''Inserts the vertex into the graph.
        Precondition: x is not already a vertex.
        '''
        self.__in_edges[x] = set()
        self.__out_edges[x] = set()

        # self.__cost[x] = set()

    def add_edge(self, x, y, cost, mm):
        '''Adds an edge from vertex x to vertex y and returns True.
            If the edge already exists, nothing happens and the function returns False.
            Precondition: x and y are valid vertices of the graph.
        '''
        if y in self.__out_edges[x]:
            return False
        self.__out_edges[x].add(y)
        self.__in_edges[y].add(x)
        self.__cost[(x, y)] = cost
        self.edge_count += 1
        # self.__cost[x].add(cost)
        # self.__cost[y].add(cost)
        # mm[0]=mm[0]+1
        # self.num = self.num+1

        return True

    def add_edge_first(self, x, y, cost):
        '''Adds an edge from vertex x to vertex y and returns True.
            If the edge already exists, nothing happens and the function returns False.
            Precondition: x and y are valid vertices of the graph.
        '''
        if y in self.__out_edges[x]:
            return False
        self.__out_edges[x].add(y)
        self.__in_edges[y].add(x)
        self.__cost[(x, y)] = cost

        return True

    def is_edge(self, x, y):
        '''Returns True if there is an edge from x to y in the graph, and False otherwise.
            Precondition: x and y are valid vertices of the graph.
        '''
        return y in self.__out_edges[x]

    def parse_nout(self, x):
        '''Returns something iterable that contains all the outbound neighbors of vertex x
            Precondition: x is a valid vertex of the graph.
        '''
        return set(self.__out_edges[x])

    def parse_nin(self, x):
        '''Returns something iterable that contains all the inbound neighbors of vertex x
            Precondition: x is a valid vertex of the graph.
        '''
        return set(self.__in_edges[x])

    def parse_vertices(self):
        '''Return something iterable that contains all the vertices of the graph
        '''
        return set(self.__in_edges.keys())

    def get_cost(self, x, y):
        '''Returns the cost associated with the edge from vertex x to vertex y.
            Precondition: There is an edge from x to y in the graph.
        '''
        return self.__cost[(x, y)] if (x, y) in self.__cost else None

    def modify_cost(self, x, y, c):
        '''
        Modifies the cost of an edge
        Precondition: x and y are valid vertices
        '''
        if self.is_edge(x, y):
            self.__cost[(x, y)] = c

    def remove_vertex(self, x):
        '''Removes the vertex x from the graph along with all its incident edges.
            Precondition: x is a valid vertex of the graph.
        '''
        for i in self.__out_edges[x]:
            self.__in_edges[i].remove(x)
            del self.__cost[(x, i)]
            self.edge_count -= 1
        del self.__out_edges[x]

        for i in self.__in_edges[x]:
            self.__out_edges[i].remove(x)
            del self.__cost[(i, x)]
            self.edge_count -= 1
        del self.__in_edges[x]

    def remove_edge(self, x, y, mm):
        '''
        Removes an edge from x to y
        Precondition: x and y are valid vertices
        '''
        # if self.is_edge(y, x):
        self.__in_edges[y].remove(x)
        self.__out_edges[x].remove(y)
        del self.__cost[(x, y)]
        self.edge_count -= 1
        # mm[0] = mm[0]-1
        # self.num = self.num-1

    def copy(self):
        """
        Returns a deepcopy of the graph.
        """
        return deepcopy(self)

    def bfs(self):
        q = deque()
        vizitat = []
        com = []
        for x in self.parse_vertices():
            vizitat.append(0)
        for x in self.parse_vertices():
            com.clear()
            if vizitat[x] == 0:
                q.append(x)

                while len(q) != 0:
                    xx = q.popleft()
                    com.append(xx)
                    vizitat[xx] = 1
                    for y in self.parse_nout(xx):
                        if vizitat[y] == 0:
                            vizitat[y] = 1
                            q.append(y)
            sem = 0
            for i in com:
                sem = 1
                print(i, end=" ")
            if sem == 1:
                print()

    def minimum_cost_walk(self, s):
        #we create the matrix d[k,x]=the cost of the lowest cost walk from s to x and of length k, where s is the starting vertex.
        num_vertices = len(self.parse_vertices())
        d = [{x: float('inf') for x in self.parse_vertices()} for _ in range(num_vertices + 1)]
        d[0][s] = 0

        for k in range(1, num_vertices + 1):
            for x in self.parse_vertices():
                d[k][x] = d[k - 1][x]
                for y in self.parse_nin(x):
                    if d[k - 1][y] + self.__cost[(y, x)] < d[k][x]:
                        d[k][x] = d[k - 1][y] + self.__cost[(y, x)]

        for x in self.parse_vertices():
            for y in self.parse_nin(x):
                if d[num_vertices][y] + self.__cost[(y, x)] < d[num_vertices][x]:
                    return "Negative cycle detected"

        return d

    def min_cost_dp(self, s, t):
        d = self.minimum_cost_walk(s)
        if d == "Negative cycle detected":
            print("Negative cycle detected")
            return
        start = s
        target = t
        min_cost = float('inf')

        for k in range(len(d)):
            if min_cost > d[k][target]:
                min_cost = d[k][target]
                len_min_cost = k

        if min_cost == float('inf'):
            return None
        return min_cost

    def kruskal(self, n, m):
        t = []
        tree = []
        for i in self.parse_vertices():
            t.append(i)
       # print(n,m)
        edges = []
        for x in self.parse_vertices():
            for y in self.parse_nin(x):
                if x < y:
                    edges.append((self.get_cost(x, y), x, y))

        s = 0
        edges.sort()
        for i in range(len(edges)):

            if t[edges[i][1]] != t[edges[i][2]]:
                tree.append(edges[i])
                s = s+edges[i][0]
                a = t[edges[i][2]]
                b = t[edges[i][1]]
                for j in range(n):
                    if t[j] == a:
                        t[j] = b

        print(tree)
        return s






























































    def min_cost_exponential(self, s, t):
        def dfs(v, target, visited, current_cost, path):
            if v == target:
                return current_cost

            if v in path:
                # Detected a cycle
                cycle_cost = 0
                cycle_start = path.index(v)
                for i in range(cycle_start, len(path) - 1):
                    cycle_cost += self.__cost[(path[i], path[i + 1])]
                cycle_cost += self.__cost[(path[-1], v)]

                if cycle_cost < 0:
                    return "Negative cycle detected"

            min_cost = float('inf')
            path.append(v)
            for u in self.parse_nin(v):
                if u not in visited:
                    cost = dfs(u, target, visited | {u}, current_cost + self.__cost[(u, v)], path)
                    if cost == "Negative cycle detected":
                        return "Negative cycle detected"
                    min_cost = min(min_cost, cost)
            path.pop()
            return min_cost

        result = dfs(s, t, set(), 0, [])
        return result if result != float('inf') else None

