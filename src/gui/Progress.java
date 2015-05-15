/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import core.Automaton;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kondrashov
 * 
 * 
 * 
 */
public class Progress extends Thread {
    
    private AMRMain frame;
    private Automaton automaton;
    
    
    
    public Progress(AMRMain someframe, Automaton someautomaton) {
        this.frame = someframe;
        this.automaton = someautomaton;
    }
    
    @Override
    public void run() {
        try {
            automaton.makeReductGraph(automaton.k);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Progress.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Progress.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
    
}
