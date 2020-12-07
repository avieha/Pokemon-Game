package api;

public class NodeData implements node_data {

    private int _key;
    private geo_location _location;
    private double _weight;
    private String _info;
    private int _tag;
    private static int index = 0;

    NodeData(int key) {
        _key = key;
        _weight = 0;
        _tag = 0;
        _info = "";
        _location = new GeoLocation();
    }

    NodeData() {
        _key = index++;
        _weight = 0;
        _tag = 0;
        _info = "";
        _location = new GeoLocation();
    }

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
        geo_location location = new GeoLocation(p.x(), p.y(), p.z());
        _location = location;
        return;
    }

    @Override
    public double getWeight() {
        return _weight;
    }

    @Override
    public void setWeight(double w) {
        _weight = w;
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


    public static class GeoLocation implements geo_location {

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
