import java.util.*;

public class CourseScheduler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[n + 1];
        
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int i = 1; i <= n; i++) {
            int k = scanner.nextInt();
            if (k == 0) {
                continue;
            }
            for (int j = 0; j < k; j++) {
                int prerequisite = scanner.nextInt();
                graph.get(prerequisite).add(i);
                inDegree[i]++;
            }
        }
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }
        
        int semesters = 0;
        int count = 0;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                count++;
                for (int neighbor : graph.get(current)) {
                    inDegree[neighbor]--;
                    if (inDegree[neighbor] == 0) {
                        queue.add(neighbor);
                    }
                }
            }
            semesters++;
        }
        
        if (count != n) {
            System.out.println("cycle");
        } else {
            System.out.println(semesters);
        }
    }
}
