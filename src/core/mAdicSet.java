package core;

import gui.AMRMain;
import static java.lang.Thread.yield;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

// класс множества m-адических чисел
public class mAdicSet {

    private AMRMain frame;
    
    private TreeMap<Integer, mAdic> mAdicMap = new TreeMap<>();
 
    // Все перестановки множества
    // c: основание системы счисления, s: кол-во элементов множества
    public mAdicSet(Integer c, Integer s, AMRMain someframe) throws CloneNotSupportedException { 
        
        double x = 0;
        int y = 0;
        Integer a = BasicOperations.getPow(c, s);
        
        this.frame = someframe;
        
        mAdic workMAdic;
        mAdic tempMAdic = new mAdic(c, s);
        workMAdic = tempMAdic.clone();
        mAdicMap.put(0, workMAdic);
        
        for (Integer i = 1; i < a; i++) {
            tempMAdic.increaseMAdic();
            //workMAdic = new mAdic(c, s);
            workMAdic = tempMAdic.clone();
            addMAdic(i, workMAdic);
            
            x = i*100/a;
            y = (int)x+1;
            
            yield();
            
            frame.setInputWordsProgressBarValue(y);
            
        }
        frame.setInputWordsProgressBarValue(100);
    }
    
    public mAdicSet() {
        
    }
    
    public void addMAdic(Integer element, mAdic value) {
        mAdicMap.put(element, value);
    }
    
    public Set<Map.Entry<Integer, mAdic>> getMAdicSet() {
        return mAdicMap.entrySet();
    }

}
