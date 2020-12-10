package gameClient;

import api.*;
import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ex2 implements Runnable {

    private static MyFrame _window;
    private static Arena _arena;
    private int _levelNum;
    public static void main(String[] a) {

        Thread client = new Thread(new Ex2());
        client.start();
    }



    @Override
    public void run() {

    }
    private void init(game_service game) {
        String graph = game.getGraph();
        String pokemons = game.getPokemons();
        dw_graph_algorithms algorGraph=new DWGraph_Algo();
        algorGraph.load(graph);
        //gg.init(g);
        _arena = new Arena();
        _arena.setGraph(algorGraph.getGraph());
        _arena.setPokemons(Arena.json2Pokemons(pokemons));

        _window = new MyFrame("pokemons");
        _window.setSize(1000, 700);
        _window.update(_arena);


        _window.setVisible(true);
        String info = game.toString();

        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            List<CL_Agent> agents=_arena.getAgents();
            for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a), algorGraph.getGraph());}
            for(int a = 0;a<rs;a++) {
                int ind = a%cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
                game.addAgent(nn);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }
    public void set_levelNum(int _levelNum) {
        this._levelNum = _levelNum;
    }
}
