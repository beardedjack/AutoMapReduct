package automapreduct;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class mAdicSet {

    private TreeMap<Integer, mAdic> mAdicMap = new TreeMap<Integer, mAdic>();

    // Все перестановки множества
    public mAdicSet(Integer c, Integer s) { // основание, кол-во элементов
        mAdic ma;
        Integer co = 1;

        Integer[] elements = new Integer[s];

        Arrays.fill(elements, 0);

        for (Integer i = 0; i < s; i++) {
            for (Integer j = 0; j < c; j++) {
                
            }
            
        }
    }

    public Set<Map.Entry<Integer, mAdic>> getMAdicSet() {
        return mAdicMap.entrySet();
    }

}
