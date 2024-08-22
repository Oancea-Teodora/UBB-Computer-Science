from Graph import Graph
from random import randint


class UI:
    def __init__(self, n):
        n = randint(3, 7)
        self._n = n
        self.g = Graph(n)
        self.copy = Graph(n)

    def run(self):
        UI.menu()
        sem = 0
        opp = 1
        n=0
        mm=[]
        while True:
            try:
                opp = int(input("Choose your option: "))
                if opp == 1:
                    m = randint(2, 6)
                    sem = 1
                    while m > 0:
                        x = randint(1, self._n-1)
                        y = randint(1, self._n-1)
                        c = randint(1, self._n - 1)
                        if self.g.add_edge_first(x, y, c):
                            m = m - 1
                elif opp == 2:
                    x = int(input("The vertex is: "))
                    self.g.add_vertex(x)
                elif opp == 3:
                    x = int(input("The vertex that you want to remove is: "))
                    self.g.remove_vertex(x)
                elif opp == 4:
                    x = int(input("The first coordinate of the edge: "))
                    y = int(input("The second coordinate of the edge: "))
                    c = int(input("The cost of the edge: "))
                    self.g.add_edge(x, y, c, mm)
                    #to make the graph undirected
                    #self.g.add_edge(y, x, c, mm)
                elif opp == 5:
                    x = int(input("The first coordinate of the edge: "))
                    y = int(input("The second coordinate of the edge: "))
                    self.g.remove_edge(x, y, mm)
                elif opp == 6:
                    x = int(input("The first coordinate of the edge: "))
                    y = int(input("The second coordinate of the edge: "))
                    c = int(input("The cost of the edge: "))
                    self.g.modify_cost(x, y, c)
                elif opp == 7:
                    x = int(input("The first coordinate of the edge: "))
                    y = int(input("The second coordinate of the edge: "))
                    nr = self.g.get_cost(x, y)
                    print("The cost is: ", nr)
                elif opp == 8:
                    nr = 0
                    v = int(input("The vertex is: "))
                    for i in self.g.parse_nin(v):
                        nr = nr+1
                    print("The in degree is: ", nr)
                elif opp == 9:
                    nr = 0
                    v = int(input("The vertex is: "))
                    for i in self.g.parse_nin(v):
                        nr = nr + 1
                    print("The out degree is: ", nr)
                elif opp == 10:
                    nr = 0
                    for i in self.g.parse_vertices():
                        nr = nr + 1
                    print("The number of vertices is: ", nr)
                elif opp == 11:
                    print("The number of edges is: ", self.g.edge_count)



                elif opp == 12:
                    print("The vertices are: ")
                    for x in self.g.parse_vertices():
                        print(x)
                elif opp == 13:
                    for x in self.g.parse_vertices():
                        for y in self.g.parse_nin(x):
                            print("(",y,",",x,")")

                elif opp == 14:
                    print("Outbound:")
                    for x in self.g.parse_vertices():
                        print(f"{x}:", end='')
                        for y in self.g.parse_nout(x):
                            print(f" {y}", end='')
                        print()
                elif opp == 15:
                    print("Inbound:")
                    for x in self.g.parse_vertices():
                        print(f"{x}:", end='')
                        for y in self.g.parse_nin(x):
                            print(f" {y}", end='')
                        print()
                elif opp == 16:
                    print("Outbound:")
                    for x in self.g.parse_vertices():
                        print(f"{x}:", end='')
                        for y in self.g.parse_nout(x):
                            print(f" {y}", end='')
                        print()
                    print("Inbound:")
                    for x in self.g.parse_vertices():
                        print(f"{x}:", end='')
                        for y in self.g.parse_nin(x):
                            print(f" {y}", end='')
                        print()
                elif opp == 17:
                    self.copy = self.g.copy()
                    print("Graph copied.")
                elif opp == 18:
                    try:
                        file_name = input("Enter the file name: ")
                        with open(file_name, 'r') as file:
                            n, m = map(int, file.readline().split())

                            mm.append(0)
                            #print(mm[0])
                            if m > n*(n-1):
                                print("Number of edges exceeds the maximum possible for the given number of vertices")
                                break
                            self.g = Graph(n)
                            for i in range(m):
                                x, y, c = map(int, file.readline().split())
                                try:
                                    self.g.add_edge(x, y, c,mm)
                                    #to make the graph undirected
                                    #self.g.add_edge(y, x, c, mm)
                                except ValueError as ve:
                                    print(ve)
                                    return
                    except FileNotFoundError:
                        print("File not found.")
                        return
                    print("Graph read from file.")

                elif opp == 19:
                    nr = 0
                    for i in self.g.parse_vertices():
                        nr = nr + 1
                    nr2 = 0
                    for x in self.g.parse_vertices():
                        for y in self.g.parse_nout(x):
                            if y in self.g.parse_vertices():
                                nr2 = nr2 + 1
                    for x in self.g.parse_vertices():
                        for y in self.g.parse_nin(x):
                            if y in self.g.parse_vertices():
                                nr2 = nr2 + 1
                    vertices = []
                    file_name = input("Enter the filename: ")
                    with open(file_name, 'w') as file:
                        file.write(str(nr) + " " + str(nr2) + "\n")
                        for v in self.g.parse_vertices():
                            vertices.append(v)
                        for v in self.g.parse_vertices():
                            for u in self.g.parse_nout(v):
                                file.write(str(v) + " " + str(u) + " " + str(self.g.get_cost(v, u)) + "\n")
                                if v in vertices:
                                    vertices.remove(v)
                                if u in vertices:
                                    vertices.remove(u)
                        for v in vertices:
                            file.write(str(v) + "\n")
                    print("Graph written to file.")
                elif opp == 20:
                    self.g.bfs()
                elif opp == 21:
                    x = int(input("Start vertex:\n"))
                    y = int(input("Finish vertex:\n"))

                    rez = self.g.min_cost_dp(x, y)
                    if rez == None:
                        print("It doesn't exist!")
                    else:
                        print("The minimum cost is:", rez)
                elif opp == 22:
                    nr = 0
                    for i in self.g.parse_vertices():
                        nr = nr + 1
                    n = nr
                    m = self.g.edge_count
                    s = self.g.kruskal(n, m)
                    print("The sum is:", s)
                elif opp == 23:
                    x = int(input("Start vertex:\n"))
                    y = int(input("Finish vertex:\n"))

                    rez = self.g.min_cost_exponential(x, y)
                    if rez == None:
                        print("It doesn't exist!")
                    else:
                        print("The minimum cost is:", rez)
                elif opp == 0:
                    break
                else:
                    print("Invalid option!")
            except ValueError as ve:
                print(ve)

    @staticmethod
    def menu():
        print("------Menu------  ")
        print("--GENERATE GRAPH--  ")
        print("1 - Generate a random graph  ")
        print("--GRAPH OPERATIONS--  ")
        print("2 - Add a vertex  ")
        print("3 - Remove a vertex  ")
        print("4 - Add an edge  ")
        print("5 - Remove an edge  ")
        print("6 - Modify the cost of an edge  ")
        print("--GRAPH PROPERTIES--  ")
        print("7 - Print the cost of an edge  ")
        print("8 - Print the in degree of a vertex  ")
        print("9 - Print the out degree of a vertex  ")
        print("10 - Print the number of vertices  ")
        print("11 - Print the number of edges  ")
        print("12 - Print the list of vertices  ")
        print("13 - Print the list of edges  ")
        print("14 - Print the list of outbound neighbours of a vertex  ")
        print("15 - Print the list of inbound neighbours of a vertex  ")
        print("--READ/WRITE/COPY/PRINT GRAPH--  ")
        print("16 - Print the graph  ")
        print("17 - Copy the graph  ")
        print("18 - Read the graph from a file  ")
        print("19 - Write the graph from a file  ")
        print("--FUNCTIONALITIES--")
        print("20 - Connected components using BFS")
        print("21 - Minimum cost walk")
        print("22 - Kruskal")
      #  print("23 - The minimum cost walk - exponential")
        print("0 - Exit  ")






