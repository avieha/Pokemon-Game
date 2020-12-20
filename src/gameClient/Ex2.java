package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * this class represents the main program which takes details
 * from the client (id and level number), initializes and run
 * the game using Thread.
 */
public class Ex2 implements Runnable {

    private int scnum = -1;
    private long id = 0;
    private static MyFrame _window;
    private static Arena _arena;
    private HashMap<Integer, Integer> agTopk = new HashMap<>();

    public static void main(String[] args) {
        if (args.length == 0) {
            Ex2 a = new Ex2();
            Login log = new Login(a);
            log.setVisible(true);
        } else {
            Ex2 b = new Ex2();
            if (args[0].length() == 9) {
                b.id = Integer.parseInt(args[0]);
            }
            b.scnum = Integer.parseInt(args[1]);
            Thread client = new Thread(b);
            client.start();
        }
    }

    public void setid(long x) {
        id = x;
    }

    public void setNum(int x) {
        scnum = x;
    }

    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(scnum);
        game.login(id);
        String g = game.getGraph();
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
        long timer = game.timeToEnd() / 1000;
        long dt = 0;
        boolean a = false;
        while (game.isRunning()) {
            moveAgents(game, gg);
            try {
                if (game.timeToEnd() / 1000 >= timer * 0.5) {
                    dt = 120;
                }
                if (game.timeToEnd() / 1000 <= timer * 0.5) {
                    dt = 80;
                }
                _window.update(_arena, game.timeToEnd());
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
     * in case the agent is on a node the next destination (next edge) is chosen using
     * the nextNodedist function below, according to a dynamic algorithm.
     *
     * @param game
     * @param gg
     * @param
     */
    public void moveAgents(game_service game, directed_weighted_graph gg) {
        String lg = game.getAgents();// returns the current status of the agents
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _arena.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs, gg);
        _arena.setPokemons(ffs);
        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            if (dest == -1) {
                dest = nextNodedist(game, gg, id, src);
                game.chooseNextEdge(ag.getID(), dest);
            }
        }
        game.move();
    }

    /**
     * a walk implementation, finding the closest pokemon to each agent
     * and calculating the best itinerary to him.
     *
     * @param g
     * @param src
     * @return closest pokemon's node id.
     */
    public int nextNodedist(game_service game, directed_weighted_graph g, int id, int src) {
        List<CL_Pokemon> pklist = Arena.json2Pokemons(game.getPokemons(), g);
        List<CL_Agent> aglist = Arena.getAgents(game.getAgents(), g);
        dw_graph_algorithms algo = new DWGraph_Algo();
        algo.init(g);
        double temp = 0;
        double min = Double.MAX_VALUE;
        int dest = 0;
        int bfdest = 0;
        for (CL_Pokemon cl_pokemon : pklist) {
            boolean a = true;
            for (CL_Agent cl_agent : aglist) {
                if (agTopk.get(cl_agent.getID()) == cl_pokemon.get_edge().getDest() && cl_agent.getID() != id) {
                    a = false;
                    break;
                }
            }
            if (a) {
                temp = algo.shortestPathDist(src, cl_pokemon.get_edge().getDest());
                if (temp < min) {
                    min = temp;
                    dest = cl_pokemon.get_edge().getDest();
                    bfdest = cl_pokemon.get_edge().getSrc();
                }
            }
        }
        agTopk.put(id, dest);
        List<node_data> nodelist = algo.shortestPath(src, dest);
        if (nodelist.size() > 1) {
            nodelist.remove(0);
            return nodelist.get(0).getKey();
        } else
            return bfdest;
    }

    public void init(game_service game, directed_weighted_graph gg) {
        String info = game.toString();
        String fs = game.getPokemons();
        System.out.println("game info: " + info);
        ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(fs, gg);
        _arena = new Arena();
        _arena.setGraph(gg);
        _arena.setPokemons(cl_fs);
        _window = new MyFrame("The Pokemons Game - Level " + scnum + ", ID: " + id);
        _window.setSize(700, 500);
        _window.update(_arena, game.timeToEnd());
        _window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                        game.chooseNextEdge(a, src);
                        agTopk.put(a, src);
                    }
                    if (c.getType() == -1 && dest < src) {
                        game.addAgent(src);
                        game.chooseNextEdge(a, dest);
                        agTopk.put(a, dest);
                    }
                    if (c.getType() == 1 && dest > src) {
                        game.addAgent(src);
                        game.chooseNextEdge(a, dest);
                        agTopk.put(a, dest);
                    }
                    if (c.getType() == 1 && dest < src) {
                        game.addAgent(dest);
                        game.chooseNextEdge(a, src);
                        agTopk.put(a, src);
                    }
                } else {
                    game.addAgent(a % cl_fs.size());
                    game.chooseNextEdge(a, a % cl_fs.size() + 1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}