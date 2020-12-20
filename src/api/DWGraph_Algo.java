package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.*;

/**
 * this class implements "dw_graph_algorithms" and its allows to take a DWGraph and make over it some manipulations
 * like:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected(); // strongly (all ordered pais connected)
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file); // JSON file
 * 6. Load(file); // JSON file
 */
public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph _graph;

    /**
     * this method init the DWGraph_algo over the received DWGraph so we can run all the function of this class over the graph
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        _graph = g;
    }

    /**
     * this method return the graph we init
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return _graph;
    }

    /**
     * this method make a deep copy of the graph
     * at first this method create new nodes same as the nodes in the graph and
     * after that it connect the nodes and make the same edges as the old graph
     *
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph copy = new DWGraph_DS();
        Collection<node_data> nodes = _graph.getV();
        for (node_data node : nodes) {
            node_data copyNode = new NodeData(node.getKey());
            copyNode.setLocation(node.getLocation());
            copy.addNode(copyNode);
        }
        for (node_data node : nodes) {
            Collection<edge_data> neiNodes = _graph.getE(node.getKey());
            for (edge_data neiNode : neiNodes) {
                copy.connect(neiNode.getSrc(), neiNode.getDest(), neiNode.getWeight());
            }
        }
        return copy;
    }

    /**
     * this method check if the graph is strongly connected means that you can go from every node in graph to every
     * other node
     * in this method we choose one node and run the BFS algorithm to chek if the graph is connected
     * if the graph is connected we make a new graph that is the inverse of the graph we used for the BFS
     * it means we create the same graph with the same nodes but we reverse all the edges
     * now we run the BFS again and if it is connected it is strongly connected
     * more info in the Readme
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (this._graph == null)
            return true;
        if (this._graph.nodeSize() == 0)
            return true;

        Collection<node_data> t = this._graph.getV();
        directed_weighted_graph reverseGraph = new DWGraph_DS();
        for (node_data node_data : t) {
            reverseGraph.addNode(node_data);
        }
        for (node_data node_data : t) {
            Collection<edge_data> n = this._graph.getE(node_data.getKey());
            for (edge_data edge_data : n) {
                reverseGraph.connect(edge_data.getDest(), edge_data.getSrc(), edge_data.getWeight());
            }
        }
        int secondTime = 0;
        Collection<node_data> secondt = this._graph.getV();
        for (node_data x : secondt) {
            this._graph.getNode(x.getKey()).setTag(0);
        }
        Iterator<node_data> itr = t.iterator();
        node_data recentNode = itr.next();
        secondTime = recentNode.getKey();
        Queue<node_data> q = new LinkedList<node_data>();
        q.add(recentNode);
        if (q.peek() != null)
            q.peek().setTag(1);
        while (!q.isEmpty()) {
            Collection<edge_data> edges = this._graph.getE(q.peek().getKey());
            q.remove();
            for (edge_data edge : edges) {
                if (_graph.getNode(edge.getDest()).getTag() == 0) {
                    _graph.getNode(edge.getDest()).setTag(1);
                    q.add(_graph.getNode(edge.getDest()));
                }
            }
        }
        Collection<node_data> y = this._graph.getV();
        for (node_data node2 : y) {
            if (node2.getTag() == 0)
                return false;
        }
        for (node_data x : secondt) {
            reverseGraph.getNode(x.getKey()).setTag(0);
        }
        recentNode = reverseGraph.getNode(secondTime);
        q.add(recentNode);
        q.peek().setTag(1);
        while (!q.isEmpty()) {
            Collection<edge_data> edges = reverseGraph.getE(q.peek().getKey());
            q.remove();
            for (edge_data edge : edges) {
                if (reverseGraph.getNode(edge.getDest()).getTag() == 0) {
                    reverseGraph.getNode(edge.getDest()).setTag(1);
                    q.add(reverseGraph.getNode(edge.getDest()));
                }
            }
        }
        Collection<node_data> secy = reverseGraph.getV();
        for (node_data node2 : secy) {
            if (node2.getTag() == 0)
                return false;
        }

        return true;
    }

    /**
     * this method return the shortest path between 2 selected nodes
     * we use the dijkstra algorithm and the Vertex class
     * we also use a comparator that compare the path we made from the src node untill
     * this node. this comparator allows  to maintain the node with to shortest path in to top of the
     * priority queue
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (this._graph == null)
            return -1;
        if (this._graph.getNode(src) == null || this._graph.getNode(dest) == null)
            return -1;
        if (src == dest)
            return 0;
        PriorityQueue<vertex> q = new PriorityQueue(this._graph.nodeSize(), new Comparator<vertex>() {
            @Override
            public int compare(vertex o1, vertex o2) {
                if (o1._edgeweight > o2._edgeweight)
                    return 1;
                if (o1._edgeweight < o2._edgeweight)
                    return -1;
                return 0;
            }
        });
        Collection<node_data> list = _graph.getV();
        for (node_data node_info : list) {
            node_info.setTag(0);
        }
        vertex ver = new vertex(_graph.getNode(src));
        HashMap<Integer, vertex> verlist = ver.vertohash(_graph.getV());
        verlist.get(src).set_edgeweight(0);
        q.add(ver);
        while (!q.isEmpty()) {
            vertex x = verlist.get(q.remove().getkey());
            _graph.getNode(x.getkey()).setTag(1);
            Collection<edge_data> neighbors = _graph.getE(x.getkey());
            for (edge_data neighbor : neighbors) {
                if (_graph.getNode(neighbor.getDest()).getTag() == 0) {
                    double weightsum = x._edgeweight + _graph.getEdge(x.getkey(), neighbor.getDest()).getWeight();
                    if (weightsum < verlist.get(neighbor.getDest())._edgeweight) {
                        verlist.get(neighbor.getDest()).set_edgeweight(weightsum);
                        verlist.get(neighbor.getDest()).setPrevnode(x.getkey());
                        q.add(verlist.get(neighbor.getDest()));
                    }

                }
            }
        }
        if (_graph.getNode(dest).getTag() == 0)
            return -1;
        return verlist.get(dest)._edgeweight;


    }

    /**
     * this method return a list of the shortest path between 2 nodes.
     * this method is similar to the method above but in here we keep for every node
     * the node which we came from this allows you to return a list of the shortest path
     * we used for this method an hashmap wich keep for every node the node which was before him
     * in that way we can go to the dest node and check wich node is before him and we can go like this until we get the
     * src node then we juast need to reverse the list and we are done
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        List<node_data> pathlist = new LinkedList<node_data>();
        if (this._graph == null)
            return null;
        if (this._graph.getNode(src) == null || this._graph.getNode(dest) == null)
            return null;
        if (src == dest) {
            pathlist.add(_graph.getNode(src));
            return pathlist;
        }
        if (shortestPathDist(src, dest) == -1)
            return null;
        PriorityQueue<vertex> q = new PriorityQueue(this._graph.nodeSize(), new Comparator<vertex>() {
            @Override
            public int compare(vertex o1, vertex o2) {
                if (o1._edgeweight > o2._edgeweight)
                    return 1;
                if (o1._edgeweight < o2._edgeweight)
                    return -1;
                return 0;
            }
        });
        Collection<node_data> list = _graph.getV();
        for (node_data node_info : list) {
            node_info.setTag(0);
        }
        vertex ver = new vertex(_graph.getNode(src));
        HashMap<Integer, vertex> verlist = ver.vertohash(_graph.getV());
        verlist.get(src).set_edgeweight(0);
        q.add(ver);
        while (!q.isEmpty()) {
            vertex x = verlist.get(q.remove().getkey());
            _graph.getNode(x.getkey()).setTag(1);
            Collection<edge_data> neighbors = _graph.getE(x.getkey());
            for (edge_data neighbor : neighbors) {
                if (_graph.getNode(neighbor.getDest()).getTag() == 0) {
                    double weightsum = x._edgeweight + _graph.getEdge(x.getkey(), neighbor.getDest()).getWeight();
                    if (weightsum < verlist.get(neighbor.getDest())._edgeweight) {
                        verlist.get(neighbor.getDest()).set_edgeweight(weightsum);
                        verlist.get(neighbor.getDest()).setPrevnode(x.getkey());
                        q.add(verlist.get(neighbor.getDest()));
                    }

                }
            }
        }
        int temp = dest;
        pathlist.add(_graph.getNode(dest));
        while (temp != src) {
            pathlist.add(_graph.getNode(verlist.get(temp).prevnode));
            temp = verlist.get(temp).prevnode;
        }
        Collections.reverse(pathlist);
        return pathlist;
    }

    /**
     * method to save a graph in a file in a Json form using the Gson library
     * we made a String of the graph and save it in a file
     *
     * @param file - the file name (may include a relative path).
     * @return
     */
    @Override
    public boolean save(String file) {
        Gson gson = new GsonBuilder().create();
        JsonObject graph = new JsonObject();
        JsonArray graphNodes = new JsonArray();
        JsonArray neigh = new JsonArray();
        Collection<node_data> t = _graph.getV();
        for (node_data node_data : t) {
            Collection<edge_data> edgecolec = _graph.getE(node_data.getKey());
            for (edge_data edge_data : edgecolec) {
                JsonObject Edges = new JsonObject();
                Edges.addProperty("src", edge_data.getSrc());
                Edges.addProperty("w", edge_data.getWeight());
                Edges.addProperty("dest", edge_data.getDest());
                neigh.add(Edges);
            }
            JsonObject nodesList = new JsonObject();
            geo_location geo = node_data.getLocation();
            nodesList.addProperty("pos", geo.toString());
            nodesList.addProperty("id", node_data.getKey());
            graphNodes.add(nodesList);
        }
        graph.add("Edges", neigh);
        graph.add("Nodes", graphNodes);
        String json = gson.toJson(graph);
        //Write JSON to file
        try {
            PrintWriter pw = new PrintWriter(new File(file));
            pw.write(json);
            pw.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * this method load a graph from saved file
     * all the deserializtion of the json object is made in a new class called
     * "graph_Load"
     *
     * @param file - file name of JSON file
     * @return
     */
    @Override
    public boolean load(String file) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class, new Graph_Load());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            this._graph = gson.fromJson(reader, directed_weighted_graph.class);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * inner class that attach a node with a double weight value
     * to make easy the use of the dijkstra algorithm in the methods
     * useful for maintain the shortest path from src to this node- vertex
     * shortest path
     */
    private static class vertex {

        node_data _node;
        double _edgeweight;
        int prevnode;

        /**
         * simple constructor
         *
         * @param node
         */
        vertex(node_data node) {
            _node = node;
            _edgeweight = Integer.MAX_VALUE;
            prevnode = -1;
        }

        /**
         * get key of the node
         *
         * @return
         */
        public int getkey() {
            return _node.getKey();
        }

        /**
         * set weight-
         *
         * @param w
         */
        public void set_edgeweight(double w) {
            _edgeweight = w;
        }

        /**
         * this method is to keep to key of the node you  came from in the graph
         *
         * @param prevnode
         */
        public void setPrevnode(int prevnode) {
            this.prevnode = prevnode;
        }

        /**
         * method that return a collection of the hashmap
         * is useful to get the shortest path list
         *
         * @param t
         * @return
         */
        public HashMap<Integer, vertex> vertohash(Collection<node_data> t) {
            HashMap<Integer, vertex> list = new HashMap<>();
            for (node_data node_data : t) {
                vertex ver = new vertex(node_data);
                list.put(ver.getkey(), ver);
            }
            return list;
        }
    }
}