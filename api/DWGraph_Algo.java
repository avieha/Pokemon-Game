package api;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph _graph;

    @Override
    public void init(directed_weighted_graph g) {
     _graph=g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return _graph;
    }

    @Override
    public directed_weighted_graph copy(){
    directed_weighted_graph copy=new DWGraph_DS();
        Collection<node_data>nodes=_graph.getV();
        for (node_data node : nodes) {
            node_data copyNode=new NodeData(node.getKey());
            copyNode.setLocation(node.getLocation());
            copy.addNode(copyNode);
        }
        for (node_data node : nodes) {
            Collection<edge_data>neiNodes=_graph.getE(node.getKey());
            for (edge_data neiNode : neiNodes) {
             copy.connect(neiNode.getSrc(),neiNode.getDest(), neiNode.getWeight());
            }
        }
        return copy;
    }

    @Override
    public boolean isConnected() {
        if(this._graph==null)
            return true;
        if(this._graph.nodeSize()==0)
            return true;

        Collection<node_data>t= this._graph.getV();
        for (node_data recentNode : t) {
            Collection<node_data>secondt= this._graph.getV();
            for (node_data x : secondt) {
                this._graph.getNode(x.getKey()).setTag(0);
            }

            Queue<node_data> q=new LinkedList<node_data>();
            q.add(recentNode);
            q.peek().setTag(1);
            while (!q.isEmpty()) {
                Collection<edge_data>edges = this._graph.getE(q.peek().getKey());
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
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
