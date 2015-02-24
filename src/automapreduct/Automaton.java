package automapreduct;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
import java.io.*;
import java.util.StringTokenizer;

public class Automaton {

    public List<String> inputAlphabet = new ArrayList<String>();
    public List<String> outputAlphabet = new ArrayList<String>();
    
    private int initialCondition = 1;
    
    // элемент таблицы переходов-выходов автомата (отдельно взятая ячейка)
    public class TransitionOutput {
                       
        int NextCondition; // следующее состояние
        String Output; // выход
        
        public TransitionOutput(int Condition, String Output) {
            this.NextCondition = Condition;
            this.Output = Output;
        }
    
    }

    // мапа всех состояний автомата с переходами/выходами по алфавиту 
    private TreeMap<Integer, List<TransitionOutput>> conditionMap = new TreeMap<Integer, List<TransitionOutput>>();
     
    // проверка на наличие состояния с таким номером
    public boolean hasCondition(Integer conditionNumber) {
        return conditionMap.containsKey(conditionNumber);
    }
     
     // добавление нового состояния
    public void addCondition(Integer transitionNumber, TransitionOutput[] e) {
        if (!hasCondition(transitionNumber)) {
            
            conditionMap.put(transitionNumber, new ArrayList<TransitionOutput>());
            
            // добавление всех переходов/выходов по каждому символу входного алфавита для данного состояния
            List<TransitionOutput> to = conditionMap.get(transitionNumber);
            for (TransitionOutput c : e) {
                to.add(c);
            }
        }
    }
    
    public void printAutomaton() {
        System.out.println("Автомат:\n");
        System.out.println("Состояние:\n");
        Set<Map.Entry<Integer, List<TransitionOutput>>> set = conditionMap.entrySet();
        for (Map.Entry<Integer, List<TransitionOutput>> me : set) {
            System.out.print(me.getKey() + ": ");
            List<TransitionOutput> to = me.getValue();
            for (TransitionOutput o : to) {
                System.out.print(o.NextCondition + "\\" + o.Output);
            }
            System.out.println("\n");
        }
    }

    public void loadFromFile(String filename) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        
        int co = 0;
        File f = new File(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        String line = null;
        
        line = br.readLine();
        
        
        // Читаем входной алфавит
        line = br.readLine();
        StringTokenizer ia1 = new StringTokenizer(line, ":");
        while(ia1.hasMoreTokens()) {
            ia1.nextToken();
            line = ia1.nextToken();
        }
        StringTokenizer ia2 = new StringTokenizer(line, ",");
        while(ia2.hasMoreTokens()) {
            line = ia2.nextToken();
            inputAlphabet.add(line);
        }
        
        // Читаем входной алфавит
        line = br.readLine();
        StringTokenizer oa1 = new StringTokenizer(line, ":");
        while(oa1.hasMoreTokens()) {
            oa1.nextToken();
            line = oa1.nextToken();
        }
        StringTokenizer oa2 = new StringTokenizer(line, ",");
        while(oa2.hasMoreTokens()) {
            line = oa2.nextToken();
            outputAlphabet.add(line);
        }
        
        // Читаем начальное состояние
        line = br.readLine();
        StringTokenizer ic = new StringTokenizer(line, ":");
        while(ic.hasMoreTokens()) {
            ic.nextToken();
            line = ic.nextToken();
            initialCondition = Integer.valueOf(line);
        }
        
        // Читаем данные: Состояние:<переход/выход>,...,<переход/выход>
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        
        br.close();
        
    }
    
    public Map<Integer, List<TransitionOutput>> getConditionMap() {
        return conditionMap;
    }

}
    


