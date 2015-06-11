package core;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import gui.AMRMain;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class Automaton {
    
    public AMRMain frame;
    public DirectedGraph directedgraph;
    public int k;
        
    public Automaton(AMRMain someframe) {
        this.frame = someframe;
    }
    
    public Automaton() {
    
    }

    // мапа всех состояний автомата с переходами/выходами по алфавиту 
    private TreeMap<Integer, List<TransitionOutput>> conditionMap = new TreeMap<>();
    private List<Integer> inputAlphabet = new ArrayList<>();
    private List<Integer> outputAlphabet = new ArrayList<>();
    public Integer alphabetsDimention = 0;
    private int initialCondition = 1;
    
    //public ArrayList<Integer> graphTypes;
    
    public ArrayList<Integer> graphCycles;
    public ArrayList<Boolean> graphTails;
    
    // элемент таблицы переходов-выходов автомата (отдельно взятая ячейка)
    private class TransitionOutput {
        Integer NextCondition; // следующее состояние
        Integer Output; // выход
        public TransitionOutput(int Condition, int Output) {
            this.NextCondition = Condition;
            this.Output = Output;
        }
    }
     
    public void setInitialCondition(Integer i) {
        initialCondition = i;
    }
    
    public int getConditionsCount() {
        return conditionMap.size();
    }
    
    // проверка на наличие состояния с таким номером
    private boolean hasCondition(Integer conditionNumber) {
        return conditionMap.containsKey(conditionNumber);
    }
     
     // добавление нового состояния
    private void addCondition(Integer transitionNumber, ArrayList<TransitionOutput> e) {
        if (!hasCondition(transitionNumber)) {
            // добавление всех переходов/выходов по каждому символу входного алфавита для данного состояния
            conditionMap.put(transitionNumber, new ArrayList<>(e));
        }
    }
    
    public void printAutomaton() {
        frame.appendTextAreaSimpleTextA("Автомат:\nНачальное состояние: " +
                Integer.toString(initialCondition) + "\n#:\t");
        for (int a : inputAlphabet) {
            frame.appendTextAreaSimpleTextA(a + "\t");
        }
        frame.appendTextAreaSimpleTextA("\n");
        Set<Map.Entry<Integer, List<TransitionOutput>>> set = conditionMap.entrySet();
        for (Map.Entry<Integer, List<TransitionOutput>> me : set) {
            frame.appendTextAreaSimpleTextA(me.getKey() + ":\t");
            List<TransitionOutput> to = me.getValue();
            for (TransitionOutput o : to) {
                frame.appendTextAreaSimpleTextA(o.NextCondition + "/" + o.Output + "\t");
            }
            frame.appendTextAreaSimpleTextA("\n");
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
        ArrayList<TransitionOutput> tr = new ArrayList<>();
        StringTokenizer st;
        while ((line = br.readLine()) != null) {
            st = new StringTokenizer(line, ":,/");
            co = Integer.valueOf(st.nextToken());
            while(st.hasMoreElements()) {
                to = new TransitionOutput(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken()));
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
        mAdicSet result = new mAdicSet(input.getMAdicSet().size());
        mAdic m;
        Integer num;
        Integer currentCondition;
        Integer[] in, out;
        Set<Map.Entry<Integer, mAdic>> set = input.getMAdicSet();
        double x = 0;
        int y = 0;
        frame.setOutputWordsProgressValue(0);
        
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
            x = num * 100 / set.size();
            y = (int)x + 1;
            frame.setOutputWordsProgressValue(y);
        }
        frame.setOutputWordsProgressValue(100);
        return result;
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
   
    private void getMapParams() {
        Integer cycles = 0;
        Integer type = 0;
        boolean tail = false;
        for (Integer a : graphCycles) {
           cycles += a; 
        }
        for (boolean a : graphTails) {
            tail = tail | a;
        }
        if (!tail) {
            if (cycles/k == 1) {
                type = 3;
            }
            else {
                type = 2;
            }
        } 
        else {
            if ((double)cycles / k == 1.0) {
                type = 1;
            }
            else {
                type = 0;
            }
        }
        
        String mapType = "Класс отображения: ";
        switch (type) {
            case 0 : {mapType += "Не определено."; break;}
            case 1 : {mapType += "Эргодическое."; break;}
            case 2 : {mapType += "Сохраняет меру."; break;}
            case 3 : {mapType += "Эргодическое + Сохраняет меру."; break;}
        }
        
        frame.appendTextAreaSimpleText(mapType + 
                "\n-----------------------------------------------------------------------\n");
    }
    
    // Выдать граф редукции
    public void makeReductGraph(Integer u) throws CloneNotSupportedException, InterruptedException {
        frame.setReductGraphEdgesLabelData(Integer.toString(0));
        frame.setProcessProgressBarValue(0);
        frame.setInputWordsProgressBarValue(0);
        frame.setOutputWordsProgressValue(0);
        frame.setSimpleProgressValue(0);
        
        if (!frame.isCycleCalc) {
            frame.appendTextAreaText("Построение графа редукции при k = " + k + " ...");
        }
        
        directedgraph = new DirectedGraph(frame);
        
        // Множество входных слов
        mAdicSet input = new mAdicSet(alphabetsDimention, k, frame);
        // Множество выходных слов
        mAdicSet output = getOutputSet(input);
        
        // <editor-fold defaultstate="collapsed" desc="Old Code">
        /* Старая версия построения графа редукции (медленно)
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
        double x = 0;
        int y = 0;
        for (Map.Entry<Integer, Integer> me1: outputSet) {
            for (Map.Entry<Integer, Integer> me2: inputSet) {
                if (Objects.equals(me2.getValue(), me1.getValue())) {
                    directedgraph.addEdge(Integer.toString(me1.getKey()), Integer.toString(me2.getKey()));
                    directedgraph.edgesCount++; // счетчик ребер графа
                }
                //frame.setSimpleProgressValue(me1.getKey()*100/inputSet.size()+1);
            }
            x = me1.getKey() * 100 / outputSet.size();
            y = (int)x + 1;
            frame.setProcessProgressBarValue(y);
            frame.setReductGraphEdgesLabelData(directedgraph.edgesCount.toString());
        }
        */
        // </editor-fold>  
        
        TreeMap<Integer, Integer> inputMap = new TreeMap<>();
        Multimap<Integer, Integer> outputMap = ArrayListMultimap.create();

        Set<Map.Entry<Integer, mAdic>> set1 = input.getMAdicSet();
        Set<Map.Entry<Integer, mAdic>> set2 = output.getMAdicSet();

        for (Map.Entry<Integer, mAdic> me1 : set1) {
            inputMap.put(Arrays.deepHashCode(me1.getValue().getDigits()), me1.getKey());
        }

        for (Map.Entry<Integer, mAdic> me2 : set2) {
            outputMap.put(Arrays.deepHashCode(me2.getValue().getDigits()), me2.getKey());
        }
        
        double x = 0;
        int y = 0;
        Integer z = 0;
        String k1, k2;
        ArrayList<Integer> al;
        
        for (Integer key : outputMap.keySet()) {
            if (inputMap.containsKey(key)) {
                al = new ArrayList<>(outputMap.get(key));
                for (Integer i : al) {
                    directedgraph.addEdge(Integer.toString(i), Integer.toString(inputMap.get(key)));
                    directedgraph.edgesCount++; // счетчик ребер графа
                }
            }
            z++;
            x = z * 100 / outputMap.size();
            y = (int)x + 1;
            frame.setProcessProgressBarValue(y);
            frame.setReductGraphEdgesLabelData(directedgraph.edgesCount.toString());
        }

        frame.setProcessProgressBarValue(100);
        frame.setIndeterminate(true);
        if (!frame.isCycleCalc) {
            frame.appendTextAreaText("Определение параметров графа ...");
        }
        directedgraph.makeAnalysis();
        graphCycles.add(directedgraph.cyclesCount);
        graphTails.add(directedgraph.thereTails);
        
        String cyclesData ="";
        
        if (directedgraph.cyclesCount > 0) {
            LinkedHashMap<Integer, Integer> cyclesInfo = new LinkedHashMap<>();
            for (Integer asd : directedgraph.cyclesLength) {
                if (cyclesInfo.containsKey(asd)) {
                    cyclesInfo.replace(asd, cyclesInfo.get(asd) + 1);
                }
                else {
                    cyclesInfo.put(asd, 1);
                }
            }
        
            Set<Map.Entry<Integer, Integer>> set = cyclesInfo.entrySet();
            for (Map.Entry<Integer, Integer> me : set) {
                cyclesData += me.getKey().toString() + "(" + me.getValue().toString() + "), ";
            }
            cyclesData = cyclesData.substring(0, cyclesData.length() - 2);
        }
        else {
            cyclesData = "0";
        }
        
        String tails = "";
        if (directedgraph.thereTails) {
            tails = "ДА";
        }
        else {
            tails = "НЕТ";
        }
        if (frame.isCycleCalc) {
            if (frame.getTabOutput()) {
                frame.appendTextAreaSimpleText(k + 
                        "\t" + directedgraph.edgesCount.toString() + 
                        "\t" + directedgraph.cyclesCount + "\t" + tails + 
                        "\t" + directedgraph.c.toString() + 
                        "\t" + cyclesData + 
                        "\t" + directedgraph.loopsCount);    
            }
            else {
                frame.appendTextAreaSimpleText("k = [" + k + 
                                    "] : Ребер = [" + directedgraph.edgesCount.toString() + 
                                    "]. Циклов = [" + directedgraph.cyclesCount + 
                                    "]. Есть хвосты = [" + tails + 
                                    "]. Длина циклов = [" + directedgraph.c +
                                    "]. Длины циклов : [" + cyclesData + 
                                    "]. Число петель = [" + directedgraph.loopsCount + "].");    
            }
            
            if (k == frame.getK()) {
                getMapParams();
            }
        }
        else {
            frame.appendTextAreaText("Посчитан граф редукции при k = " + k + 
                                    ".\nРебер графа = " + directedgraph.edgesCount.toString() + 
                                    ".\nЦиклов в графе = " + directedgraph.cyclesCount + 
                                    ".\nОбщая длина всех циклов в графе = " + directedgraph.c +
                                    ".\nЧисло петель в графе = " + directedgraph.loopsCount +
                                    ".\nДлины циклов в графе : " + cyclesData +
                                    ".\nЕсть хвосты = " + tails + ".");
        }
        frame.setIndeterminate(false);
    }
}