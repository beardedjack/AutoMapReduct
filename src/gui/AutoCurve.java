package gui;

import core.Automaton;
import core.BasicOperations;
import core.mAdic;
import core.mAdicSet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AutoCurve extends JPanel{
    private float STROKE_WIDTH = 2.0f;
    private static final Dimension APP_SIZE = new Dimension(800, 600);
    private LinkedHashMap<Integer, Integer> Points;
    //private List<Points> al = new ArrayList<Points>();
    
    public AutoCurve(Automaton automaton) {
        double x = 0, y = 0;
        Integer[] xx;
        Integer[] yy;
        Integer langLength = 0;
        Integer size = 0;
        Set<Map.Entry<Integer, mAdic>> set1 = automaton.input.getMAdicSet();
        double[] xxx = new double[set1.size()]; 
        for (Map.Entry<Integer, mAdic> me1 : set1) {
            xx = me1.getValue().getDigits();
            langLength = me1.getValue().getCapacity() + 1;
            x = 0;
            for (Integer i = 0; i < xx.length; i++) {
                x = x + (double)(xx[i] + 1) / (double)BasicOperations.getPow(langLength, i);  
            }
            xxx[me1.getKey()] = x;
        }
        //for (Integer j=1; j<=automaton.getConditionsCount(); j++) {
        Set<Map.Entry<Integer, mAdic>> set2 = automaton.output.getMAdicSet();
        double[] yyy = new double[set2.size()];
        
        for (Map.Entry<Integer, mAdic> me2 : set2) {
            yy = me2.getValue().getDigits();
            langLength = me2.getValue().getCapacity() + 1;
            y = 0;
            for (Integer i = 0; i < yy.length; i++) {
                y = y + (double)(yy[i] + 1) / (double)BasicOperations.getPow(langLength, i);  
            }
            yyy[me2.getKey()] = y;
        }
        Points = new LinkedHashMap<>();
        Points.put(0, 0);
        for (Integer i = 0; i < set1.size(); i++) {
            Points.put((int)xxx[i], (int)yyy[i]);
            //points.add(new Point2D.Double(xxx[i], yyy[i]));
        }
        //}
        this.setPreferredSize(APP_SIZE);
        JFrame frame = new JFrame("Геометрическое пространство");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
  
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int width = getWidth();
    int height = getHeight();
    g.setColor(Color.white);
    g.fillRect(0, 0, width, height);
    g.setColor(Color.black);
    Graphics2D g2 = (Graphics2D)g;
    g2.setStroke(new BasicStroke(
            STROKE_WIDTH,
            BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND));
    g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON);
    Set<Map.Entry<Integer, Integer>> set = Points.entrySet();
    Set<Map.Entry<Integer, Integer>> set1 = Points.entrySet();
        Integer prevx = null, prevy = null;
        int x1 = 0 , y1 = 0, x2 = 0, y2 = 0;
        Integer minx = 0, miny = 0, maxx = 0, maxy = 0;
        for (Map.Entry<Integer, Integer> me1 : set1) {
            if (me1.getKey() > maxx) maxx = me1.getKey();
            if (me1.getValue() > maxy) maxy = me1.getValue();
            if (me1.getKey() < minx) minx = me1.getKey();
            if (me1.getValue() < miny) miny = me1.getValue();
        }
        int xMultiplier = (width - 100) / maxx;
        int yMultiplier = (height - 100) / maxy;
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(15, maxy * yMultiplier + 10, width, maxy * yMultiplier + 10);
        g2.setColor(Color.BLUE);
        for (Map.Entry<Integer, Integer> me : set) {
            if (prevx != null) {
                x1 = prevx;
                y1 = prevy;       
                x2 = me.getKey();
                y2 = me.getValue();
                g2.setColor(Color.BLUE);
                STROKE_WIDTH = 2.0f;
                g2.setStroke(new BasicStroke(
                        STROKE_WIDTH, 
                        BasicStroke.CAP_ROUND, 
                        BasicStroke.JOIN_ROUND));
                g2.drawString(me.getValue().toString(), 5,
                            0 - y2 * yMultiplier + maxy * yMultiplier + 10);
                if (me.getKey() > 1) {
                    g2.drawLine(x1 * xMultiplier + 20,
                                0 - y1 * yMultiplier + maxy * yMultiplier + 10,
                                x2 * xMultiplier + 20,
                                0 - y2 * yMultiplier + maxy * yMultiplier + 10);
                }
            }
            g2.setColor(Color.DARK_GRAY);
            STROKE_WIDTH = 0.5f;
            g2.setStroke(new BasicStroke(
                    STROKE_WIDTH, 
                    BasicStroke.CAP_ROUND, 
                    BasicStroke.JOIN_ROUND));
            g2.drawLine(15, 0 - y2 * yMultiplier + maxy * yMultiplier + 10,
                        width, 0 - y2 * yMultiplier + maxy * yMultiplier + 10);
            g2.drawLine(x2 * xMultiplier + 20, 0,
                        x2 * xMultiplier + 20, maxy * yMultiplier + 15);
            g2.drawString(me.getKey().toString(),
                        x2 * xMultiplier + 20, maxy * yMultiplier + 25);
            prevx = x2;
            prevy= y2;
        }
}
  
}
