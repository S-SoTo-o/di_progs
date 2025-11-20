import java.util.*;

public class GraphBase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int N = scanner.nextInt();
        int M = scanner.nextInt();

        List<List<Integer>> graph = new ArrayList<>();
        List<List<Integer>> reverseGraph = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            graph.add(new ArrayList<>());
            reverseGraph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            graph.get(u).add(v);
            reverseGraph.get(v).add(u);
        }

        boolean[] visited = new boolean[N];
        List<Integer> order = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            if (!visited[i]) {
                dfs1(i, graph, visited, order);
            }
        }

        visited = new boolean[N];
        int[] componentId = new int[N];
        List<List<Integer>> components = new ArrayList<>();
        int currentComponent = 0;

        for (int i = N - 1; i >= 0; i--) {
            int node = order.get(i);
            if (!visited[node]) {
                List<Integer> currentComp = new ArrayList<>();
                dfs2(node, reverseGraph, visited, componentId, currentComp, currentComponent);
                components.add(currentComp);
                currentComponent++;
            }
        }

        int compCount = components.size();
        boolean[] hasIncomingEdge = new boolean[compCount];

        for (int i = 0; i < N; i++) {
            for (int neighbor : graph.get(i)) {
                int compFrom = componentId[i];
                int compTo = componentId[neighbor];
                if (compFrom != compTo) {
                    hasIncomingEdge[compTo] = true;
                }
            }
        }

        List<Integer> baseComponents = new ArrayList<>();
        for (int i = 0; i < compCount; i++) {
            if (!hasIncomingEdge[i]) {
                baseComponents.add(i);
            }
        }

        List<Integer> answer = new ArrayList<>();
        for (int compIndex : baseComponents) {
            List<Integer> comp = components.get(compIndex);
            int minVertex = comp.get(0);
            for (int vertex : comp) {
                if (vertex < minVertex) {
                    minVertex = vertex;
                }
            }
            answer.add(minVertex);
        }

        Collections.sort(answer);

        for (int i = 0; i < answer.size(); i++) {
            System.out.print(answer.get(i));
            if (i < answer.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    // первый обход в глубину
    static void dfs1(int node, List<List<Integer>> graph, boolean[] visited, List<Integer> order) {
        visited[node] = true;
        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                dfs1(neighbor, graph, visited, order);
            }
        }
        order.add(node);
    }

    static void dfs2(int node, List<List<Integer>> reverseGraph, boolean[] visited,
                     int[] componentId, List<Integer> currentComp, int compId) {
        visited[node] = true;
        componentId[node] = compId;
        currentComp.add(node);
        for (int neighbor : reverseGraph.get(node)) {
            if (!visited[neighbor]) {
                dfs2(neighbor, reverseGraph, visited, componentId, currentComp, compId);
            }
        }
    }
}
