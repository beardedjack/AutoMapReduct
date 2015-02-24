package automapreduct;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;

public class Automaton {

    private int initialTransition = 1;
    
    // элемент таблицы переходов-выходов автомата (отдельно взятая ячейка)
    public class TransitionOutput {
                       
        int Condition; // следующее состояние
        char Output; // выход
        
        public TransitionOutput(int Condition, char Output) {
            this.Condition = Condition;
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
                System.out.print(o.Condition + "\\" + o.Output);
            }
            System.out.println("\n");
        }
    }

    
    
    public Map<Integer, List<TransitionOutput>> getConditionMap() {
        return conditionMap;
    }

}
    


