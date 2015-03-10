package automapreduct;

// m-адическое число

public class mAdic {
    
    private Integer capacity; // основание системы счисления
    private Integer[]  digits; // элементы множетсва
    
    public mAdic(Integer m, int s) {
        this.capacity = m;
        this.digits = new Integer[s];
        
        
        for (Integer i = 0; i < s; i++) {
            digits[i] = 0;
        }
        
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
