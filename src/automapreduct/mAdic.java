package automapreduct;

// m-адическое число

import java.util.Arrays;


public class mAdic implements Cloneable {
    
    private Integer capacity; // основание системы счисления
    private Integer[]  digits; // элементы множетсва
    
    public mAdic(Integer m, int s) {
        this.capacity = m;
        this.digits = new Integer[s];
        Arrays.fill(digits, 0);
    }
    
    public mAdic(Integer m, Integer[] s) {
        this.capacity = m;
        this.digits = Arrays.copyOf(s, s.length);
    }
    
    public void setDigits(Integer[] value) {
        digits = value;
    } 
    
    public void setCapacity(Integer c) {
        capacity = c;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public Integer[] getDigits() {
        return digits;
    }
    
    public Integer getSize() {
        return digits.length;
    }
    
    public void increaseMAdic() {
        boolean flag = false;
        for (Integer i = 0; i < digits.length; i++) {
            if (flag) {
                if (digits[i] == (capacity - 1)) {
                    digits[i] = 0;
                }
                else {
                    digits[i] = digits[i] + 1;
                    return;
                }
            }
            else {
                if (digits[i] == (capacity - 1)) {
                    digits[i] = 0;
                    flag = true;
                }
                else {
                    digits[i] = digits[i] + 1;
                    return;
                }
            }
        }
    }
    
    // Глубокое клонирование
    @Override
    public mAdic clone() throws CloneNotSupportedException {
        Integer c = getCapacity();
        Integer[] i = Arrays.copyOf(digits, digits.length);
        mAdic obj = (mAdic)super.clone();
        obj.setCapacity(c);
        obj.setDigits(i);
        return obj;
    }
    
    
}
