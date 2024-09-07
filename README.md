# Data-Structures-and-Algorithms
This repository contains 2 Java programs developed as part of a university project.

# mp1 Key Components

- DisjointSetElement: Interface that specifies how references and related integers for elements within a disjoint set should be managed
- DisjointSets: Interface that describes how to create new sets, discover representatives, conduct union operations, and get members of sets and current representatives for collections of disjoint sets
- MyIntLinkedListDisjointSetElement: class that implements DisjointSetElement for integer elements using linked lists to manage internal pointers and size attributes for disjoint sets
- Multiset Interface: Offers techniques for handling multisets, in which elements may appear more than once. Includes iterating over elements and procedures for adding, removing, and counting occurrences

#mp2 Key Components
- AdjacencyMatrix: Uses a 2D adjacency matrix to represent a graph. permits adding, deleting, and querying edges in addition to verifying if there are edges connecting nodes
- DisjointSets: Implements the Union-Find data structure to manage a collection of disjoint sets. Provides functionalities for union operations, finding set representatives with path compression, and retrieving current sets
- Edge: Represents an edge in a graph with attributes for the start node, end node, and weight. Includes methods for accessing and modifying edge properties, as well as comparing edges
- ForestDisjointSets: implementation of the disjoint-set data structure, utilizing a forest of trees. Supports set creation, union operations, and efficient set retrieval
- GraphEdge: Defines edges in a graph with support for weighted and directed edges. Includes methods to check weight, get connected nodes, determine direction, and compare edges
- GraphNode: Represents nodes in a graph with operations for managing node attributes like distance, predecessor and timing used in graph algorithms. The Node's identity is determined by its label
- KruskalMST: Implements Kruskal's algorithm to find the Minimum Spanning Tree (MST) of an undirected, weighted graph with non-negative weights. Handles sorting of edges and utilizes disjoint-set operations to build the MST

