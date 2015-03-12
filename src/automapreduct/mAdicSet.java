package automapreduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class mAdicSet {

    private TreeMap<Integer, mAdic> mAdicMap = new TreeMap<Integer, mAdic>();
    private Integer[] elements;
    private ArrayList<Integer> al;
    
    
    // Все перестановки множества
    public mAdicSet(Integer c, Integer s) { // основание, кол-во элементов
        mAdic ma;
        Integer co = 1;

        elements = new Integer[s];

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
            
            
                permute(c, s);
           

    }
    
    private void permute(Integer u, Integer v) {
        al = new ArrayList<Integer>();
        for (Integer a = 0; a < u; a++) {
            
            
        }
       
       
        
    }
    
    public Set<Map.Entry<Integer, mAdic>> getMAdicSet() {
        return mAdicMap.entrySet();
    }

}
