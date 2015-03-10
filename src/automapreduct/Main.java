package automapreduct;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try { 
            int i = Integer.parseInt(args[0]);
            System.out.println("k="+i);
            
            /*
            Automaton a = new Automaton();
            a.loadFromFile("InputAuto.txt");
            
            DirectedGraph sdf = a.makeReductGraph(i);
            sdf.makeFile("graphreduct.dot", i);
            */
            //DirectedGraph ag = a.makeAutomatonGraph();
            //ag.makeFile("graphautomaton.dot", 0);
            
            mAdicSet m = new mAdicSet(2, 3);
            System.out.println(m.getMAdicSet().size());
            /*
            Integer[] ccc;
            
            
            Set<Map.Entry<Integer, mAdic>> set = m.getMAdicSet();
            for (Map.Entry<Integer, mAdic> me : set) {
                //ccc = new Integer[me.getValue().getSize()];
                ccc = me.getValue().getDigits();
                System.out.print(me.getKey() + " ");
                for (Integer ccc1 : ccc) {
                    System.out.print(ccc[ccc1] + " ");
                }
                System.out.println("");
            }
            
            */
            
            
            
        }  
        catch (Exception e) { 
            System.out.println(e); 
        } 
    }
}
