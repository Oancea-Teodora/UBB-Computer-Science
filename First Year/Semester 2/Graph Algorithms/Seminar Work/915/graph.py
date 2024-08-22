import random
import time

class Graph:
    def __init__(self, n=0):
        '''Constructs a graph with n vertices numbered from 0 to n and no edges
        '''
        self.__n=n
        self.__in_edges={}
        for i in range(n):
            self.__in_edges[i]=set()
        self.__out_edges={}
        for i in range(n):
            self.__out_edges[i]=set()
        self.__cost = {}
        
    def add_vertex(self, x):
        '''Inserts the vertex into the graph.
        Precondition: x is not already a vertex.
        '''
        self.__in_edges[x]=set()
        self.__out_edges[x]=set()
    
    def add_edge(self, x, y, c=1):
        '''Adds an edge from vertex x to vertex y and returns True.
            If the edge already exists, nothing happens and the function returns False.
            Precondition: x and y are valid vertices of the graph.
        '''
        if y in self.__out_edges[x]:
            return False
        self.__out_edges[x].add(y)
        self.__in_edges[y].add(x)
        self.__cost[(x,y)] = c
        return True

    def cost(self, x, y):
        '''Returns the cost of the edge (x,y)
            Precondition: (x,y) is an edge of the graph
        '''
        return self.__cost[(x,y)]

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
                

class GraphOutOnly:
    def __init__(self, n=0):
        '''Constructs a graph with n vertices numbered from 0 to n and no edges
        '''
        self.__out_edges={}
        for i in range(n):
            self.__out_edges[i]=set()
        
    def add_vertex(self, x):
        '''Inserts the vertex into the graph.
        Precondition: x is not already a vertex.
        '''
        self.__out_edges[x]=set()
    
    def add_edge(self, x, y):
        '''Adds an edge from vertex x to vertex y and returns True.
            If the edge already exists, nothing happens and the function returns False.
            Precondition: x and y are valid vertices of the graph.
        '''
        if y in self.__out_edges[x]:
            return False
        self.__out_edges[x].add(y)
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
        ans=set()
        for i, out_neighbors in self.__out_edges.items():
            if x in out_neighbors:
                ans.add(i)
        return ans
        
    def parse_vertices(self):
        '''Return something iterable that contains all the vertices of the graph
        '''
        return set(self.__out_edges.keys())
                

class GraphAdjacencyMatrix:
    def __init__(self, n=0):
        '''Constructs a graph with n vertices numbered from 0 to n and no edges
        '''
        self.__n=n
        self.__matrix = [None for i in range(n)]
        for i in range(n):
            self.__matrix[i] = [False for j in range(n)]

    def add_vertex(self, x):
        '''Inserts the vertex into the graph.
        Precondition: x is not already a vertex.
        '''
        raise Exception("Not supported")
    
    def add_edge(self, x, y):
        '''Adds an edge from vertex x to vertex y and returns True.
            If the edge already exists, nothing happens and the function returns False.
            Precondition: x and y are valid vertices of the graph.
        '''
        if self.__matrix[x][y]:
            return False
        self.__matrix[x][y]=True
        return True

    def is_edge(self, x, y):
        '''Returns True if there is an edge from x to y in the graph, and False otherwise.
            Precondition: x and y are valid vertices of the graph.
        '''
        return self.__matrix[x][y]

    def parse_nout(self, x):
        '''Returns something iterable that contains all the outbound neighbors of vertex x
            Precondition: x is a valid vertex of the graph.
        '''
        res=[]
        for i in range(self.__n):
            if self.__matrix[x][i]:
                res.append(i)
        return res;
        
    def parse_nin(self, x):
        '''Returns something iterable that contains all the inbound neighbors of vertex x
            Precondition: x is a valid vertex of the graph.
        '''
        res=[]
        for i in range(self.__n):
            if self.__matrix[i][x]:
                res.append(i)
        return res;
        
    def parse_vertices(self):
        '''Return something iterable that contains all the vertices of the graph
        '''
        return range(self.__n)

def create_small_graph(ctor):
    g = ctor(5)
    g.add_edge(0, 1, 1)
    g.add_edge(0, 2, 4)
    g.add_edge(1, 2, 2)
    g.add_edge(2, 0, 2)
    g.add_edge(2, 3, 3)
    return g

def create_small_dag(ctor):
    g = ctor(5)
    g.add_edge(0, 1, 1)
    g.add_edge(0, 2, 4)
    g.add_edge(1, 2, 2)
    g.add_edge(2, 3, 3)
    g.add_edge(2, 4, -1)
    return g

def create_random_graph(ctor, n, m):
    g = ctor(n)
    while m > 0:
        x = random.randrange(n)
        y = random.randrange(n)
        if g.add_edge(x, y):
            m = m - 1
    return g

def print_graph(g):
    print("Outbound:")
    for x in g.parse_vertices():
        print(f"{x}:", end='')
        for y in g.parse_nout(x):
            print(f" {y}", end='')
        print()
    print("Inbound:")
    for x in g.parse_vertices():
        print(f"{x}:", end='')
        for y in g.parse_nin(x):
            print(f" {y}", end='')
        print()

def measure_time(g):
    print("Outbound:")
    t = time.time()
    for x in g.parse_vertices():
        for y in g.parse_nout(x):
            pass
    print(f"Used {(time.time()-t)*1000} ms")
    print("Inbound:")
    t = time.time()
    for x in g.parse_vertices():
        for y in g.parse_nin(x):
            pass
    print(f"Used {(time.time()-t)*1000} ms")

def test_basic_graph():
    #g = create_small_graph(GraphAdjacencyMatrix)
    g = create_random_graph(GraphAdjacencyMatrix, 1000, 500000)
    #print_graph(g)
    measure_time(g)

if __name__ == "__main__":
    test_basic_graph()
