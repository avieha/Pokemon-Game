package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Ex2 implements Runnable {

    private static MyFrame _window;
    private static Arena _arena;

    public static void main(String[] args) {
        Thread client = new Thread(new Ex2());
        client.start();
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
        directed_weighted_graph gg = g0.getGraph();
        init(game, gg);
        game.startGame();
        int ind = 0;
        long dt = 100;
        while (game.isRunning()) {
            moveAgents(game, gg);
            try {
                _window.repaint();
                Thread.sleep(dt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();
        System.out.println(res);
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

        String lg = game.getAgents();// returns the current status of the agents
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _arena.setAgents(log);
        //ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs, gg);
        _arena.setPokemons(ffs);
        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i);
           // System.out.println("agent: "+ag);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
                if (dest == -1) {
                    dest = nextNodevalue(game, gg, src);
                    game.chooseNextEdge(ag.getID(), dest);
                    System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
                }game.move();
            }
        }

    /**
     * a very simple random walk implementation!
     *
     * @param g
     * @param src
     * @return
     */
    private static int nextNodedist(game_service game,directed_weighted_graph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        List<CL_Pokemon>pklist=Arena.json2Pokemons(game.getPokemons(), g);
        dw_graph_algorithms algo=new DWGraph_Algo();
        algo.init(g);
        double temp=0;
        double min=Double.MAX_VALUE;
        int dest=0;
        int bfdest=0;
        int type=0;
        for (CL_Pokemon cl_pokemon : pklist) {
            temp= algo.shortestPathDist(src,cl_pokemon.get_edge().getDest());
            if(temp<min){
                min=temp;
                dest= cl_pokemon.get_edge().getDest();
                bfdest=cl_pokemon.get_edge().getSrc();
                type= cl_pokemon.getType();
            }
        }
        List<node_data> nodelist=algo.shortestPath(src,dest);
        System.out.println("list: "+nodelist);
        if(nodelist.size()>1){
            nodelist.remove(0);
            return nodelist.get(0).getKey();
        }

        else
            return bfdest;
    }
    private static int nextNodevalue(game_service game,directed_weighted_graph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        List<CL_Pokemon>pklist=Arena.json2Pokemons(game.getPokemons(), g);
        double temp=0;
        double max=Double.MIN_VALUE;
        int dest=0;
        int bfdest=0;
        int type=0;
        for (CL_Pokemon cl_pokemon : pklist) {
            temp=cl_pokemon.getValue();
            if(temp>max){
                max=temp;
                dest= cl_pokemon.get_edge().getDest();
                bfdest=cl_pokemon.get_edge().getSrc();
                type= cl_pokemon.getType();
            }
        }
        dw_graph_algorithms algo=new DWGraph_Algo();
        algo.init(g);
        List<node_data> nodelist=algo.shortestPath(src,dest);
        System.out.println("list: "+nodelist);
       if(nodelist.size()>1){
            nodelist.remove(0);
            return nodelist.get(0).getKey();
        }

        else
       return bfdest;
    }

    private void init(game_service game, directed_weighted_graph gg) {
        String info = game.toString();
        String fs = game.getPokemons();
        System.out.println("game info: " + info);
        ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(fs, gg);
        System.out.println("Pokemons: " + cl_fs);
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
            PriorityQueue<CL_Pokemon> q = new PriorityQueue(cl_fs.size(), new Comparator<CL_Pokemon>() {
                @Override
                public int compare(CL_Pokemon o1, CL_Pokemon o2) {
                    if (o1.getValue() > o2.getValue())
                        return -1;
                    if (o1.getValue() < o2.getValue())
                        return 1;
                    return 0;
                }
            });
            for (int i = 0; i < cl_fs.size(); i++) {
                Arena.updateEdge(cl_fs.get(i), gg);
                q.add(cl_fs.get(i));
            }
            for (int a = 0; a < agenum; a++) {
                if (!q.isEmpty()) {
                    CL_Pokemon c = q.remove();
                    int src = c.get_edge().getSrc();
                    int dest = c.get_edge().getDest();
                    if (c.getType() == -1 && dest > src) {
                        game.addAgent(dest);
                        System.out.println(dest + " entered 1");
                        game.chooseNextEdge(a, src);
                    }
                    if (c.getType() == -1 && dest < src) {
                        game.addAgent(src);
                        System.out.println(src + " entered 2");
                        game.chooseNextEdge(a, dest);
                    }
                    if (c.getType() == 1 && dest > src) {
                        game.addAgent(src);
                        System.out.println(src + " entered 3");
                        game.chooseNextEdge(a, dest);
                    }
                    if (c.getType() == 1 && dest < src) {
                        game.addAgent(dest);
                        System.out.println(dest + "entered 4");
                        game.chooseNextEdge(a, src);
                    }
                } else {
                    game.addAgent(a % cl_fs.size());
                    game.chooseNextEdge(a, a % cl_fs.size() + 1);
                }
            }
            List<CL_Agent> agents = Arena.getAgents(game.getAgents(),gg);
            int i=0;
            for (CL_Agent agent : agents) {
                agent.set_curr_fruit(cl_fs.get(i));
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}