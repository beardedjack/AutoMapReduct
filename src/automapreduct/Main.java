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
        
        String o = a.getOutputWord("22121001210201222010210102020101010100100011212120101121020120120102010111002201001010110102020210122010101110100000111011111000011122222200101222000111100221101010");
        
        System.out.println(o);
        
        
        
        
        
    }
}
