package automapreduct;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.io.*;
import java.util.StringTokenizer;

public class Automaton {
    // мапа всех состояний автомата с переходами/выходами по алфавиту 
    private TreeMap<Integer, List<TransitionOutput>> conditionMap = new TreeMap<Integer, List<TransitionOutput>>();
        
    public List<Integer> inputAlphabet = new ArrayList<Integer>();
    public List<Integer> outputAlphabet = new ArrayList<Integer>();
    public Integer alphabetsDimention =0;
    private int initialCondition = 1;
    
    // элемент таблицы переходов-выходов автомата (отдельно взятая ячейка)
    public class TransitionOutput {
                       
        Integer NextCondition; // следующее состояние
        Integer Output; // выход
        
        public TransitionOutput(int Condition, int Output) {
            this.NextCondition = Condition;
            this.Output = Output;
        }
    
    }
     
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
        bw.write("AlphabetsDimention:"+alphabetsDimention);
        
        
        
        
        /*
        for (Integer a : inputAlphabet) {
            line += a + ",";
        }
        bw.write(line.substring(0, line.length()-1));
        
        bw.newLine();
        bw.write("OutputAlphabet:");
        line = "";
        for (Integer a : outputAlphabet) {
            line += a + ",";
        }
        bw.write(line.substring(0, line.length()-1));
        */
        
        
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
    
    public String getOutputWord(String inputWord) {
        String outWord ="";
        int tmp = 0; 
        int currentCondition = 1;
        
        //System.out.println("inputWord=" + inputWord);
        
        Integer i = inputWord.length()-1;
        
        do {tmp = Character.getNumericValue(inputWord.charAt(i));
            outWord = Integer.toString(this.getOutput(tmp, currentCondition)) + outWord;
            // outWord += Integer.toString(this.getOutput(tmp, currentCondition));
            currentCondition = this.getCondition(tmp, currentCondition);
            i = i-1;
            
        } while (i>=0);
        
        /*
        for (Integer i=inputWord.length(); i== 0; i--) {
        //for (Integer i=0; i<inputWord.length(); i++) {
            tmp = Character.getNumericValue(inputWord.charAt(i));
            outWord = Integer.toString(this.getOutput(tmp, currentCondition)) + outWord;
            
           // outWord += Integer.toString(this.getOutput(tmp, currentCondition));
            currentCondition = this.getCondition(tmp, currentCondition);
        }
        */
        return outWord;
    }
        
    // Выдать степень числа
    public Integer getPow(Integer a, Integer b) {
        Integer res = 1;
        for (int i=1; i<=b; i++) {
            res *= a;
        }
        return res;
    }
    
    // Выдать редукцию
    public String getReduction(String input, Integer k) {
        String outWord ="";
        String o = input.substring(input.length()-k+1, input.length());
        for (Integer i=0; i<input.length()-k; i++) {
            outWord +="0 ";
        }
        
        
        for (Integer i=0; i<o.length(); i++) {
            outWord += "\t" + Integer.toString(Character.getNumericValue(o.charAt(i))*getPow(alphabetsDimention, o.length()-i));
           
        }
        
        return outWord;
    }
        
    // Выдать элемент выходного алфавита по номеру элемента входного алфавита и номеру состояния
    public Integer getOutput(Integer inputElement, Integer condition) {
        List<TransitionOutput> to = conditionMap.get(condition);
        return to.get(inputElement).Output;
    }
    
    // Выдать номер следующего состояния по номеру элемента входного алфавита и номеру состояния
    public Integer getCondition(Integer inputElement, Integer condition) {
        List<TransitionOutput> to = conditionMap.get(condition);
        return to.get(inputElement).NextCondition;
        
    }
    
    public boolean compareByModule(Integer x, Integer y, Integer k) {
        Integer c = getPow(alphabetsDimention, k);
        return (x%c==y%c);
    }
   
    public DirectedGraph makeGraph(Integer k) {
        DirectedGraph graph = new DirectedGraph();
        String binaryData;
        String outBinaryData;
        Integer outData = 0;
        
        TreeMap<Integer, Integer> dotList = new TreeMap<Integer, Integer>();
        
        
        //LinkedList<Integer> dotList = new LinkedList<Integer>();
        // Заполняем соответствия x-f(x)
        for (Integer i = 0; i < getPow(alphabetsDimention, k)-1; i++) {
            // Взяли число и перевели его в двоичную
            binaryData = Integer.toBinaryString(i);
            // Даем его автомату
            //System.out.println("=======>" + binaryData);
            outBinaryData = getOutputWord(binaryData);
            // Выход из автомата переводим в десятичную
            //System.out.println("=======>" + outBinaryData);
            outData = Integer.parseInt(outBinaryData, 2);
            // И добавляем его в коллекцию
            dotList.put(i, outData);
        }
        
        // Перебираем и заполняем граф
        Set<Map.Entry<Integer, Integer>> set1 = dotList.entrySet();
        Set<Map.Entry<Integer, Integer>> set2 = dotList.entrySet();
        
        for (Map.Entry<Integer, Integer> me1 : set1) {
            for (Map.Entry<Integer, Integer> me2 : set2) {
                //  f(x)=y (mod m^k)
                if (compareByModule(me1.getValue(), me2.getKey(), k)) {
                   graph.addEdge(Integer.toString(me1.getKey()), Integer.toString(me2.getKey()));
                }
            }
        }
        
        
        return graph;

}
    


}