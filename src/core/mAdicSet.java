package core;

import gui.AMRMain;
import static java.lang.Thread.yield;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

// класс множества m-адических чисел
public class mAdicSet {

    private AMRMain frame;
    private LinkedHashMap<Integer, mAdic> mAdicMap;
    
    // Все перестановки множества
    // c: основание системы счисления, s: кол-во элементов множества
    public mAdicSet(Integer c, Integer s, AMRMain someframe) throws CloneNotSupportedException { 
        this.frame = someframe;
        double x = 0;
        int y = 0;
        Integer a = BasicOperations.getPow(c, s);
        mAdicMap = new LinkedHashMap<>(a*s, 1);
        mAdic workMAdic;
        mAdic tempMAdic = new mAdic(c, s);
        workMAdic = tempMAdic.clone();
        mAdicMap.put(0, workMAdic);
        for (Integer i = 1; i < a; i++) {
            tempMAdic.increaseMAdic();
            workMAdic = tempMAdic.clone();
            mAdicMap.put(i, workMAdic);
            x = i * 100 / a;
            y = (int)x + 1;
            yield();
            frame.setInputWordsProgressBarValue(y);
        }
        frame.setInputWordsProgressBarValue(100);
    }
    
    public mAdicSet(Integer a) {
        mAdicMap = new LinkedHashMap<>(a*5, 1);
    }
    
    public void addMAdic(Integer element, mAdic value) {
        mAdicMap.put(element, value);
    }
    
    public Set<Map.Entry<Integer, mAdic>> getMAdicSet() {
        return mAdicMap.entrySet();
    }

}
