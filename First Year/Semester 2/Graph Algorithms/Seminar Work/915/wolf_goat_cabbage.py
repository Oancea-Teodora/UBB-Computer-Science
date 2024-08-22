_items = ["wolf", "goat", "cabbage", "boat"]

class Vertex:
    def __init__(self, d):
        self.__dict = dict(d)
    
    def __repr__(self):
        return f"{self.__dict}"

    def __str__(self):
        return f"{self.__dict}"

    def parse_neighbors(self):
        for item in _items:
            d = dict(self.__dict)
            if item != "boat":
                if d[item] != d["boat"]:
                    continue
                d[item] = not d[item]
            d["boat"] = not d["boat"]
            v = Vertex(d)
            if v.is_valid():
                yield v
    
    def is_valid(self):
        if self.__dict["goat"] == self.__dict["boat"]:
            return True
        if self.__dict["goat"] == self.__dict["wolf"]:
            return False
        if self.__dict["goat"] == self.__dict["cabbage"]:
            return False
        return True

    def __eq__(self, other):
        if not isinstance(other, Vertex):
            return False
        return self.__dict == other.__dict

    def __hash__(self):
        val = 0
        for key in _items:
            val = val * 2 + (1 if self.__dict[key] else 0)
        return val

class WolfGoatCabbageGraph:
    def is_edge(self, x, y):
        '''Returns True if there is an edge from x to y in the graph, and False otherwise.
            Precondition: x and y are valid vertices of the graph.
        '''
        raise Exception("Not implemented")

    def parse_nout(self, x):
        '''Returns something iterable that contains all the outbound neighbors of vertex x
            Precondition: x is a valid vertex of the graph.
        '''
        return x.parse_neighbors()
        
    def parse_nin(self, x):
        '''Returns something iterable that contains all the inbound neighbors of vertex x
            Precondition: x is a valid vertex of the graph.
        '''
        return x.parse_neighbors()
        
    def parse_vertices(self):
        '''Return something iterable that contains all the vertices of the graph
        '''
        raise Exception("Not implemented")

    def init_state(self):
        return Vertex({"wolf" : False, "goat" : False, "cabbage" : False, "boat" : False, })

    def final_state(self):
        return Vertex({"wolf" : True, "goat" : True, "cabbage" : True, "boat" : True, })
