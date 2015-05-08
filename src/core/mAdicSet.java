package core;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

// класс множества m-адических чисел
public class mAdicSet {

    private TreeMap<Integer, mAdic> mAdicMap = new TreeMap<Integer, mAdic>();
 
    // Все перестановки множества
    // c: основание системы счисления, s: кол-во элементов множества
    public mAdicSet(Integer c, Integer s) throws CloneNotSupportedException { 
        mAdic workMAdic;
        mAdic tempMAdic = new mAdic(c, s);
        workMAdic = tempMAdic.clone();
        mAdicMap.put(0, workMAdic);
        for (Integer i = 1; i < BasicOperations.getPow(c, s); i++) {
            tempMAdic.increaseMAdic();
            //workMAdic = new mAdic(c, s);
            workMAdic = tempMAdic.clone();
            addMAdic(i, workMAdic);
        }
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
