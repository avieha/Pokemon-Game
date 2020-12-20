package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents GUI class to present the
 * pokemons game on a graph, with countdown & agents scores
 * shown on the screen.
 */
public class MyFrame extends JFrame {

    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private JLabel time = new JLabel();
    private long clock;

    public MyFrame(String a) {
        super(a);
        this.add(time);
    }

    /**
     * this methods initializes and keeps
     * refreshing the frame with current time
     * and graphic game status
     * @param ar - Arena
     * @param time - current time
     */
    public void update(Arena ar, long time) {
        this._ar = ar;
        this.clock = time;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
    }

    @Override
    public void paintComponents(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);
        updateFrame();
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);
        drawClock(g);
    }

    public void paint(Graphics g) {
        Image buffer_image;
        Graphics buffer_graphics;
        // Create a new "canvas"
        buffer_image = createImage(this.getWidth(), this.getHeight());
        buffer_graphics = buffer_image.getGraphics();

        // Draw on the new "canvas"
        paintComponents(buffer_graphics);

        // "Switch" the old "canvas" for the new one
        g.drawImage(buffer_image, 0, 0, this);

    }

    public void drawClock(Graphics g) {
        String t = "Time to end: " + this.clock / 1000;
        this.time = new JLabel(t);
        double x = 0.035 * this.getHeight();
        g.setFont(new Font("", Font.CENTER_BASELINE, (int) x));
        g.setColor(Color.BLACK);
        g.drawString(t, 45, 70);
        List<CL_Agent> rs = _ar.getAgents();
        String[] s = new String[rs.size()];
        int i = 0;
        for (CL_Agent r : rs) {
            s[i] = "Agent " + r.getID() + ": " + r.getValue();
            i++;
        }
        for (i = 0; i < rs.size(); i++) {
            g.drawString(s[i], 45, 90 + (20 * i));
        }
    }

    private void drawInfo(Graphics g) {
        List<String> str = _ar.get_info();
        String dt = "none";
        for (int i = 0; i < str.size(); i++) {
            g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
        }
    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.blue);
            drawNode(n, 5, g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs != null) {
            Iterator<CL_Pokemon> itr = fs.iterator();
            while (itr.hasNext()) {
                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r = 10;
                g.setColor(Color.green);
                if (f.getType() < 0) {
                    g.setColor(Color.orange);
                }
                if (c != null) {
                    geo_location fp = this._w2f.world2frame(c);
                    g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                }
            }
        }
    }

    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        g.setColor(Color.red);
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            if (c != null) {
                geo_location fp = this._w2f.world2frame(c);
                g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                g.drawString(""+rs.get(i).getValue(), (int)fp.x(), (int)fp.y()-4*r);
            }
            i++;
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.setFont(new Font("", Font.BOLD, 16));
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
    }
}