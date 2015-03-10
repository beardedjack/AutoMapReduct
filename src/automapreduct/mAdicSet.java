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

        //for (Integer i = 0; i < c; i++) {
            for (int a = 0; a<2; a++) {
                for (int b = 0; b<2; b++) {
                    for (int u = 0; u<2; u++) {
                        System.out.println(a + " " + b + " " + u);
                    }
                }
            }
        //}
            
            for (Integer i = 0; i < s; i++) {
                permute(c, i);
            }

    }
    
    private void permute(Integer u, Integer w) {
        for (Integer a = 0; a < u; a++) {
           // System.out.print(a + " ");
            
        }
        System.out.println("");
        //permute(u, w);
        
    }
    
    public Set<Map.Entry<Integer, mAdic>> getMAdicSet() {
        return mAdicMap.entrySet();
    }

}
