import java.util.*;
import java.io.*;

public class PascalCompilerGoStyle {
    static class Node {
        String name;
        int color; // 0 = BLACK, 1 = RED
        int pas;
        int dcu;
        List<String> imports;
        
        Node(String name) {
            this.name = name;
            this.color = 0; // BLACK
            this.pas = -1;
            this.dcu = -1;
            this.imports = new ArrayList<>();
        }
    }
    
    static Map<String, Node> graph;
    static boolean cycleFound = false;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int N = scanner.nextInt();
        graph = new HashMap<>();
        
        for (int i = 0; i < N; i++) {
            String source = scanner.next();
            int k = scanner.nextInt();
            Node node = new Node(source);
            for (int j = 0; j < k; j++) {
                node.imports.add(scanner.next());
            }
            graph.put(source, node);
        }
        
        int M = scanner.nextInt();
        for (int i = 0; i < M; i++) {
            String fileName = scanner.next();
            int timestamp = scanner.nextInt();
            String[] parts = fileName.split("\\.");
            String source = parts[0];
            String extension = parts[1];
            
            if (extension.equals("pas")) {
                if (graph.containsKey(source)) {
                    graph.get(source).pas = timestamp;
                }
            } else if (extension.equals("dcu")) {
                if (graph.containsKey(source)) {
                    graph.get(source).dcu = timestamp;
                }
            }
        }
        
        // Проверка циклов
        if (findCycles()) {
            System.out.println("!CYCLE");
            return;
        }
        
        // Многократный вызов visit (как в Go-решении)
        for (int i = 0; i < N; i++) {
            visit("main");
        }
        
        // Сбор результатов
        List<String> answer = new ArrayList<>();
        for (Node node : graph.values()) {
            if (node.color == 1) { // RED
                answer.add(node.name + ".pas");
            }
        }
        
        Collections.sort(answer);
        for (String res : answer) {
            System.out.println(res);
        }
    }
    
    static void visit(String source) {
        Node unit = graph.get(source);
        
        // Условие 1: нет .dcu файла
        if (unit.dcu == -1) {
            unit.color = 1; // RED
        }
        
        // Условие 2: .pas новее .dcu
        if (unit.dcu < unit.pas) {
            unit.color = 1; // RED
        }
        
        // Обработка зависимостей
        for (String dependency : unit.imports) {
            if (dependency.equals(unit.name)) {
                continue;
            }
            
            Node depNode = graph.get(dependency);
            
            // Логика из Go-решения: если зависимость красная ИЛИ её .dcu новее нашего .dcu
            if (depNode.color == 1 || depNode.dcu > unit.dcu) {
                unit.color = 1; // RED
            }
            
            visit(dependency);
        }
    }
    
    static boolean findCycles() {
        Map<String, Integer> color = new HashMap<>();
        for (String node : graph.keySet()) {
            color.put(node, 0); // 0 = WHITE, 1 = GRAY, 2 = BLACK
        }
        
        final boolean[] ok = {false};
        
        for (String node : graph.keySet()) {
            if (color.get(node) == 0) {
                dfsCycle(node, color, ok);
            }
        }
        
        return ok[0];
    }
    
    static void dfsCycle(String source, Map<String, Integer> color, boolean[] ok) {
        color.put(source, 1); // GRAY
        
        for (String dependency : graph.get(source).imports) {
            if (color.get(dependency) == 1) { // GRAY - цикл найден
                ok[0] = true;
            }
            if (color.get(dependency) == 0) { // WHITE - не посещен
                dfsCycle(dependency, color, ok);
            }
        }
        
        color.put(source, 2); // BLACK
    }
}
