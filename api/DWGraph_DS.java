package api;

import java.util.*;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> _nodesList;
    private HashMap<Integer, HashMap<Integer, edge_data>> _out;
    private HashMap<Integer, HashMap<Integer, node_data>> _in;
    private int _nodeSize;
    private int _edgeSize;
    private int _MC;

    public DWGraph_DS() {
        _nodesList = new HashMap<>();
        _in = new HashMap<>();
        _out = new HashMap<>();
        _edgeSize = 0;
        _nodeSize = 0;
        _MC = 0;
    }

    @Override
    public node_data getNode(int key) {
        if (!_nodesList.containsKey(key))
            return null;
        return _nodesList.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if (src == dest || getNode(src) == null || getNode(dest) == null)
            return null;
        if (!_out.containsKey(src))
            return null;
        return _out.get(src).get(dest);
    }

    @Override
    public void addNode(node_data n) {
        if (n == null)
            return;
        if (getNode(n.getKey()) != null)
            return;
        // HashMap<Integer, node_data> in = new HashMap<Integer, node_data>();
        //HashMap<Integer, edge_data> out = new HashMap<Integer, edge_data>();
        _in.put(n.getKey(), new HashMap<Integer, node_data>());
        _out.put(n.getKey(), new HashMap<Integer, edge_data>());
        _nodesList.put(n.getKey(), n);
        _nodeSize++;
        _MC++;


    }

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
            return;
        }

        edge_data e = new EdgeData(src, dest, w);
        _out.get(src).put(dest, e);
        _in.get(dest).put(src, getNode(src));
        _edgeSize++;
        _MC++;
    }

    @Override
    public Collection<node_data> getV() {
        Collection<node_data> vertices = _nodesList.values();
        return vertices;
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        if (getNode(node_id) == null)
            return null;
        Collection<edge_data> edges = _out.get(node_id).values();
        return edges;
    }

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

    @Override
    public edge_data removeEdge(int src, int dest) {
        if (getEdge(src, dest) == null)
            return null;
        _in.get(dest).remove(src);
        _edgeSize--;
        _MC++;
        return _out.get(src).remove(dest);
    }

    @Override
    public int nodeSize() {
        return _nodesList.size();
    }

    @Override
    public int edgeSize() {
        return _edgeSize;
    }

    @Override
    public int getMC() {
        return _MC;
    }


    private class EdgeData implements edge_data {
        private int _src, _dest;
        private double _weight;
        private String _info;
        private int _tag;

        EdgeData(int src, int dest, double w) {
            _dest = dest;
            _src = src;
            _weight = w;
            _tag = 0;
            _info = "";
        }

        @Override
        public int getSrc() {
            return _src;
        }

        @Override
        public int getDest() {
            return _dest;
        }

        @Override
        public double getWeight() {
            return _weight;
        }

        @Override
        public String getInfo() {
            return _info;
        }

        @Override
        public void setInfo(String s) {
            _info = s;
        }

        @Override
        public int getTag() {
            return _tag;
        }

        @Override
        public void setTag(int t) {
            _tag = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EdgeData edge = (EdgeData) o;
            return _src == edge._src &&
                    _dest == edge._dest &&
                    _weight == edge._weight;

        }

        @Override
        public String toString() {
            return "" + _src + " -> " + _dest;
        }
    }

    /**
     * method to check if 2 graphs are equal
     *
     * @param o
     * @return
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
}