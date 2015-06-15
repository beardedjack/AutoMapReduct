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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;

// Класс построения геометрического пространства
public class AutoCurve extends JPanel{
    private float STROKE_WIDTH = 2.0f;
    private static final Dimension APP_SIZE = new Dimension(800, 800);
    private Points points;
    private List<Points> al = new ArrayList<>();
    Color[] colors = new Color[10];
    
    public AMRMain frame;
    
    public AutoCurve(Automaton automaton, AMRMain someframe) throws CloneNotSupportedException, FileNotFoundException, IOException {
        this.frame = someframe;
        Automaton geomAutomaton = new Automaton(frame);
        geomAutomaton.loadFromFile(automaton.frame.autoFileName);
        double x = 0, y = 0;
        Integer[] xx;
        Integer[] yy;
        Integer langLength = 0;
        Integer size = 0;
        mAdicSet input = new mAdicSet(geomAutomaton.alphabetsDimention, frame.getK(), frame);
        Set<Map.Entry<Integer, mAdic>> set1 = input.getMAdicSet();
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
        Points points;
        Set<Map.Entry<Integer, mAdic>> set2;
                
        for (Integer j = 1; j <= geomAutomaton.getConditionsCount(); j++) {
            geomAutomaton.setInitialCondition(j);
            mAdicSet output = geomAutomaton.getOutputSet(input);
            set2 = output.getMAdicSet();
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
            
            points = new Points();
            points.addLine(0, 0);
            for (Integer i = 0; i < set1.size(); i++) {
                points.addLine((int)xxx[i], (int)yyy[i]);
            }
            al.add(points);
        }
        this.setPreferredSize(APP_SIZE);
        JFrame gframe = new JFrame("Геометрическое пространство");
        gframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        gframe.getContentPane().add(this);
        gframe.pack();
        gframe.setLocationRelativeTo(null);
        gframe.setVisible(true);

        colors[0] = Color.BLACK;
        colors[1] = Color.BLUE;
        colors[2] = Color.CYAN;
        colors[3] = Color.GREEN;
        colors[4] = Color.MAGENTA;
        colors[5] = Color.ORANGE;
        colors[6] = Color.PINK;
        colors[7] = Color.RED;
        colors[8] = Color.YELLOW;
        colors[9] = Color.DARK_GRAY;
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
    
    Integer minx = 1, miny = 1, maxx = 1, maxy = 1;
    for (Points all : al) {
        Set<Map.Entry<Integer, Integer>> set1 = all.getLines().entrySet();
        for (Map.Entry<Integer, Integer> me1 : set1) {
            if (me1.getKey() > maxx) maxx = me1.getKey();
            if (me1.getValue() > maxy) maxy = me1.getValue();
            if (me1.getKey() < minx) minx = me1.getKey();
            if (me1.getValue() < miny) miny = me1.getValue();
        }
    }
    
    int xMultiplier = (width - 100) / maxx;
    int yMultiplier = (height - 250) / maxy;
    double[] curvedX;    
    double[] curvedY;
    double co;
    Integer ui = 1;
    Integer prevx = null;
    Integer prevy = null;
    double xx1;
    double yy1;
    double xx2;
    double yy2;
    double prevdx, prevdy;
    
    for (Points all : al) {
        Set<Map.Entry<Integer, Integer>> set = all.getLines().entrySet();
        prevx = null;
        prevy = null;
        int x1 = 0 , y1 = 0, x2 = 0, y2 = 0;
        g2.setColor(Color.BLUE);
        
        for (Map.Entry<Integer, Integer> me : set) {
            if (prevx != null) {
                x1 = prevx;
                y1 = prevy;       
                x2 = me.getKey();
                y2 = me.getValue();
                g2.drawString(me.getValue().toString(), 5,
                            0 - y2 * yMultiplier + maxy * yMultiplier + 10 + 150);
                g2.setColor(Color.RED);
                STROKE_WIDTH = 7.0f;
                g2.setStroke(new BasicStroke(
                    STROKE_WIDTH, 
                    BasicStroke.CAP_ROUND, 
                    BasicStroke.JOIN_ROUND));
                g2.drawLine(x2 * xMultiplier + 20,
                        0 - y2 * yMultiplier + maxy * yMultiplier + 10 + 150,
                            x2 * xMultiplier + 20,
                        0 - y2 * yMultiplier + maxy * yMultiplier + 10 + 150);
            }
            g2.setColor(Color.DARK_GRAY);
            STROKE_WIDTH = 0.5f;
            g2.setStroke(new BasicStroke(
                STROKE_WIDTH, 
                BasicStroke.CAP_ROUND, 
                BasicStroke.JOIN_ROUND));
            g2.drawLine(15, 0 - y2 * yMultiplier + maxy * yMultiplier + 10 + 150,
                        width, 0 - y2 * yMultiplier + maxy * yMultiplier + 10 + 150);
            g2.drawLine(x2 * xMultiplier + 20, 0 + 150,
                        x2 * xMultiplier + 20, maxy * yMultiplier + 15 + 150);
            g2.drawString(me.getKey().toString(),
                        x2 * xMultiplier + 20, maxy * yMultiplier + 25 + 150);
            prevx = x2;
            prevy= y2;
        }
        
        curvedX = new double[set.size()];
        curvedY = new double[set.size()];
        co = 1.0;

        for (Map.Entry<Integer, Integer> me : set) {
            curvedX[me.getKey()] = (double) me.getKey();
            curvedY[me.getKey()] = (double) me.getValue();
        }
        
        g2.setColor(colors[ui - 1]);
        g2.drawString("Состояние " + ui.toString(), 10, ui * 15);
        STROKE_WIDTH = 1.0f;
        g2.setStroke(new BasicStroke(
            STROKE_WIDTH, 
            BasicStroke.CAP_ROUND, 
            BasicStroke.JOIN_ROUND));
        prevdx = -1;
        prevdy = -1;
        while (co <= maxx)  {
            xx1 = prevdx;
            yy1 = prevdy;
            xx2= co;
            yy2= f(co, curvedX, curvedY);
            if (prevdx != -1) {
                g2.drawLine((int)(xx1 * xMultiplier) + 20,
                        0 - (int)(yy1 * yMultiplier) + maxy * yMultiplier + 10 + 150,
                            (int)(xx2 * xMultiplier) + 20,
                        0 - (int)(yy2 * yMultiplier) + maxy * yMultiplier + 10 + 150);
            }
            co += 0.01;
            prevdx = xx2;
            prevdy = yy2;
        }
        ui++;
    }
}

public static double f(double arg, double[] x, double[] y) {
        double result = 0;
        for (int i = 0; i < x.length; i++) {
            double k = 1;
            for (int j = 0; j < y.length; j++) {
                if (j != i) {
                    k *= (arg - x[j]) / (x[i] - x[j]);
                }
            }
            result += k * y[i];
        }
        return result;
    }
  
}
