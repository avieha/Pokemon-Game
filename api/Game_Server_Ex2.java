package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Game_Server_Ex2 implements game_service{
    private directed_weighted_graph _graph;
    private dw_graph_algorithms _algo_graph;

    Game_Server_Ex2(){
        _graph=new DWGraph_DS();
        _algo_graph=new DWGraph_Algo();
        _algo_graph.init(_graph);
    }

    @Override
    public String getGraph() {
        _algo_graph.save("graph.txt");
        try
        {
//            GsonBuilder builder = new GsonBuilder();
//            builder.registerTypeAdapter(directed_weighted_graph.class, new Graph_Load());
//            Gson gson = builder.create();

            FileReader reader = new FileReader("graph.text");
            return reader.toString();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public directed_weighted_graph getJava_Graph_Not_to_be_used() {
        return null;
    }

    @Override
    public String getPokemons() {
        return null;
    }

    @Override
    public String getAgents() {
        return null;
    }

    @Override
    public boolean addAgent(int start_node) {
        return false;
    }

    @Override
    public long startGame() {
        return 0;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public long stopGame() {
        return 0;
    }

    @Override
    public long chooseNextEdge(int id, int next_node) {
        return 0;
    }

    @Override
    public long timeToEnd() {
        return 0;
    }

    @Override
    public String move() {
        return null;
    }

    @Override
    public boolean login(long id) {
        return false;
    }
}
