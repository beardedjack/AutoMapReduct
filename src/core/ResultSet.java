package core;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import gui.AMRMain;
import static java.lang.Thread.yield;
import java.util.Arrays;
import java.util.TreeMap;

public class ResultSet {

    private AMRMain frame;
    
    private TreeMap<Integer, Integer> inputMap = new TreeMap<>();
    private Multimap<Integer, Integer> outputMap = ArrayListMultimap.create();
    
    public ResultSet(Integer c, Integer s, AMRMain someframe, Automaton automaton) {
        this.frame = someframe;
        double x = 0;
        int y = 0;
        Integer a = BasicOperations.getPow(c, s);
        mAdic inputMAdic = new mAdic(c, s);
        mAdic outputMAdic = automaton.getOutput(inputMAdic);
        inputMap.put(Arrays.deepHashCode(inputMAdic.getDigits()), 0);
        outputMap.put(Arrays.deepHashCode(outputMAdic.getDigits()), 0);
        for (Integer i = 1; i < a; i++) {
            inputMAdic.increaseMAdic();
            outputMAdic = automaton.getOutput(inputMAdic);
            inputMap.put(Arrays.deepHashCode(inputMAdic.getDigits()), i);
            outputMap.put(Arrays.deepHashCode(outputMAdic.getDigits()), i);
            x = i * 100 / a;
            y = (int)x + 1;
            yield();
            frame.setInputWordsProgressBarValue(y);
            frame.setOutputWordsProgressValue(y);
        }
        frame.setInputWordsProgressBarValue(100);
        frame.setOutputWordsProgressValue(100);
}
    
    public TreeMap<Integer, Integer> getInputMap() {
        return inputMap;
    }
    
    public Multimap<Integer, Integer> getOutputMap() {
        return outputMap;
    }

}
