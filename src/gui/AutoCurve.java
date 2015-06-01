package gui;

import core.BasicOperations;
import core.mAdic;
import core.mAdicSet;
import java.awt.BasicStroke;
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
 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AutoCurve extends JPanel{
  private static final float STROKE_WIDTH = 5.5f;
  private static final Dimension APP_SIZE = new Dimension(800, 600);
  private List<Point2D> points = new ArrayList<Point2D>();
  
  public AutoCurve(mAdicSet input, mAdicSet output) {
      double x = 0, y = 0;
      Integer[] xx;
      Integer[] yy;
      Integer size = 0;
      
      Set<Map.Entry<Integer, mAdic>> set1 = input.getMAdicSet();
      //System.out.println(set1.size());
      Set<Map.Entry<Integer, mAdic>> set2 = output.getMAdicSet();
      
      double[] xxx = new double[set1.size()]; 
      double[] yyy = new double[set2.size()]; 
      
      for (Map.Entry<Integer, mAdic> me1 : set1) {
          //size = set1.size();
          xx = me1.getValue().getDigits();
          
          x = 0;
          for (Integer i = 0; i < xx.length; i++) {
              
              x = x + (xx[i]+1)/(double)BasicOperations.getPow(xx.length+1, i);  
          }
          xxx[me1.getKey()] = x;
          
      }
      
      for (Map.Entry<Integer, mAdic> me2 : set2) {
           yy = me2.getValue().getDigits();
          y = 0;
          for (Integer i = 0; i < yy.length; i++) {
              y = y + (yy[i]+1)/BasicOperations.getPow(yy.length+1, i);  
          }
          yyy[me2.getKey()] = y;
      }
      
      for (Integer i = 0; i < set1.size(); i++) {
          points.add(new Point2D.Double(xxx[i], yyy[i]));
          
      }
  
      this.setPreferredSize(APP_SIZE);
      JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(this);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
      
      
   
    
  }
  
  
  
  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    int width = getWidth();
    int height = getHeight();
    //g.drawLine(0, height/2, width, height/2);
    Graphics2D g2 = (Graphics2D)g;
    //g2.drawLine(0, height/2, width, height/2);
    g2.setStroke(new BasicStroke(
        STROKE_WIDTH, 
        BasicStroke.CAP_ROUND, 
        BasicStroke.JOIN_ROUND));
    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, 
        RenderingHints.VALUE_ANTIALIAS_ON);
    if (points.size() > 1)
    {
      double xMultiplier = width;
      double yMultiplier = height;
      Point2D prevPoint = null;
      for (Point2D point : points)
      {
        if (prevPoint != null)
        {
          int x1 = BasicOperations.getPow((int) (prevPoint.getX()), 4);
          int y1 = BasicOperations.getPow((int) (prevPoint.getY()), 4);
          int x2 = BasicOperations.getPow((int) (point.getX()), 4);
          int y2 = BasicOperations.getPow((int) (point.getY()), 4);
          g2.drawLine(x2, y2, x2, y2);
          
        }
        prevPoint = point;
      }
    }
  }
 

 public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        //createAndShowGUI();
      }
    });
  }
  
  
  
}
