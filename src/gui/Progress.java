package gui;

import core.Automaton;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Progress extends Thread {
    
    private final AMRMain frame;
    private final Automaton automaton;
    
    public Progress(AMRMain someframe, Automaton someautomaton) {
        this.frame = someframe;
        this.automaton = someautomaton;
    }
    
    @Override
    public void run() {
        if (frame.isCycleCalc) {
            for (Integer i = 1; i <= frame.getK(); i++) {
                try {
                    frame.setProcessProgressBarValue(0);
                    frame.setInputWordsProgressBarValue(0);
                    frame.setOutputWordsProgressValue(0);
                    automaton.k = i;
                    frame.setKvalueLabelData(i.toString() + " из " + frame.getK());
                    automaton.makeReductGraph(automaton.k);
                } catch (CloneNotSupportedException | InterruptedException ex) {
                    Logger.getLogger(Progress.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else {
            try {
                frame.setProcessProgressBarValue(0);
                frame.setInputWordsProgressBarValue(0);
                frame.setOutputWordsProgressValue(0);
                automaton.makeReductGraph(automaton.k);
            } catch (CloneNotSupportedException | InterruptedException ex) {
                Logger.getLogger(Progress.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
