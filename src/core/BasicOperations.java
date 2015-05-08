package core;

public class BasicOperations {
    
    // Возведение в степень на Integer'ах
    public static Integer getPow(Integer a, Integer b) {
        Integer res = 1;
        for (int i = 1; i <= b; i++) { 
            res *= a;
        }
        return res;
    }
    
}
