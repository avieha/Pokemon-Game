package gameClient;

import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

/** this class represents a pokemon object on the game,
 * including info as location,value and specific edge where it
 * stays.
 */
public class CL_Pokemon {

    private edge_data _edge;
    private double _value;
    private int _type;
    private Point3D _pos;
    private double min_dist;
    private int min_ro;

    public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
        _type = t;
        //	_speed = s;
        _value = v;
        set_edge(e);
        _pos = p;
        min_dist = -1;
        min_ro = -1;
    }

    public String toString() {
        return "F:{v=" + _value + ", t=" + _type + ", pos=" + _pos + "}";
    }

    public edge_data get_edge() {
        return _edge;
    }

    public void set_edge(edge_data _edge) {
        this._edge = _edge;
    }

    public Point3D getLocation() {
        return _pos;
    }

    public int getType() {
        return _type;
    }

    public double getValue() {
        return _value;
    }
}