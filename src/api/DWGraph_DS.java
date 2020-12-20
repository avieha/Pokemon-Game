package api;

import java.util.*;

/**
 * this is a the implementation of "directed_weighted_graph" and its reprasent a weighted directed graph
 * with the simple functoons of adding and deleting a node or an edge and geting the size of node and edges
 * implementation with 3 hashmap "nodesList" for the nodes "out" for ol the edges goes out from every node
 * and "in" for all the edges goes in to every node
 * more info in Readme
 */
public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> _nodesList;
    private HashMap<Integer, HashMap<Integer, edge_data>> _out;
    private HashMap<Integer, HashMap<Integer, node_data>> _in;
    private int _nodeSize;
    private int _edgeSize;
    private int _MC;

    /**
     * empty constructor
     */
    public DWGraph_DS() {
        _nodesList = new HashMap<>();
        _in = new HashMap<>();
        _out = new HashMap<>();
        _edgeSize = 0;
        _nodeSize = 0;
        _MC = 0;
    }
    /**
     * method to get a node from graph
     *
     * @param key - the node_id
     * @return
     */
    @Override
    public node_data getNode(int key) {
        if (!_nodesList.containsKey(key))
            return null;
        return _nodesList.get(key);
    }

    /**
     * method to get an edge from graph
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if (src == dest || getNode(src) == null || getNode(dest) == null)
            return null;
        if (!_out.containsKey(src))
            return null;
        return _out.get(src).get(dest);
    }

    /**
     * method to add a node to graph
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (n == null)
            return;
        if (getNode(n.getKey()) != null)
            return;
        _in.put(n.getKey(), new HashMap<Integer, node_data>());
        _out.put(n.getKey(), new HashMap<Integer, edge_data>());
        _nodesList.put(n.getKey(), n);
        _nodeSize++;
        _MC++;


    }

    /**
     * method to connect betwenn nodes
     * the method add the edge to the out hashmap in src key
     * and add the edge to in hasmap in dest key
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (getNode(src) == null || getNode(dest) == null || src == dest || w < 0)
            return;
        if (getEdge(src, dest) != null) {
            if (getEdge(src, dest).getWeight() == w)
                return;
            edge_data e = new EdgeData(src, dest, w);
            _out.get(src).put(dest, e);
            _in.get(dest).put(src, getNode(src));
            _MC++;
            return;
        }
        edge_data e = new EdgeData(src, dest, w);
        _out.get(src).put(dest, e);
        _in.get(dest).put(src, getNode(src));
        _edgeSize++;
        _MC++;
    }

    /**
     * method to get a collection of all node in the graph
     *
     * @return
     */
    @Override
    public Collection<node_data> getV() {
        Collection<node_data> vertices = _nodesList.values();
        return vertices;
    }

    /**
     * method to get all edges goes out from  a specific node
     *
     * @param node_id
     * @return
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (getNode(node_id) == null)
            return null;
        Collection<edge_data> edges = _out.get(node_id).values();
        return edges;
    }
    /**
     * method for removing a node
     * also remove all edges is connected to
     * remove all edges in the out hashmap and in th in hashmap
     *
     * @param key
     * @return
     */
    @Override
    public node_data removeNode(int key) {
        if (!_nodesList.containsKey(key))
            return null;
        Collection<node_data> inners = _in.get(key).values();
        for (node_data node_data : inners) {
            _out.get(node_data.getKey()).remove(key);
            _edgeSize--;
            _MC++;
        }
        Collection<edge_data> outers = _out.get(key).values();
        for (edge_data outer : outers) {
            _in.get(outer.getDest()).remove(key);
        }
        _edgeSize -= _out.get(key).size();
        _MC += _out.get(key).size();
        _out.remove(key);
        _nodeSize--;
        _MC++;
        return _nodesList.remove(key);
    }
    /**
     * method for removing a node
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (getEdge(src, dest) == null)
            return null;
        _in.get(dest).remove(src);
        _edgeSize--;
        _MC++;
        return _out.get(src).remove(dest);
    }

    /**
     * return the size of all nodes in the graph
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return _nodesList.size();
    }

    /**
     * return the size of all edges in graph
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return _edgeSize;
    }

    /**
     * return the number of all the action which have been made in the graph
     * every node or edge which were added or removed from  the graph increase the mc by one
     *
     * @return
     */
    @Override
    public int getMC() {
        return _MC;
    }
    /**
     * method to check if 2 graphs are equal
     *
     * @param o - dwgraph_ds
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWGraph_DS wGraph_ds = (DWGraph_DS) o;
        return _nodeSize == wGraph_ds._nodeSize &&
                _edgeSize == wGraph_ds._edgeSize &&
                Objects.equals(_nodesList, wGraph_ds._nodesList) &&
                Objects.equals(_in, wGraph_ds._in) &&
                Objects.equals(_out, wGraph_ds._out);
    }

    /**
     * to string method
     *
     * @return string
     */
    @Override
    public String toString() {
        Collection<node_data> x = _nodesList.values();
        for (node_data node_data : x) {
            System.out.print("{ node: " + node_data.getKey() + " neighboors:[");
            Collection<edge_data> y = _out.get(node_data.getKey()).values();
            int size = y.size();
            if (size == 0)
                System.out.println(" ] }");
            else
                for (edge_data edge_data : y) {
                    if (size == 1)
                        System.out.println(edge_data.getDest() + " ] }");
                    else
                        System.out.print(edge_data.getDest() + ",");
                    size--;
                }
        }
        return "";
    }

    /**
     * inner class which implements "edge_data". this class represent all the edge of the graph
     * and it contain values such as edge weight, detination ,and soure etc.
     */
    private class EdgeData implements edge_data {
        private int _src, _dest;
        private double _weight;
        private String _info;
        private int _tag;

        /**
         * constructor
         *
         * @param src
         * @param dest
         * @param w
         */
        EdgeData(int src, int dest, double w) {
            _dest = dest;
            _src = src;
            _weight = w;
            _tag = 0;
            _info = "";
        }

        /**
         * methot to get source of the edge
         *
         * @return src
         */
        @Override
        public int getSrc() {
            return _src;
        }

        /**
         * method to get destination of the edge
         *
         * @return dest
         */
        @Override
        public int getDest() {
            return _dest;
        }

        /**
         * method to get the weight of the edge
         *
         * @return
         */
        @Override
        public double getWeight() {
            return _weight;
        }

        /**
         * get info method
         *
         * @return
         */
        @Override
        public String getInfo() {
            return _info;
        }

        /**
         * set info method
         *
         * @param s
         */
        @Override
        public void setInfo(String s) {
            _info = s;
        }

        /**
         * get tag method. useful for algorithms
         *
         * @return
         */
        @Override
        public int getTag() {
            return _tag;
        }

        /**
         * set tag method
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(int t) {
            _tag = t;
        }
        /**
         * method which check if 2 edges are equal
         *
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EdgeData edge = (EdgeData) o;
            return _src == edge._src &&
                    _dest == edge._dest &&
                    _weight == edge._weight;
        }

        /**
         * to string method
         *
         * @return
         */
        @Override
        public String toString() {
            return "" + _src + " -> " + _dest;
        }
    }
}