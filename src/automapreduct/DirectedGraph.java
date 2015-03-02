package automapreduct;

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
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class DirectedGraph {
    
    private HashMap<String, List<String>> vertexMap = new HashMap<String, List<String>>();
 
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
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
        String line;
        bw.write("digraph G{");
        bw.newLine();
        
        Set<Map.Entry<String, List<String>>> set = vertexMap.entrySet();
        for (Map.Entry<String, List<String>> me : set) {
            line = me.getKey();
            List<String> to = me.getValue();
            for (String s : to) {
                bw.write(line + "->" + s);
                bw.newLine();
            }
        }
        bw.write("}");
        bw.close();
        
        // Делаем графический файл
        try {
            Process process = Runtime.getRuntime().
            exec("C:\\graphviz\\bin\\dot -Tjpg -o graph(k=" + k + ").jpg graph.dot");
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();
 
            } catch (IOException e) {
            // handle exception.
        }
        
        
        
    }
    
}
