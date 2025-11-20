import java.util.*;
import java.util.stream.Collectors;

public class GCModel {
    static class GCObject {
        int id;
        GCObject[] fields = new GCObject[26];
        
        GCObject(int id) {
            this.id = id;
        }
    }
    
    private static GCObject[] globals = new GCObject[26];
    private static Map<Integer, GCObject> allObjects = new HashMap<>();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            processAssignment(line);
        }
        
        Set<GCObject> reachable = findReachableObjects();
        List<Integer> unreachableIds = findUnreachableIds(reachable);
        
        System.out.println(unreachableIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" ")));
    }
    
    private static void processAssignment(String assignment) {
        String[] parts = assignment.split("=");
        String left = parts[0];
        String right = parts[1];
        
        GCObject value = resolveRightValue(right);
        assignToLeft(left, value);
    }
    
    private static GCObject resolveRightValue(String expr) {
        if ("0".equals(expr)) {
            return null;
        }
        if (expr.startsWith("@")) {
            int id = Integer.parseInt(expr.substring(1));
            GCObject obj = new GCObject(id);
            allObjects.put(id, obj);
            return obj;
        }
        
        String[] path = expr.split("\\.");
        GCObject current = globals[path[0].charAt(0) - 'a'];
        
        for (int i = 1; i < path.length && current != null; i++) {
            current = current.fields[path[i].charAt(0) - 'a'];
        }
        
        return current;
    }
    
    private static void assignToLeft(String target, GCObject value) {
        String[] path = target.split("\\.");
        
        if (path.length == 1) {
            globals[target.charAt(0) - 'a'] = value;
            return;
        }
        
        GCObject current = globals[path[0].charAt(0) - 'a'];
        for (int i = 1; i < path.length - 1 && current != null; i++) {
            current = current.fields[path[i].charAt(0) - 'a'];
        }
        
        if (current != null) {
            int fieldIndex = path[path.length - 1].charAt(0) - 'a';
            current.fields[fieldIndex] = value;
        }
    }
    
    private static Set<GCObject> findReachableObjects() {
        Set<GCObject> visited = new HashSet<>();
        Queue<GCObject> queue = new LinkedList<>();
        
        for (GCObject global : globals) {
            if (global != null && !visited.contains(global)) {
                visited.add(global);
                queue.add(global);
            }
        }
        
        while (!queue.isEmpty()) {
            GCObject current = queue.poll();
            
            for (GCObject field : current.fields) {
                if (field != null && !visited.contains(field)) {
                    visited.add(field);
                    queue.add(field);
                }
            }
        }
        
        return visited;
    }
    
    private static List<Integer> findUnreachableIds(Set<GCObject> reachable) {
        return allObjects.values().stream()
                .filter(obj -> !reachable.contains(obj))
                .map(obj -> obj.id)
                .sorted()
                .collect(Collectors.toList());
    }
}
