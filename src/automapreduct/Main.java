package automapreduct;

import automapreduct.Automaton.TransitionOutput;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try { 
            int i = Integer.parseInt(args[0]);
            System.out.println("k="+i);
            Automaton a = new Automaton();
            a.loadFromFile("InputAuto.txt");
            DirectedGraph sdf = a.makeGraph(i);
            sdf.makeFile("graph.dot");
        } 
        catch (Exception e) { 
            System.out.println(e); 
        } 
    }
}
