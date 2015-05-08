package core;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
import java.io.*;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.StringTokenizer;

// Старый класс автомата
public class Auto {
    
    public List<String> inputAlphabet = new ArrayList<String>();
    public List<String> outputAlphabet = new ArrayList<String>();
    
    private int initialCondition = 1;
    
    // элемент таблицы переходов-выходов автомата (отдельно взятая ячейка)
    public class TransitionOutput {
                       
        Integer NextCondition; // следующее состояние
        String Output; // выход
        
        public TransitionOutput(int Condition, String Output) {
            this.NextCondition = Condition;
            this.Output = Output;
        }
    
    }
    
    public class equivalenceClasses {
        
        private TreeMap<Integer, ArrayList<Integer>> equivalenceMap = new TreeMap<Integer, ArrayList<Integer>>();
        
        // Добавить новый класс
        public void addClass(List<Integer> e) {
            equivalenceMap.put(equivalenceMap.size()+1, new ArrayList<Integer>(e));
        }
        
        public void clearClass() {
            equivalenceMap.clear();
        }
        
        // Вернуть к какому номеру класса относится состояние
        public Integer getEquivalenceClassNum(Integer v) {
            Integer c = 0;
            Set<Map.Entry<Integer, ArrayList<Integer>>> set = equivalenceMap.entrySet();
            for (Map.Entry<Integer, ArrayList<Integer>> me : set) {
                c=me.getKey();
                ArrayList<Integer> al = me.getValue();
                for (Integer i : al) {
                    if (i==v) {
                        return c;
                    }
                }
            }
            
            return c;
        }
        
        // Вернуть сколько классов эквивалентности есть
        public Integer getEquivalenceClassesCount() {
            return equivalenceMap.size();
        }
        
        // Вернуть список элементов одного класса эквивалентности
        public ArrayList<Integer> getElements(Integer i) {
            return equivalenceMap.get(i);
        }
        
    }

    // мапа всех состояний автомата с переходами/выходами по алфавиту 
    private TreeMap<Integer, List<TransitionOutput>> conditionMap = new TreeMap<Integer, List<TransitionOutput>>();
     
    // проверка на наличие состояния с таким номером
    public boolean hasCondition(Integer conditionNumber) {
        return conditionMap.containsKey(conditionNumber);
    }
     
     // добавление нового состояния
    public void addCondition(Integer transitionNumber, ArrayList<TransitionOutput> e) {
        if (!hasCondition(transitionNumber)) {
            // добавление всех переходов/выходов по каждому символу входного алфавита для данного состояния
            conditionMap.put(transitionNumber, new ArrayList<TransitionOutput>(e));
        }
    }
    
    public void printAutomaton() {
        
        System.out.print("Автомат:\nНачальное состояние: " +
                Integer.toString(initialCondition) + "\n#:\t");
        
        for (String a : inputAlphabet) {
            System.out.print(a + "\t");
        }
        System.out.print("\n");
        Set<Map.Entry<Integer, List<TransitionOutput>>> set = conditionMap.entrySet();
        for (Map.Entry<Integer, List<TransitionOutput>> me : set) {
            System.out.print(me.getKey() + ":\t");
            List<TransitionOutput> to = me.getValue();
            for (TransitionOutput o : to) {
                System.out.print(o.NextCondition + "/" + o.Output + "\t");
            }
            System.out.print("\n");
        }
    }

    public void loadFromFile(String filename) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        
        Integer co = 0;
        File f = new File(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        String line = null;
        
        line = br.readLine();
        
        // ПЕРЕДЕЛАТЬ БАРДАК ТУТ
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
        
        // Читаем выходной алфавит
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
        TransitionOutput to; 
        ArrayList<TransitionOutput> tr = new ArrayList<TransitionOutput>();
        StringTokenizer st;
        
        while ((line = br.readLine()) != null) {
            st = new StringTokenizer(line, ":,/");
            co = Integer.valueOf(st.nextToken());
           
            while(st.hasMoreElements()) {
                to = new TransitionOutput(Integer.valueOf(st.nextToken()), st.nextToken());
                //to.NextCondition = Integer.valueOf(st.nextToken());
                //to.Output = st.nextToken();
                tr.add(to);
            }
            addCondition(co, tr);
            tr.clear();
        }
        br.close();
    }
    
    public void saveToFile(String filename) throws FileNotFoundException, IOException {
        File f = new File(filename);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
        String line = "";
        
        bw.write("@Automaton");
        
        bw.newLine();
        bw.write("InputAlphabet:");
        
        for (String a : inputAlphabet) {
            line += a + ",";
        }
        bw.write(line.substring(0, line.length()-1));
        
        bw.newLine();
        bw.write("OutputAlphabet:");
        line = "";
        for (String a : outputAlphabet) {
            line += a + ",";
        }
        bw.write(line.substring(0, line.length()-1));
        
        bw.newLine();
        bw.write("InitialCondition:");
        bw.write(Integer.toString(initialCondition));
        
        Set<Map.Entry<Integer, List<TransitionOutput>>> set = conditionMap.entrySet();
        for (Map.Entry<Integer, List<TransitionOutput>> me : set) {
            bw.newLine();
            line = Integer.toString(me.getKey()) + ":";
            List<TransitionOutput> to = me.getValue();
            for (TransitionOutput o : to) {
                line += Integer.toString(o.NextCondition) + "/" + o.Output + ",";
            }
            bw.write(line.substring(0, line.length()-1));
        }
        
        bw.close();
    }
    
    public Map<Integer, List<TransitionOutput>> getConditionMap() {
        return conditionMap;
    }
    
    // Выдать элемент выходного алфавита по номеру элемента входного алфавита и номеру состояния
    public String getOutput(Integer inputElement, Integer condition) {
        List<TransitionOutput> to = conditionMap.get(condition);
        return to.get(inputElement-1).Output;
    }
    
    // Выдать номер следующего состояния по номеру элемента входного алфавита и номеру состояния
    public Integer getCondition(Integer inputElement, Integer condition) {
        List<TransitionOutput> to = conditionMap.get(condition);
        return to.get(inputElement-1).NextCondition;
    }
    
    
    // Минимизация автомата
    public void /*Automaton*/ getMinimized() {
        
        int cc;
        int co = 0;
        String so = "";

        // Первичное разбиение
        TreeMap<Integer, ArrayList<Integer>> fc = new TreeMap<Integer, ArrayList<Integer>>();
        Set<Map.Entry<Integer, List<TransitionOutput>>> set = conditionMap.entrySet();
        for (String oa : outputAlphabet) {
            for (int ia = 0; ia < inputAlphabet.size(); ia++) {
                fc.put(co, new ArrayList<Integer>());
                for (Map.Entry<Integer, List<TransitionOutput>> me : set) {
                    cc = me.getKey(); // Текущее состояние
                    List<TransitionOutput> to = me.getValue();
                    so = to.get(ia).Output;
                    if (so.equals(oa)) {
                     ArrayList<Integer> al = fc.get(co);
                     al.add(cc);
                    }
                }
                co++;
            }
        }
       
    // Делаем таблицу разбиений
    Integer table [][] = new Integer[fc.size()][conditionMap.size()];

    // 1. Забиваем ее нулями
    for (int i1=0; i1<(fc.size()); i1++) {
    for (int i2=0; i2<(conditionMap.size()); i2++) {
    table[i1][i2] = 0;}
    }
    
    // 2. Заполняем ее
    Set<Map.Entry<Integer, ArrayList<Integer>>> p = fc.entrySet();
    for (Map.Entry<Integer, ArrayList<Integer>> s : p) {
        Integer cl = s.getKey();
        ArrayList<Integer> al = s.getValue();
        for (Integer i  : al) {
            table[cl][al.indexOf(i)] = i;
        }
    }

    // 3. Преобразовываем ее (убираем дубли)
    for (int i1=0; i1<(fc.size()); i1++) {
        for (int j1=0; j1<conditionMap.size(); j1++) {
            for (int i2=0; i2<(fc.size()); i2++) {
                if (i1!=i2) {
                        for (int j2=0; j2<conditionMap.size(); j2++) {
                            
                            
                            if (table[i2][j2] == table[i1][j1]) {
                                table[i2][j2] = 0;
                            }
                        }
                    }
                }
            }
        }
    
    
    // Заполняем классы эквивалентности
    equivalenceClasses eqc = new equivalenceClasses();
    ArrayList<Integer> al;
    for (int i1=0; i1<(fc.size()); i1++) {
        al = new ArrayList<Integer>();
        for (int i2=0; i2<(conditionMap.size()); i2++) {
            if (table[i1][i2]!=0) {
                al.add(table[i1][i2]);
            }
        }
        if (al.size()!=0) {
        eqc.addClass(al);
        }
    }

    
    
    
        /*
        Automaton minAutomaton = new Automaton();
        return minAutomaton;
        */
    }  

}
    


