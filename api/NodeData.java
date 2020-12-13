package api;

import java.util.Objects;

/**
 * this is a simple implementation of the class "node_data" its represent nodes in the graph
 */
public class NodeData implements node_data,java.io.Serializable{

    private int _key;
    private geo_location _location;
    private double _weight;
    private String _info;
    private int _tag;
    private static int index=0;

    /**
     * simple constructor with a given key
     * @param key
     */
    public NodeData(int key){
        _key=key;
        _weight=0;
        _tag=0;
        _info="";
        _location=new GeoLocation();
    }

    /**
     * simple empty constructor with a static index
     */
    NodeData(){
        _key=index++;
        _weight=0;
        _tag=0;
        _info="";
        _location=new GeoLocation();
    }

    /**
     * get key method
     * @return key
     */
    @Override
    public int getKey() {
        return _key;
    }

    /**
     * get location method
     * returns the location of the node in the graph
     * @return location
     */
    @Override
    public geo_location getLocation() {
        return _location;
    }

    /**
     * set location
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        geo_location location=new GeoLocation(p.x(),p.y(),p.z());
        _location=location;
        return;
    }

    /**
     * get weight method
     * @return weight
     */
    @Override
    public double getWeight() {
        return _weight;
    }

    /**
     * set weight method
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        _weight=w;
    }

    /**
     * get info method
     * good for algorithms uses
     * @return
     */
    @Override
    public String getInfo() {
        return _info;
    }

    /**
     * set info method
     * @param s
     */
    @Override
    public void setInfo(String s) {
        _info=s;
    }

    /**
     * get tag method
     * good for algorithms uses
     * @return tag
     */
    @Override
    public int getTag() {
        return _tag;
    }

    /**
     * set tag method
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        _tag=t;
    }

    /**
     * inner implementation of "geo_location" class represent the 3D location of every node in the graph
     */
    public static class GeoLocation implements geo_location {

        private double _x,_y,_z;

        /**
         * empty constructor
         */
        GeoLocation(){
            _x=0;
            _y=0;
            _z=0;
        }

        /**
         * consturctor with 3 double values
         * @param x
         * @param y
         * @param z
         */
        GeoLocation(double x, double y, double z){
            _x=x;
            _y=y;
            _z=z;
        }

        /**
         * get the x Dimension of the location
         * @return
         */
        @Override
        public double x() {
            return _x;
        }

        /**
         * return the y dimension of the location
         * @return
         */
        @Override
        public double y() {
            return _y;
        }

        /**
         * return the z dimension of the location
         * @return
         */
        @Override
        public double z() {
            return _z;
        }

        /**
         * method to calculate the distance between to locations
         * @param g
         * @return
         */
        @Override
        public double distance(geo_location g) {
            return Math.sqrt(Math.pow(_x-g.x(),2) + Math.pow(_y-g.y(),2) + Math.pow(_z-g.z(),2));
        }

        /**
         * to string method
         * @return
         */
        @Override
        public String toString(){
            return _x+","+_y+","+_z;
        }
    }

    /**
     *  method to check if  2 locations are equals
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeInfo = (NodeData) o;
        return _key == nodeInfo._key &&
                Integer.compare(nodeInfo._tag, _tag) == 0 &&
                Objects.equals(_info, nodeInfo._info)&&
                _location.x()==nodeInfo._location.x() &&
                _location.y()==nodeInfo._location.y() &&
                _location.z()==nodeInfo._location.z();
    }
    @Override
    public String toString(){
        return ""+_key+"";
    }
}