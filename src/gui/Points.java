package gui;

import java.util.LinkedHashMap;

class Points {
    public LinkedHashMap<Integer, Integer> Po;
    
    public Points() {
        Po = new LinkedHashMap<>();
    }
    
    public void addLine(Integer a, Integer b) {
        Po.put(a, b);
    }
    
    public LinkedHashMap<Integer, Integer> getLines() {
        return Po;
    }
}
