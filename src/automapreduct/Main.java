/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automapreduct;

import automapreduct.Automaton.TransitionOutput;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author kondrashov
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Automaton a = new Automaton();
        
        a.loadFromFile("InputAuto.txt");
        a.printAutomaton();
        a.saveToFile("OutputAuto.txt");
        
        /*
        TransitionOutput[] to = new TransitionOutput[5];
        to[1].NextCondition = 1;
        */
        
        
        
        
    }
}
