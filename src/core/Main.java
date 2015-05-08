package core;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try { 
            int i = Integer.parseInt(args[0]);
            Automaton a = new Automaton();
            a.loadFromFile("InputAuto.txt");
            DirectedGraph sdf = a.makeReductGraph(i);
            //sdf.makeFile("graphreduct.dot", i);
            sdf.makeAnalysis();
        }  
        
        catch (Exception e) { 
            System.out.println(e); 
        } 
    }
}
