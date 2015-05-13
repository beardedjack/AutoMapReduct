package core;

/*
Внутреннее представление графа - список смежности
Вершины однозначно идентифицируются строковыми идентификаторами (именами).
Не допускается двух вершин с одинаковым именем
Список смежных вершин держится отсортированным, 
так чтобы можно было быстро найти вершину в списке по имени
(для поиска в отсортированном списке используется двоичный поиск)
Для того, чтобы добавить ребро между двумя вершинами,
необязательно предварительно добавлять вершины в граф.
Несуществующие вершины будут созданы автоматически.
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import static java.lang.String.format;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;

public class DirectedGraph {
    
    private HashMap<String, List<String>> vertexMap = new HashMap<String, List<String>>();
    
    public Integer edgesCount = 0; // счетчик ребер графа
    
    public void addVertex(String vertexName) {
        if (!hasVertex(vertexName)) {
            vertexMap.put(vertexName, new ArrayList<String>());
        }
    }
 
    public boolean hasVertex(String vertexName) {
        return vertexMap.containsKey(vertexName);
    }
 
    public boolean hasEdge(String vertexName1, String vertexName2) {
        if (!hasVertex(vertexName1)) return false;
        List<String> edges = vertexMap.get(vertexName1);
        return Collections.binarySearch(edges, vertexName2) != -1;
    }
 
    public void addEdge(String vertexName1, String vertexName2) {
        if (!hasVertex(vertexName1)) addVertex(vertexName1);
        if (!hasVertex(vertexName2)) addVertex(vertexName2);
        List<String> edges1 = vertexMap.get(vertexName1);
        edges1.add(vertexName2);
        Collections.sort(edges1);
    }
 
    public Map<String, List<String>> getVertexMap() {
        return vertexMap;
    }
    
    // Делаем файл для графвиза
    public void makeFile(String filename, int k) throws FileNotFoundException, IOException {
        File f = new File(filename);
        String dummy;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
        String line;
        bw.write("digraph G{");
        bw.newLine();
        bw.write(format("label=\"(k = %d)\"", k));
        bw.newLine();
        bw.write("node[shape=\"circle\"]");
        bw.newLine();
        Set<Map.Entry<String, List<String>>> set = vertexMap.entrySet();
        for (Map.Entry<String, List<String>> me : set) {
            line = me.getKey();
            List<String> to = me.getValue();
            
            if (to.size()==0) {
            bw.write(line);
                bw.newLine();
            }
            for (String s : to) {
                bw.write(line + "->" + s);
                bw.newLine();
            }
        }
        bw.write("}");
        bw.close();
        
        if (k == 0) {dummy = "C:\\graphviz\\bin\\dot -Tjpg -o GraphAutomaton.jpg graphautomaton.dot"; }
        else {dummy = "C:\\graphviz\\bin\\dot -Tjpg -o GraphReduct(k=" + Integer.toString(k) + ").jpg graphreduct.dot"; }
        
        // Делаем графический файл
        try {
            Process process = Runtime.getRuntime().
            exec(dummy);
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();
 
            } catch (IOException e) {
            // handle exception.
        }
      
        
    }
   
    public void makeAnalysis() {
        // массив выходных вершин
        Integer vertexFrom [] = new Integer [edgesCount];
        // массив входных вершин
        Integer vertexTo [] = new Integer [edgesCount];
        Integer e1, e2, e = 0;
        Set<Map.Entry<String, List<String>>> set = vertexMap.entrySet();
        // Запоняем массивы входных и выходных вершин
        for (Map.Entry<String, List<String>> me : set) {
            e1 = Integer.valueOf(me.getKey());
            List<String> ls = me.getValue();
            for (String s: ls) {
                e2 = Integer.valueOf(s);
                // заполняем с учетом возможных петель
                if (e1 != e2) {
                    vertexFrom [e] = e1;
                    vertexTo [e] = e2;
                }
                else {
                    vertexFrom [e] = -1;
                    vertexTo [e] = -1; 
                }
                e++;
            }
        }
        
        // Находим все циклы в графе
        
        Integer vertexFromSorted [];
        Integer vertexToSorted [];
        Integer a = 0, b = 0;
                
        do {    
            b = a;
            vertexToSorted = Arrays.copyOf(vertexTo, edgesCount);
            vertexFromSorted = Arrays.copyOf(vertexFrom, edgesCount);
            Arrays.sort(vertexFromSorted);
            Arrays.sort(vertexToSorted);
        
            for (Integer i = 0; i < edgesCount; i++) {
                if (((Arrays.binarySearch(vertexToSorted, vertexFrom[i]) < 0) | (vertexFrom[i] < 0)) || 
                        ((Arrays.binarySearch(vertexFromSorted, vertexTo[i]) < 0) | (vertexTo[i] < 0))) {
                    vertexFrom[i] = -1;
                    vertexTo[i] = -1;
                }
            }

            a = 0;
            for (Integer i = 0; i < edgesCount; i++) {
                if  (vertexFrom[i] != -1 | vertexTo[i] != -1) {
                    a++;
                }
            }
            
        } while (!Objects.equals(a, b));
                
        Integer c = 0;
        
       // новые массивы, содержащие только циклы:
        cycleVertexFrom = new Integer[a]; // из вершины
        cycleVertexTo = new Integer[a]; // в вершину
        
        for (Integer i = 0; i < edgesCount; i++) {
            if  (vertexFrom[i] != -1 | vertexTo[i] != -1) {
                cycleVertexFrom[c] = vertexFrom[i];
                cycleVertexTo[c] = vertexTo[i];
                c++;
            }
        }

        // поиск количества циклов...
        
        used = new boolean[c];
        Arrays.fill(used, false);
        Integer cyclesCount = 0;
        
        for (Integer i = 0; i<c; i++) {
        // for (Integer i = 1; i<c; ++i) {
            if (!used[i]) {
                dfs(i);
                ++cyclesCount;
            }
        }
        
        System.out.println("Циклов:" + cyclesCount);
    }

    private boolean used[];
    private Integer cycleVertexFrom[];
    private Integer cycleVertexTo[];
    
    private void dfs(Integer cur) {
        Integer next;
        used[cur] = true;
            next = getIndex(cycleVertexTo[cur]); 
            if (!used[next]) {
                dfs(next);
            }
    }
    
    private Integer getIndex(Integer c) {
        Integer u=0;
        for (Integer i = 0; i < used.length; i++) {
            if (Objects.equals(cycleVertexFrom[i], c)) {u=i;}
        }
        return u;
    }
    
}
