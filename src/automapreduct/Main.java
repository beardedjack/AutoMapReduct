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
        //a.printAutomaton();
        //a.printAutomaton();
        //a.saveToFile("OutputAuto.txt");
        //a.getMinimized();
        //System.out.println(a.getOutput(1, 9));
        //System.out.println(a.getCondition(1, 9));
        
        String o = "010000101110100001110110101010100001101101";
         System.out.println(o);
        
        String s = a.getReduction(o, 12);
        System.out.println(s);
        
        
        String a1 = a.getOutputWord(o);
        System.out.println(a1);
        
        String a2 = a.getReduction(a1, 12);
        
       
        
        
        System.out.println(a2);
      
        
        
    }
}
