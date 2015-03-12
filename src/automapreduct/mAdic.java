package automapreduct;

// m-адическое число

import java.util.Arrays;


public class mAdic {
    
    private Integer capacity; // основание системы счисления
    private Integer[]  digits; // элементы множетсва
    
    public mAdic(Integer m, int s) {
        this.capacity = m;
        this.digits = new Integer[s];
        
        
        Arrays.fill(digits, 0);
        
    } 
    
    public void setDigits(Integer[] value) {
        digits = value;
    } 
    
    public Integer[] getDigits() {
        return digits;
    }
    
    public Integer getSize() {
        return digits.length;
    }
    
}
