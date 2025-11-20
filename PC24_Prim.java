import java.util.*;
import java.io.*;
// ver scheme + task
public class PascalCompiler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int N = scanner.nextInt();
        Map<String, List<String>> graph = new HashMap<>();
        List<String> modules = new ArrayList<>();
        
        for (int i = 0; i < N; i++) {
            String module = scanner.next();
            modules.add(module);
            int k = scanner.nextInt();
            List<String> dependencies = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                dependencies.add(scanner.next());
            }
            graph.put(module, dependencies);
        }
        
        int M = scanner.nextInt();
        Map<String, Long> pasTimestamps = new HashMap<>();
        Map<String, Long> dcuTimestamps = new HashMap<>();
        
        for (int i = 0; i < M; i++) {
            String filename = scanner.next();
            long timestamp = scanner.nextLong();
            if (filename.endsWith(".pas")) {
                String module = filename.substring(0, filename.length() - 4);
                pasTimestamps.put(module, timestamp);
            } else if (filename.endsWith(".dcu")) {
                String module = filename.substring(0, filename.length() - 4);
                dcuTimestamps.put(module, timestamp);
            }
        }
        
        // Проверка на циклы с помощью топологической сортировки
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        
        for (String module : modules) {
            inDegree.put(module, 0);
            reverseGraph.put(module, new ArrayList<>());
        }
        
        for (String module : graph.keySet()) {
            for (String dep : graph.get(module)) {
                reverseGraph.get(dep).add(module);
                inDegree.put(module, inDegree.get(module) + 1);
            }
        }
        
        Queue<String> queue = new LinkedList<>();
        for (String module : modules) {
            if (inDegree.get(module) == 0) {
                queue.add(module);
            }
        }
        
        List<String> topologicalOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            topologicalOrder.add(current);
            for (String neighbor : reverseGraph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }
        
        if (topologicalOrder.size() != N) {
            System.out.println("!CYCLE");
            return;
        }
        
        // Определение необходимости перекомпиляции
        Map<String, Boolean> recompile = new HashMap<>();
        
        // Сначала проверяем условия 1 и 2 для всех модулей
        for (String module : modules) {
            boolean needRecompile = false;
            
            // Условие 1: нет .dcu файла
            if (!dcuTimestamps.containsKey(module)) {
                needRecompile = true;
            }
            // Условие 2: .pas новее .dcu
            else if (pasTimestamps.containsKey(module) && 
                    pasTimestamps.get(module) > dcuTimestamps.get(module)) {
                needRecompile = true;
            }
            
            recompile.put(module, needRecompile);
        }
        
        // main.pas всегда перекомпилируется
        recompile.put("main", true);
        
        // Распространение перекомпиляции по зависимостям (условие 3)
        // В топологическом порядке от зависимостей к зависящим
        for (String module : topologicalOrder) {
            if (recompile.get(module)) {
                // Если модуль перекомпилируется, все зависящие от него тоже
                for (String dependent : reverseGraph.get(module)) {
                    recompile.put(dependent, true);
                }
            }
        }
        
        // Сбор и вывод результатов
        List<String> result = new ArrayList<>();
        for (String module : modules) {
            if (recompile.get(module)) {
                result.add(module + ".pas");
            }
        }
        
        Collections.sort(result);
        for (String res : result) {
            System.out.println(res);
        }
    }
}
