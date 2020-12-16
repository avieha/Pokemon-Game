#                                                                    directed Weighted graph

 This project is divided to two parts. in the first part we implemnted a directed weighted graph, and in the second part 
 we use the directed weighted graph to tun a "pokemon" game

## Table of contents
* [General info](#general-info)
* [Data structure](#data-strucure)
* [Algorithms](#algorithms)
* [Tests](#tests)
* [Launch](#launch)
* [Sources](#sources)

## General info
Directed Wighted Graph:
a diredted weighted grap(dwg) is a graph made from nodes - |V| and edges = |E| 
the classes which implemnt the dwg are int the folder "API" and they are:
* NodeData- this class implemnts the interface of "node_data" and it represent the nodes in the graph.
every node have a key, weight, info, tag and a location which is an inner class which implements the interface of "geo_location" wich represent a location of 3D.

* DWGraph_DS- this class represennt the graph and it has an inner class "EdgeData" which is the interface of "edge_data" and represent the edges whos connect the nodes
every edge have a positive weight, a source- the node the edge came from-, a destination- the node the edge go to-, info and tag.
in the DWGraph_DS you can find all the simple methods of the graph such as adding a node or an edge, reamoving a node or an edge, connecting nodes, get a list of the nodes 
or a list of all the neighboors of a specific node, and some more methods.

* DWGraph_Algo- this class allows you the make some manipulations over the graph. you can init a directed weighted graph graph run some functions like:
-copy- make a deep copy of the graph
-isconnected- check if the graph is strongly connected( you can go from every node to every node) using a BFs algorithm and the reversing alll the graph and run agaim the BfS from the same node
-shortestpathdist- return the shortest path (by the edges weight) between 2 nodes.
-shortestpath- return a list of nodes of the shortest path 
-save- save a file of the graph in a Json form.. in this method we used the GSon library of google
-load- load a saved Json file and create anew graph.  in this method we used the GSon library of google

	
## Data structure
We decide to write this project using 3 Hasmaps. 1 for the nodes and 2 for the edges. the first hasmap of the edges is used to keep all the edges that come out
from every node in the graph
and the third hashmap is to keep for every node the edges the came to him. The reson we needed 2 hashamp for the edges is that you need to know for every node 
to which nodes is connected and which nodes are connected to him. In that way when you remov a node you acan also remove all the edges which were connected to
him and all the nodes he were connected to. the others reasons we used hashmap are becuase it allows you to get every node in o(1), it allows you to contain many nodes without initialize a a size at first,
and beacuase it also have the function value which let you convert the hasmap into a list with just a pointer in o(1)

## Algorithms
* Kosaraju's- Following is Kosaraju’s BFS based simple algorithm
that does two BFS traversals of graph:
1) Initialize all vertices as not visited.
2) Do a BFS traversal of graph starting from 
   any arbitrary vertex v. If BFS traversal 
   doesn’t visit all vertices, then return false.
3) Reverse all edges (or find transpose or reverse 
   of graph)
4) Mark all vertices as not visited in reversed graph.
5) Again do a BFS traversal of reversed graph starting
   from same vertex v (Same as step 2). If BFS traversal
   doesn’t visit all vertices, then return false. 
   Otherwise, return true.
  Time complexity:
 Time complexity of above implementation is same as Breadth First Search which is O(V+E).

* Dijikstra- in this algorithm you use a priority queue with a comperator wich compare the weight of the edges and mantain the node with the shortest ptha in the top of the queue so at first you add the source node to the queue and then ron over a while loop antil the queue is empty. inside the loop you mark the top element as visited you remove him from queue and add all his not visited neighbors to the queue. Every node that get into the queue have the sum of the edges from source node to him in his Tag value.
Thanks to the comparator the node with the minor sum will be in the top of the queue, that permit you to check if there is a shortest path which passes trough a node which is allready inside the queue and still didnt get marked as not visited,
so after the queue get empty you check the destination node if his Tag was changes it mean this is the shortes path if he didnt get changed it is mean the src and dst node are not connected
in the "shortestpath" I added an hashmap that when you add a neighbor you also put in the hashmap the node that was before the neighboore which were added
that way you can check in the hashmap wich node was before the destination node and which was before this node and you can go like this until you get the src node so 
that way you actually get the list of nodes which are the shortest path!
  
  
## Tests
in the folder "tests" you can find 2 Junit classes the test all the methods in the DWGraph_Algo and all the methods in DWGraph_DS
the tests checks simple methods like adding adding or removing a node to the complex methods like isconnected or shortestpath
  
## Launch
To run this project you need at first to Pull the file from the Git repository with the command :

$ git clone https://github.com/avieha/ex2.git 

then you have several options

## Sources
In the algorithm of Dijikstra i get help from a youtube video: https://www.youtube.com/watch?v=FSm1zybd0Tk 

info about Kosaraju's algorithm https://en.wikipedia.org/wiki/Strongly_connected_component
https://www.geeksforgeeks.org/check-given-directed-graph-strongly-connected-set-2-kosaraju-using-bfs/?ref=rp

```
  
