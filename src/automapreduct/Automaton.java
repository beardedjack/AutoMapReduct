package automapreduct;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.io.*;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Automaton {
    // мапа всех состояний автомата с переходами/выходами по алфавиту 
    private TreeMap<Integer, List<TransitionOutput>> conditionMap = new TreeMap<Integer, List<TransitionOutput>>();
        
    private List<Integer> inputAlphabet = new ArrayList<Integer>();
    private List<Integer> outputAlphabet = new ArrayList<Integer>();
    private Integer alphabetsDimention =0;
    private int initialCondition = 1;
    
    // элемент таблицы переходов-выходов автомата (отдельно взятая ячейка)
    private class TransitionOutput {
                       
        Integer NextCondition; // следующее состояние
        Integer Output; // выход
        
        public TransitionOutput(int Condition, int Output) {
            this.NextCondition = Condition;
            this.Output = Output;
        }
    
    }
     
    // проверка на наличие состояния с таким номером
    private boolean hasCondition(Integer conditionNumber) {
        return conditionMap.containsKey(conditionNumber);
    }
     
     // добавление нового состояния
    private void addCondition(Integer transitionNumber, ArrayList<TransitionOutput> e) {
        if (!hasCondition(transitionNumber)) {
            // добавление всех переходов/выходов по каждому символу входного алфавита для данного состояния
            conditionMap.put(transitionNumber, new ArrayList<TransitionOutput>(e));
        }
    }
    
    private void printAutomaton() {
        
        System.out.print("Автомат:\nНачальное состояние: " +
                Integer.toString(initialCondition) + "\n#:\t");
        
        for (int a : inputAlphabet) {
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
        
         // Читаем размерность множества входного/выходного алфавита
        line = br.readLine();
        StringTokenizer dm = new StringTokenizer(line, ":");
        while(dm.hasMoreTokens()) {
            dm.nextToken();
            line = dm.nextToken();
            alphabetsDimention = Integer.valueOf(line);
        }
        
        // Заполняем алфавиты (входной=выходной)
        for (Integer a=0; a<alphabetsDimention; a++) {
            inputAlphabet.add(a);
            outputAlphabet.add(a);
        }
        
        
        
        // Читаем входной алфавит
        /*
        line = br.readLine();
        StringTokenizer ia1 = new StringTokenizer(line, ":");
        while(ia1.hasMoreTokens()) {
            ia1.nextToken();
            line = ia1.nextToken();
        }
        StringTokenizer ia2 = new StringTokenizer(line, ",");
        while(ia2.hasMoreTokens()) {
            line = ia2.nextToken();
            inputAlphabet.add(Integer.valueOf(line));
            
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
            outputAlphabet.add(Integer.valueOf(line));
        }
       */ 
                
                
                
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
                to = new TransitionOutput(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken()));
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
        bw.write("AlphabetsDimention:" + alphabetsDimention);
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
            bw.write(line.substring(0, line.length() - 1));
        }
        bw.close();
    }
    
    
    // Выход автомата по входу
    private mAdic getOutput(mAdic input) {
        mAdic result = new mAdic(alphabetsDimention, 0);
        return result;
    }
    
    private String getOutputWord(String inputWord) {
        String outWord ="";
        int tmp = 0; 
        int currentCondition = 1;
        Integer i = inputWord.length()-1;
        
        do {tmp = Character.getNumericValue(inputWord.charAt(i));
            outWord =  Integer.toString(this.getOutput(tmp, currentCondition)) + outWord  ;
            // outWord += Integer.toString(this.getOutput(tmp, currentCondition));
            currentCondition = getCondition(tmp, currentCondition);
            i = i-1;
        } while (i>=0);
        return outWord;
    }
        
    // Выдать степень числа
    private Integer getPow(Integer a, Integer b) {
        Integer res = 1;
        for (int i = 1; i <= b; i++) { 
            res *= a;
        }
        return res;
    }
            
    // Выдать элемент выходного алфавита по номеру элемента входного алфавита и номеру состояния
    private Integer getOutput(Integer inputElement, Integer condition) {
        List<TransitionOutput> to = conditionMap.get(condition);
        return to.get(inputElement).Output;
    }
    
    // Выдать номер следующего состояния по номеру элемента входного алфавита и номеру состояния
    private Integer getCondition(Integer inputElement, Integer condition) {
        List<TransitionOutput> to = conditionMap.get(condition);
        return to.get(inputElement).NextCondition;
    }
       
    private String addBits(String s, Integer k) {
        StringBuffer sb = new StringBuffer(s).reverse();
        while(sb.length() < k)
            sb.append("0");
            sb.reverse();
        return sb.toString();
    }

    // Выдать граф автомата
    public DirectedGraph makeAutomatonGraph() {
        DirectedGraph graph = new DirectedGraph();
        String curr ,next;
        Set<Map.Entry<Integer, List<TransitionOutput>>> set = conditionMap.entrySet();
        for (Map.Entry<Integer, List<TransitionOutput>> me : set) {
            curr = Integer.toString(me.getKey());
            List<TransitionOutput> to = me.getValue();
            for (TransitionOutput o : to) {
                next = Integer.toString(o.NextCondition);
                graph.addEdge(curr, next);
            }
        }
        return graph;
    }
   
    // Выдать граф редукции
    public DirectedGraph makeReductGraph(Integer k) {
        DirectedGraph graph = new DirectedGraph();
        
        // мапа <№ множества> - <x, f(x)>
        TreeMap<Integer, LinkedList<String>> reductMap = new TreeMap<Integer, LinkedList<String>>();
        
        String input, output = ""; // <входное слово>,<выходное слово>
        LinkedList<String> corr;
        
        for (Integer i = 0; i < getPow(alphabetsDimention, k); i++) {
            corr = new LinkedList<String>();
            input = addBits(Integer.toBinaryString(i), k);
            output = getOutputWord(input);
            corr.add(input);
            corr.add(output);
            reductMap.put(i, corr);
        }

        Set<Map.Entry<Integer, LinkedList<String>>> set = reductMap.entrySet();
        
        for (Map.Entry<Integer, LinkedList<String>> me1 : set) { 
            for (Map.Entry<Integer, LinkedList<String>> me2 : set) {
                if (me1.getValue().get(1).equalsIgnoreCase(me2.getValue().get(0))) {
                    graph.addEdge(Integer.toString(me1.getKey()), Integer.toString(me2.getKey()));
                }
            }
        }

        return graph;
    }
    


}