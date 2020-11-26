package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {


    @Override
    public node_data getNode(int key) {
        return null;
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        return null;
    }

    @Override
    public void addNode(node_data n) {

    }

    @Override
    public void connect(int src, int dest, double w) {

    }

    @Override
    public Collection<node_data> getV() {
        return null;
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return null;
    }

    @Override
    public node_data removeNode(int key) {
        return null;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return 0;
    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int getMC() {
        return 0;
    }

    private class NodeData implements node_data{
        private int _key;
        private geo_location _location;
        private double _weight;
        private String _info;
        private int _tag;
        @Override
        public int getKey() {
            return _key;
        }

        @Override
        public geo_location getLocation() {
            return _location;
        }

        @Override
        public void setLocation(geo_location p) {
           _location=p;
        }

        @Override
        public double getWeight() {
            return _weight;
        }

        @Override
        public void setWeight(double w) {
            _weight=w;
        }

        @Override
        public String getInfo() {
            return _info;
        }

        @Override
        public void setInfo(String s) {
              _info=s;
        }

        @Override
        public int getTag() {
            return _tag;
        }

        @Override
        public void setTag(int t) {
            _tag=t;
        }
        private class GeoLocation implements geo_location{
             private double _x,_y,_z;
            GeoLocation(){
                _x=0;
                _y=0;
                _z=0;
            }
             GeoLocation(double x, double y, double z){
                 _x=x;
                 _y=y;
                 _z=z;
             }

            @Override
            public double x() {
                return _x;
            }

            @Override
            public double y() {
                return _y;
            }

            @Override
            public double z() {
                return _z;
            }

            @Override
            public double distance(geo_location g) {
                return Math.sqrt((_x*_x)+(_z*_z)+(_y*_y));
            }
        }
    }
    private class EdgeData implements edge_data{
         private node_data _src,_dest;
         private double _weight;
         private String _info;
         private int _tag;
          EdgeData(node_data src, node_data dest, double w){
              _dest=dest;
              _src=src;
              _weight=w;
              _tag=0;
              _info="";
          }
        @Override
        public int getSrc() {
            return _src.getKey();
        }

        @Override
        public int getDest() {
            return _dest.getKey();
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
              _info=s;
        }

        @Override
        public int getTag() {
            return _tag;
        }

        @Override
        public void setTag(int t) {
            _tag=t;
        }
    }
  /*  private class EdgeLocation implements edge_location{
           private edge_data _edgeloc;
           private double _ratio;
           EdgeLocation(edge_data e){
               _edgeloc=e;
               _ratio=e
           }
        @Override
        public edge_data getEdge() {
            return null;
        }

        @Override
        public double getRatio() {
            return 0;
        }
    }*/
}
