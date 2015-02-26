package automapreduct;

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

public class Automaton {

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

    // мапа всех состояний автомата с переходами/выходами по алфавиту 
    private TreeMap<Integer, List<TransitionOutput>> conditionMap = new TreeMap<Integer, List<TransitionOutput>>();
     
    // проверка на наличие состояния с таким номером
    public boolean hasCondition(Integer conditionNumber) {
        return conditionMap.containsKey(conditionNumber);
    }
     
     // добавление нового состояния
    public void addCondition(Integer transitionNumber, ArrayList<TransitionOutput> e) {
        if (!hasCondition(transitionNumber)) {
            
            conditionMap.put(transitionNumber, new ArrayList<TransitionOutput>(e));
            
            // добавление всех переходов/выходов по каждому символу входного алфавита для данного состояния
            /*
            List<TransitionOutput> to = conditionMap.get(transitionNumber);
            for (TransitionOutput c : e) {
                to.add(c);
            }
            */
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
    
    // Минимизация автомата
    public void /*Automaton*/ getMinimized() {
        
        int cc = 1;
        int co = 1;
        String so = "";
                
        TreeMap<Integer, ArrayList<Integer>> fc = new TreeMap<Integer, ArrayList<Integer>>();
        TreeMap<Integer, ArrayList<Integer>> equalClasses = new TreeMap<Integer, ArrayList<Integer>>();
        
        
        Set<Map.Entry<Integer, List<TransitionOutput>>> set = conditionMap.entrySet();
      
        // Первичное разбиение
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
        
        
        
        int equClassNum = 1;
        int c;
        
        
        Set<Map.Entry<Integer, ArrayList<Integer>>> p0 = fc.entrySet();
        Set<Map.Entry<Integer, ArrayList<Integer>>> p1 = fc.entrySet();
        
 /*
 Можно сделать так

for(Iterator<Role> roleIter = roles.iterator(); roleIter.hasNext();){
 Role currentRole = roleIter.next();
  if (roleId.equals(currentRole.getId())) {
     roleIter.remove();
   }
}

а можно еще так. Так как удаляется всего один элемент то достаточно после удаления поставить return из фукнции

private function deleteRole(List<Role> roles)
for (Role givenRole : roles) {
  if (roleId.equals(givenRole.getId())) {
     roles.remove(givenRole);
     return;
   }
 }
}
 */
        
        
        
 
        

        
                
        
        for (Map.Entry<Integer, ArrayList<Integer>> s0 : p0) { //перебираем созданную ранее мапу
            equalClasses.put(equClassNum, new ArrayList<Integer>());
            ArrayList<Integer> al0 = s0.getValue(); // <=== взяли первый класс
            if (al0.size()!=0) {
            for (Integer i : al0) {
                for (Map.Entry<Integer, ArrayList<Integer>> s1 : p1) {
                    c = s1.getKey(); //текущий класс
                    ArrayList<Integer> al1 = s1.getValue();
                    for (Integer j : al1) {
                        if (i==j) {
                            ArrayList<Integer> a = equalClasses.get(equClassNum);
                            a.add(j);
                            ArrayList<Integer> b = fc.get(c);
                            b.remove(b.indexOf(j));
                            
                        }
                    }
                }
            }
        }
            equClassNum++;
        }
        

        
        
        // Отобразить разбиения
        Set<Map.Entry<Integer, ArrayList<Integer>>> pppp = equalClasses.entrySet();
        for (Map.Entry<Integer, ArrayList<Integer>> sssss : pppp) {
            System.out.print(sssss.getKey() + ": ");
            ArrayList<Integer> aaaaa = sssss.getValue();
            for (Integer iiiii : aaaaa) {
                System.out.print(iiiii + ", ");
            }
            System.out.print("\n");
        }
        
        
        
        /*
        // Отобразить разбиения
        boolean showPartitions = true;
        if (showPartitions) {
            System.out.println("Первичное разбиение:");
            Set<Map.Entry<Integer, ArrayList<Integer>>> sss = fc.entrySet();
            for (Map.Entry<Integer, ArrayList<Integer>> ss : sss) {
                System.out.print(ss.getKey() + ": ");
                ArrayList<Integer> llll = ss.getValue();
                for (Integer qwer : llll) {
                    System.out.print(qwer.intValue() + " ");
                }
                System.out.print("\n");
            }
        }
        */
        
        
        
        
        /*
        Automaton minAutomaton = new Automaton();
        return minAutomaton;
        */
    }  

}
    


