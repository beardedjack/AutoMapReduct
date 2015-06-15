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

import gui.AMRMain;
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

public class DirectedGraph {

    private HashMap<Integer, List<Integer>> vertexMap = new HashMap<>();
    
    public Integer edgesCount = 0; // счетчик ребер графа
    public Integer cyclesCount = 0; // число циклов
    public boolean thereTails = false; // есть хвосты на циклах
    public Integer c = 0; // длина циклов в графе
    public ArrayList<Integer> cyclesLength; // длины циклов
    public Integer loopsCount = 0; // число петель
    
    public AMRMain frame;
    
    public void addVertex(Integer vertexName) {
        if (!hasVertex(vertexName)) {
            vertexMap.put(vertexName, new ArrayList<>());
        }
    }
 
    public DirectedGraph(AMRMain someframe) {
        this.frame = someframe;
    }
    
    public DirectedGraph() {
        
    }
    
    public boolean hasVertex(Integer vertexName) {
        return vertexMap.containsKey(vertexName);
    }
 
    public boolean hasEdge(Integer vertexName1, Integer vertexName2) {
        if (!hasVertex(vertexName1)) return false;
        List<Integer> edges = vertexMap.get(vertexName1);
        return Collections.binarySearch(edges, vertexName2) != -1;
    }
 
    public void addEdge(Integer vertexName1, Integer vertexName2) {
        if (!hasVertex(vertexName1)) addVertex(vertexName1);
        if (!hasVertex(vertexName2)) addVertex(vertexName2);
        List<Integer> edges1 = vertexMap.get(vertexName1);
        edges1.add(vertexName2);
        Collections.sort(edges1);
    }
 
    public Map<Integer, List<Integer>> getVertexMap() {
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
        Set<Map.Entry<Integer, List<Integer>>> set = vertexMap.entrySet();
        for (Map.Entry<Integer, List<Integer>> me : set) {
            line = me.getKey().toString();
            List<Integer> to = me.getValue();
            if (to.isEmpty()) {
            bw.write(line);
                bw.newLine();
            }
            for (Integer s : to) {
                bw.write(line + "->" + s.toString());
                bw.newLine();
            }
        }
        bw.write("}");
        bw.close();
        
        if (k == 0) {
            dummy = "\\graphviz\\bin\\dot -Tjpg -o GraphAutomaton.jpg graphautomaton.dot"; 
        }
        else {dummy = "\\graphviz\\bin\\dot -Tjpg -o GraphReduct(k=" +
                Integer.toString(k) + ").jpg graphreduct.dot";
        }
        
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
   
    // Выполнение анализа: сколько циклов, есть ли хвосты...
    public void makeAnalysis() {
        // массив выходных вершин
        Integer vertexFrom [] = new Integer [edgesCount];
        // массив входных вершин
        Integer vertexTo [] = new Integer [edgesCount];
        loopsCount = 0; // количество циклов
        Integer e1, e2, e = 0;
        Set<Map.Entry<Integer, List<Integer>>> set = vertexMap.entrySet();
        // Запоняем массивы входных и выходных вершин
        for (Map.Entry<Integer, List<Integer>> me : set) {
            e1 = me.getKey();
            List<Integer> ls = me.getValue();
            for (Integer s: ls) {
                e2 = s;
                // Заполняем с учетом возможных петель
                if (Objects.equals(e1, e2)) {
                    loopsCount++;
                }
                vertexFrom [e] = e1;
                vertexTo [e] = e2;
                e++;
            }
        }
        
        // Находим все циклы в графе
        Integer vertexFromSorted [];
        Integer vertexToSorted [];
        Integer a = 0;
        Integer b = 0;
                
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
                
        c = 0;
        thereTails = a < e;
        
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

        // поиск количества циклов и определение длин самих циклов...
        used = new boolean[c];
        Arrays.fill(used, false);
        
        cyclesCount = 0;
        cyclesLength = new ArrayList<>();
        Integer aaa = 0;
        double x = 0;
        int y = 0;
        
        frame.setIndeterminate(false);
        frame.setSimpleProgressValue(0);
        
        for (Integer i = 1; i < c; i++) {
            if (!used[i]) {
                aaa = dfs(i);
                if (aaa != 1) {
                    cyclesLength.add(aaa);
                    cyclesCount++;
                }
            }
            x = i * 100 / c;
            y = (int)x + 1;
            frame.setSimpleProgressValue(y);
        }
        frame.setSimpleProgressValue(100);
        frame.setIndeterminate(true);
        frame.setDFScurrentElement(0);
        // Сортировка длин циклов для вывода
        Collections.sort(cyclesLength);
        // В обратном порядке
        Collections.reverse(cyclesLength);
        // Длина всех циклов за вычетом длин петель (по 1 на петлю)
        c = c - loopsCount;
    }

    private boolean used[];
    private Integer cycleVertexFrom[];
    private Integer cycleVertexTo[];
    
    private Integer dfs(Integer cur) {
        frame.setDFScurrentElement(cur);
        Integer next, a = 0;
        used[cur] = true;
        next = getIndex(cycleVertexTo[cur]); 
        if (!used[next]) {
            a = dfs(next);
        }
        a++;
        return a;
    }
    
    private Integer getIndex(Integer c) {
        Integer u = 0;
        for (Integer i = 0; i < used.length; i++) {
            if (Objects.equals(cycleVertexFrom[i], c)) {u = i;}
        }
        return u;
    }
}
