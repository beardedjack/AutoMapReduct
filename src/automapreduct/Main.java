package automapreduct;

import automapreduct.Automaton.TransitionOutput;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Automaton a = new Automaton();
        
       a.loadFromFile("InputAuto.txt");
       
    }
}
