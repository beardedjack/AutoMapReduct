package core;

import gui.AMRMain;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.io.*;
import static java.lang.Math.floor;
import static java.lang.Thread.sleep;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Automaton {
    
    ////////////////// Для GUI /////////////////
    private AMRMain frame;
    
    public Automaton(AMRMain someframe) {
        this.frame = someframe;
    }
    
    ////////////////////////////////////////////
    
    // мапа всех состояний автомата с переходами/выходами по алфавиту 
    private TreeMap<Integer, List<TransitionOutput>> conditionMap = new TreeMap<Integer, List<TransitionOutput>>();
        
    private List<Integer> inputAlphabet = new ArrayList<Integer>();
    private List<Integer> outputAlphabet = new ArrayList<Integer>();
    private Integer alphabetsDimention = 0;
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
        for (Integer a = 0; a < alphabetsDimention; a++) {
            inputAlphabet.add(a);
            outputAlphabet.add(a);
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
                to = new TransitionOutput(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken()));
                //to.NextCondition = Integer.valueOf(st.nextToken());
                //to.Output = st.nextToken();
                tr.add(to);
            }
            addCondition(co, tr);
            tr.clear();
        }
        br.close();
        
        
        frame.setDimentionLabelData(alphabetsDimention.toString());
        frame.setStatesCountLabelData(Integer.toString(conditionMap.size()));
        frame.setInitialStateLabelData(Integer.toString(initialCondition));
        
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
    
    // Выдать множество выходных слов по множеству входных слов
    public mAdicSet getOutputSet(mAdicSet input) {
        mAdicSet result = new mAdicSet();
        mAdic m;
        Integer num;
        Integer currentCondition;
        Integer[] in, out;
        Set<Map.Entry<Integer, mAdic>> set = input.getMAdicSet();
        
        for (Map.Entry<Integer, mAdic> me : set) {
            num = me.getKey();
            in = me.getValue().getDigits();
            out = new Integer[in.length];
            currentCondition = initialCondition;
            
            for (Integer i = 0; i < in.length; i++) {
                out[i] = getOutput(in[i], currentCondition);
                currentCondition = getCondition(in[i], currentCondition);
            }
            m = new mAdic(alphabetsDimention, out);
            result.addMAdic(num, m);
            //System.out.println("Input: " + Arrays.toString(in) + " Output: " + Arrays.toString(out));
        }
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
        DirectedGraph graph = new DirectedGraph(frame);
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
    public DirectedGraph makeReductGraph(Integer k) throws CloneNotSupportedException, InterruptedException {
        DirectedGraph graph = new DirectedGraph(frame);
        
        mAdicSet input = new mAdicSet(alphabetsDimention, k);
        mAdicSet output = getOutputSet(input);
        
        // !!!
        TreeMap<Integer, Integer> inputMap = new TreeMap<Integer, Integer>();
        TreeMap<Integer, Integer> outputMap = new TreeMap<Integer, Integer>();
        
        Set<Map.Entry<Integer, mAdic>> set1 = input.getMAdicSet();
        Set<Map.Entry<Integer, mAdic>> set2 = output.getMAdicSet();
        
        for (Map.Entry<Integer, mAdic> me1 : set1) {
            inputMap.put(me1.getKey(), Arrays.deepHashCode(me1.getValue().getDigits()));
        }
        
        for (Map.Entry<Integer, mAdic> me2 : set2) {
            outputMap.put(me2.getKey(), Arrays.deepHashCode(me2.getValue().getDigits()));
        }
        
        Set<Map.Entry<Integer, Integer>> inputSet = inputMap.entrySet();
        Set<Map.Entry<Integer, Integer>> outputSet = outputMap.entrySet();
        
        
        //System.out.println("Вершин:" + inputSet.size());
        
        double x = 0;
        y =0;
        
        
        
        
        for (Map.Entry<Integer, Integer> me1: inputSet) {
            //System.out.println("Вершина: " + me1.getKey());
            
            x = me1.getKey()*100/inputSet.size();
            y = (int)x+1;
            //frame.setProcessProgressBarValue(y);
            
            
            for (Map.Entry<Integer, Integer> me2: outputSet) {
                if (Objects.equals(me2.getValue(), me1.getValue())) {
                    graph.addEdge(Integer.toString(me2.getKey()), Integer.toString(me1.getKey()));
                    graph.edgesCount ++; // счетчик ребер графа
                }
            }
        }
        
        /*
        for (Map.Entry<Integer, mAdic> me1 : set1) {
            for (Map.Entry<Integer, mAdic> me2 : set2) {
                if (Arrays.equals(me2.getValue().getDigits(), me1.getValue().getDigits())) {
                    graph.addEdge(Integer.toString(me2.getKey()), Integer.toString(me1.getKey()));
                    graph.edgesCount ++; // счетчик ребер графа
                }
            }
        }

        */
        return graph;
    }
    
    public int y;
    
    
    
   
    
}