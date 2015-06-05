package gui;

import core.Automaton;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            //automaton.directedgraph.makeAnalysis();
        } catch (CloneNotSupportedException | InterruptedException ex) {
            Logger.getLogger(Progress.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
