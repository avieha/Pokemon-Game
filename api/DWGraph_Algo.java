package api;

import java.util.Collection;
import java.util.List;

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
        return false;
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
