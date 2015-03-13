package automapreduct;

public class BasicOperations {
    
    public static Integer getPow(Integer a, Integer b) {
        Integer res = 1;
        for (int i = 1; i <= b; i++) { 
            res *= a;
        }
        return res;
    }
    
}
