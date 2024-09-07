# Data-Structures-and-Algorithms
This repository contains Java code developed as part of a university project for implementing and managing disjoint sets (also known as union-find data structures) and multisets.

# mp1 Key Components

- The DisjointSetElement Interface specifies how references and related integers for elements within a disjoint set should be managed.
- The DisjointSets Interface describes how to create new sets, discover representatives, conduct union operations, and get members of sets and current representatives for collections of disjoint sets.
- The MyIntLinkedListDisjointSetElement class implements DisjointSetElement for integer elements using linked lists to manage internal pointers and size attributes for disjoint sets.
- Multiset Interface: Offers techniques for handling multisets, in which elements may appear more than once. includes iterating over elements and procedures for adding, removing, and counting occurrences.
