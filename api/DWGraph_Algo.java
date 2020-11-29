package api;

import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms,java.io.Serializable{
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
        if(this._graph==null)
            return -1;
        if(this._graph.getNode(src)==null||this._graph.getNode(dest)==null)
            return -1;
        if(src==dest)
            return 0;
        PriorityQueue<vertex> q =new PriorityQueue(this._graph.nodeSize(), new Comparator<vertex>() {
            @Override
            public int compare(vertex o1, vertex o2) {
                if(o1._edgeweight> o2._edgeweight)
                    return 1;
                if(o1._edgeweight<o2._edgeweight)
                    return -1;
                return 0;
            }
        });
        Collection<node_data> list= _graph.getV();
        for (node_data node_info : list) {
            node_info.setTag(0);
        }
        vertex ver=new vertex(_graph.getNode(src));
        HashMap<Integer,vertex> verlist=ver.vertohash(_graph.getV());
        verlist.get(src).set_edgeweight(0);
        q.add(ver);
        while (!q.isEmpty()){
            vertex x= verlist.get(q.remove().getkey());
            _graph.getNode(x.getkey()).setTag(1);
            Collection<edge_data> neighbors= _graph.getE(x.getkey());
            for (edge_data neighbor : neighbors) {
                if(_graph.getNode(neighbor.getDest()).getTag()==0)
                {
                    double weightsum=x._edgeweight+_graph.getEdge(x.getkey(),neighbor.getDest()).getWeight();
                    if(weightsum<verlist.get(neighbor.getDest())._edgeweight){
                        verlist.get(neighbor.getDest()).set_edgeweight(weightsum);
                       // PrevNode.put(neighbor,x);
                        verlist.get(neighbor.getDest()).setPrevnode(x.getkey());
                        q.add(verlist.get(neighbor.getDest()));
                    }

                }
            }
        }      if(_graph.getNode(dest).getTag()==0)
            return -1;
        return verlist.get(dest)._edgeweight;


    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        List<node_data> pathlist=new LinkedList<node_data>();
        if(this._graph==null)
            return null;
        if(this._graph.getNode(src)==null||this._graph.getNode(dest)==null)
            return null;
        if(src==dest){
            pathlist.add(_graph.getNode(src));
            return pathlist;}
        if(shortestPathDist(src,dest)==-1)
            return null;
        PriorityQueue<vertex> q =new PriorityQueue(this._graph.nodeSize(), new Comparator<vertex>() {
            @Override
            public int compare(vertex o1, vertex o2) {
                if(o1._edgeweight> o2._edgeweight)
                    return 1;
                if(o1._edgeweight<o2._edgeweight)
                    return -1;
                return 0;
            }
        });
        Collection<node_data> list= _graph.getV();
        for (node_data node_info : list) {
            node_info.setTag(0);
        }
        vertex ver=new vertex(_graph.getNode(src));
        HashMap<Integer,vertex> verlist=ver.vertohash(_graph.getV());
        verlist.get(src).set_edgeweight(0);
        q.add(ver);
        while (!q.isEmpty()){
            vertex x= verlist.get(q.remove().getkey());
            _graph.getNode(x.getkey()).setTag(1);
            Collection<edge_data> neighbors= _graph.getE(x.getkey());
            for (edge_data neighbor : neighbors) {
                if(_graph.getNode(neighbor.getDest()).getTag()==0)
                {
                    double weightsum=x._edgeweight+_graph.getEdge(x.getkey(),neighbor.getDest()).getWeight();
                    if(weightsum<verlist.get(neighbor.getDest())._edgeweight){
                        verlist.get(neighbor.getDest()).set_edgeweight(weightsum);
                        verlist.get(neighbor.getDest()).setPrevnode(x.getkey());
                        q.add(verlist.get(neighbor.getDest()));
                    }

                }
            }
        } int temp=dest;
        pathlist.add(_graph.getNode(dest));
        while(temp!=src){
            pathlist.add(_graph.getNode(verlist.get(temp).prevnode));
            temp=verlist.get(temp).prevnode;
        }
        Collections.reverse(pathlist);
        return pathlist;
    }

    @Override
    public boolean save(String file) {
        try {

            // Saving of object in a file
            FileOutputStream File = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream
                    (File);

            // Method for serialization of object
            out.writeObject(_graph);

            out.close();
            File.close();
            return true;
        }

        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file) {
        try {

            // Reading the object from a file
            FileInputStream File = new FileInputStream
                    (file);
            ObjectInputStream in = new ObjectInputStream(File);

            // Method for deserialization of object
            this._graph = (directed_weighted_graph)in.readObject();

            in.close();
            File.close();
            return true;

            // System.out.println("z = " + object1.z);
        }

        catch (IOException ex) {
            return false;
        }

        catch (ClassNotFoundException ex) {
            return false;
        }
    }

    private static class vertex{
        node_data _node;
        double _edgeweight;
        int prevnode;
        vertex(node_data node){
            _node=node;
            _edgeweight=Integer.MAX_VALUE;
            prevnode=-1;
        }
        public int getkey(){
            return _node.getKey();
        }
        public void set_edgeweight(double w){
            _edgeweight=w;
        }

        public void setPrevnode(int prevnode) {
            this.prevnode = prevnode;
        }

        public HashMap<Integer,vertex> vertohash(Collection<node_data> t){
            HashMap<Integer,vertex> list=new HashMap<>();
            for (node_data node_data : t) {
                vertex ver=new vertex(node_data);
                list.put(ver.getkey(),ver);
            }
            return list;
        }

    }
}
