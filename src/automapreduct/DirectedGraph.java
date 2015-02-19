/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automapreduct;

/**
 *
 * @author kondrashov
 */

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

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

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
}