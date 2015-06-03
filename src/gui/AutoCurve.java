package gui;

import core.BasicOperations;
import core.mAdic;
import core.mAdicSet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AutoCurve extends JPanel{
    private static final float STROKE_WIDTH = 1.0f;
    private static final Dimension APP_SIZE = new Dimension(800, 600);
    private List<Point2D> points = new ArrayList<Point2D>();
    private TreeMap<Integer, Integer> Points = new TreeMap<>();
  
    public AutoCurve(mAdicSet input, mAdicSet output) {
        double x = 0, y = 0;
        Integer[] xx;
        Integer[] yy;
        Integer size = 0;
        Set<Map.Entry<Integer, mAdic>> set1 = input.getMAdicSet();
        Set<Map.Entry<Integer, mAdic>> set2 = output.getMAdicSet();
        double[] xxx = new double[set1.size()]; 
        double[] yyy = new double[set2.size()]; 
        for (Map.Entry<Integer, mAdic> me1 : set1) {
            xx = me1.getValue().getDigits();
            x = 0;
            for (Integer i = 0; i < xx.length; i++) {
                x = x + (double)(xx[i]+1)/(double)BasicOperations.getPow(xx.length+1, i);  
            }
            xxx[me1.getKey()] = x;
        }
        for (Map.Entry<Integer, mAdic> me2 : set2) {
            yy = me2.getValue().getDigits();
            y = 0;
            for (Integer i = 0; i < yy.length; i++) {
                y = y + (double)(yy[i]+1)/BasicOperations.getPow(yy.length+1, i);  
            }
            yyy[me2.getKey()] = y;
        }
        for (Integer i = 0; i < set1.size(); i++) {
            Points.put((int)xxx[i], (int)yyy[i]);
            //points.add(new Point2D.Double(xxx[i], yyy[i]));
        }
        this.setPreferredSize(APP_SIZE);
        JFrame frame = new JFrame();
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
    //g.drawLine(0, height/2, width, height/2);
    g.setColor(Color.white);
    g.fillRect(0, 0, width, height);
    g.setColor(Color.black);
    Graphics2D g2 = (Graphics2D)g;
    //g2.drawLine(0, height/2, width, height/2);
    g2.setStroke(new BasicStroke(
        STROKE_WIDTH, 
        BasicStroke.CAP_ROUND, 
        BasicStroke.JOIN_ROUND));
    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, 
        RenderingHints.VALUE_ANTIALIAS_ON);
    
    Set<Map.Entry<Integer, Integer>> set = Points.entrySet();
        
        Integer prevx = null, prevy = null;
        int x1 = 0 , y1, x2 = 0, y2 = 0;
    
        Integer minx=0, miny=0,maxx=0, maxy=0;
        for (Map.Entry<Integer, Integer> me : set) {
            if (me.getKey()>maxx) maxx = me.getKey();
            if (me.getValue()>maxy) maxy = me.getValue();
            if (me.getKey()<minx) minx = me.getKey();
            if (me.getValue()<miny) miny = me.getValue();
        }
        int xMultiplier = (width-100)/maxx;
        int yMultiplier = (height-100)/maxy;
        
        
        g2.drawLine(0, height-yMultiplier, width, height-yMultiplier);
        
        for (Map.Entry<Integer, Integer> me : set) {
            
            if (prevx != null) {
            x1 = prevx;
            y1 = prevy;       
            x2 = me.getKey();
            y2 = me.getValue();
            
            g2.drawLine(x1*xMultiplier,0-y1*yMultiplier+maxy*yMultiplier+10,x2*xMultiplier,0-y2*yMultiplier+maxy*yMultiplier+10);
            
            
            
            
            }
            g2.drawLine(x2*xMultiplier, 0, x2*xMultiplier, height-yMultiplier+10);
            g2.drawString(me.getKey().toString(), x2*xMultiplier, height-yMultiplier+15);
            prevx = x2;
            prevy= y2;
        }
    
    
}
  
}
