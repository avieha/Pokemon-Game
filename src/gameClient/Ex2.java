package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Ex2 implements Runnable {

    private static MyFrame _window;
    private static Arena _arena;

    public static void main(String[] args) {
        Thread client = new Thread(new Ex2());
        client.start();
/*        game_service game = Game_Server_Ex2.getServer(0);
//        game.login();
        game.addAgent(1);
        game.startGame();
        String graph = game.getGraph();
        String agents=game.getAgents();
        String pok=game.getPokemons();
        while (game.isRunning()){
            game.chooseNextEdge(0,2);
            game.move();
            JsonObject p = JsonParser.parseString(pok).getAsJsonObject();

//            System.out.println(game.timeToEnd());
        }*/
    }

    @Override
    public void run() {
        int scenario_num = 1;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();
        try {
            PrintWriter pw = new PrintWriter(new File("graph.txt"));
            pw.write(g);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dw_graph_algorithms g0 = new DWGraph_Algo();
        g0.load("graph.txt");
        directed_weighted_graph gg= g0.getGraph();
        init(game,gg);
        game.startGame();
        _window.setTitle("Ex2 - first time try");
        int ind = 0;
        long dt = 100;
        while (game.isRunning()) {
            moveAgents(game, gg);
            try {
                if (ind % 1 == 0) {
                    _window.repaint();
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();
        System.exit(0);
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     *
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _arena.setAgents(log);
        //ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs,gg);
        _arena.setPokemons(ffs);
        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if (dest == -1) {
                dest = nextNode(gg, src);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
        }
    }

    /**
     * a very simple random walk implementation!
     *
     * @param g
     * @param src
     * @return
     */
    private static int nextNode(directed_weighted_graph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int) (Math.random() * s);
        int i = 0;
        while (i < r) {
            itr.next();
            i++;
        }
        ans = itr.next().getDest();
        return ans;
    }

    private void init(game_service game,directed_weighted_graph gg) {
        String info = game.toString();
        String fs = game.getPokemons();
        System.out.println("game info: "+info);
        System.out.println("Pokemons: "+fs);
        ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(fs,gg);
        //System.out.println("cl_fs edge is: "+cl_fs.get(0).get_edge());
        _arena = new Arena();
        _arena.setGraph(gg);
        _arena.setPokemons(cl_fs);
        _window = new MyFrame("First Ex2 try");
        _window.setSize(1000, 700);
        _window.update(_arena);
        _window.setVisible(true);
        try {
            JSONObject line = new JSONObject(info);
            JSONObject gameinfo = line.getJSONObject("GameServer");
            int agenum = gameinfo.getInt("agents");
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), gg);
            }
            for (int a = 0; a < agenum; a++) {
                int ind = a % cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                System.out.println("pokemon is: "+c);
                int nn = c.get_edge().getDest();
                if (c.getType() < 0) {
                    nn = c.get_edge().getSrc();
                }
//                System.out.println("game is: "+game.toString());
                game.addAgent(nn);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}