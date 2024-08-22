import graph
import wolf_goat_cabbage
import heapq
import math

class TreeNode:
    '''A tree node, with 3 members: parent : TreeNode or None, children : list of TreeNode, level : int
    '''
    def __str__(self):
        return f"[parent: {self.parent}, level: {self.level}, children: {self.children}]"
    def __repr__(self):
        return f"[parent: {self.parent}, level: {self.level}, children: {self.children}]"

def bfs(g, s):
    '''Performs a Breadth-First Search of the graph g, starting from vertex s, in forward direction
        Returns a dictionary mapping accessible vertices to the corresponding TreeNode information
    '''
    tree = {}
    q = [s]
    tree[s] = TreeNode()
    tree[s].parent = None
    tree[s].level = 0
    tree[s].children = []
    head = 0
    while head < len(q):
        x = q[head]
        head += 1
        for y in g.parse_nout(x):
            if y not in tree.keys():
                q.append(y)
                tree[y] = TreeNode()
                tree[y].parent = x
                tree[y].level = tree[x].level + 1
                tree[y].children = []
                tree[x].children.append(y)
        #print(f"x={x}, q={q[head:]}, tree={tree}")
    return tree

def shortest_path(g, s, t):
    tree = bfs(g, s)
    print(tree)
    start = s
    target = t
    if target not in tree.keys():
        return None
    path_rev = list()
    while target!=start:
        path_rev.append(target)
        target = tree[target].parent
    path_rev.append(start)
    path_rev.reverse()
    return path_rev
    
def dijkstra(g, s):
    '''Executes Dijkstra algorithm in graph g, starting from vertex s, in forward direction
        Returns a dictionary mapping accessible vertices to the corresponding TreeNode information
    '''
    tree = {}
    q = [(0,s)]
    tree[s] = TreeNode()
    tree[s].parent = None
    tree[s].level = 0
    tree[s].children = []
    while len(q) > 0:
        dx,x = heapq.heappop(q)
        if dx > tree[x].level:
            print(f"Skipping {x}")
            continue
        if tree[x].parent is not None:
            tree[tree[x].parent].children.append(x)
        for y in g.parse_nout(x):
            if y not in tree.keys() or tree[y].level > tree[x].level + g.cost(x,y):
                if y not in tree.keys():
                    tree[y] = TreeNode()
                    tree[y].children = []
                tree[y].parent = x
                tree[y].level = tree[x].level + g.cost(x,y)
                heapq.heappush(q, (tree[y].level, y))
                #tree[x].children.append(y)
        print(f"x={x}, q={q}, tree={tree}")
    return tree

def min_cost_dijkstra(g, s, t):
    tree = dijkstra(g, s)
    print(tree)
    start = s
    target = t
    if target not in tree.keys():
        return None
    path_rev = list()
    while target!=start:
        path_rev.append(target)
        target = tree[target].parent
    path_rev.append(start)
    path_rev.reverse()
    return path_rev

def dp_compute_costs(g, s):
    '''Returns a list of dictionaries w so that w[k][x] = cost of min cost walk from s to x and
    of length equal to k, on float('inf') if no such walk exists
    '''
    w = [{x:float('inf') for x in g.parse_vertices()}]
    w[0][s] = 0
    for k in range(len(g.parse_vertices())):
        w.append({})
        for x in g.parse_vertices():
            min_cost = float('inf')
            for y in g.parse_nin(x):
                if min_cost > w[k][y] + g.cost(y, x):
                    min_cost = w[k][y] + g.cost(y, x)
            w[k+1][x] = min_cost
    return w

def min_cost_dp(g, s, t):
    w = dp_compute_costs(g, s)
    print(w)
    start = s
    target = t
    min_cost = float('inf')

    for k in range(len(w)):
        if min_cost > w[k][target]:
            min_cost = w[k][target]
            len_min_cost = k

    if min_cost == float('inf'):
        return None
    print(f"min_cost={min_cost}; len={len_min_cost}")
    path_rev = list()
    while target!=start:
        path_rev.append(target)
        for x in g.parse_nin(target):
            if w[len_min_cost-1][x] + g.cost(x, target) == w[len_min_cost][target]:
                prev = x
                break
        target = prev
        len_min_cost -= 1
    path_rev.append(start)
    path_rev.reverse()
    return path_rev

def toposort(g):
    '''Returns the list of vertices of the graph g in a topologically sorted order,
    or None if there is a cycle
    '''
    sorted_list = []
    indegree = {}
    for x in g.parse_vertices():
        indegree[x] = len(list(g.parse_nin(x)))
        if indegree[x] == 0:
            sorted_list.append(x)
    head = 0
    while head < len(sorted_list):
        x = sorted_list[head]
        head += 1
        for y in g.parse_nout(x):
            indegree[y] -= 1
            if indegree[y] == 0:
                sorted_list.append(y)
    if len(sorted_list) < len(indegree):
        return None
    return sorted_list

def min_cost_dag(g, s):
    '''Computes the distances and the predecessors on minimum cost walks starting from vertex s in a DAG 
        Returns a dictionary mapping accessible vertices to the corresponding TreeNode information
    '''
    tree = {}
    sorted_list = toposort(g)
    for x in sorted_list:
        if x == s:
            dist = 0
            prev = None
        else:
            dist = math.inf
            prev = None
            for y in g.parse_nin(x):
                if y in tree.keys() and dist > tree[y].level + g.cost(y, x):
                    dist = tree[y].level + g.cost(y, x)
                    prev = y
        if dist < math.inf:
            tree[x] = TreeNode()
            tree[x].level = dist
            tree[x].parent = prev
            tree[x].children = None
        print(f"x={x}, tree={tree}")
    return tree

def min_cost_path_dag(g, s, t):
    tree = min_cost_dag(g, s)
    print(tree)
    start = s
    target = t
    if target not in tree.keys():
        return None
    path_rev = list()
    while target!=start:
        path_rev.append(target)
        target = tree[target].parent
    path_rev.append(start)
    path_rev.reverse()
    return path_rev

class DagComputation:
    def __init__(self, g, func):
        self.g = g
        self.func = func
        self.values = {}
    def compute(self, x):
        if x not in self.values.keys():
            args = []
            for y in g.parse_nin(x):
                args.append((y, self.compute(y)))
                self.values[x] = self.func(args)
        return self.values[x]

def min_dist(g, prev):
    

def compute_on_dag(g, func, s):
    comp = DagComputation(g, func)
    return comp.compute(s)

def test_shortest_path():
    g = graph.create_small_graph(graph.Graph)
    #print(bfs(g, 2))
    path = shortest_path(g, 0, 3)
    print(f"path={path}")

def test_wolf_goat_cabbage():
    g = wolf_goat_cabbage.WolfGoatCabbageGraph()
    s = g.init_state()
    t = g.final_state()
    path = shortest_path(g, s, t)
    print(f"path={path}")

def test_dijkstra():
    g = graph.create_small_graph(graph.Graph)
    print(dijkstra(g, 0))
    path = min_cost_dijkstra(g, 0, 3)
    print(f"path={path}")

def test_dp():
    g = graph.create_small_graph(graph.Graph)
    #print(dp_compute_costs(g, 0))
    path = min_cost_dp(g, 0, 3)
    print(f"path={path}")

def test_dag():
    g = graph.create_small_dag(graph.Graph)
    print(min_cost_path_dag(g, 4, 0))

def test_dag_2():
    g = graph.create_small_dag(graph.Graph)
    print(compute_on_dag(g, 4, 0))

if __name__ == "__main__":
    test_dag()
