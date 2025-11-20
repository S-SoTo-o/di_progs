import java.util.*;
import java.util.stream.*;

public class MaxComponent {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        List<List<Integer>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        List<Edge> edges = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            edges.add(new Edge(u, v));
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        scanner.close();

        List<ComponentInfo> components = findConnectedComponents(n, graph);
        int bestComponentIndex = findBestComponent(components);

        Set<Integer> bestComponentVertices = components.get(bestComponentIndex).vertices;

        System.out.println("graph {");
        for (int i = 0; i < n; i++) {
            if (bestComponentVertices.contains(i)) {
                System.out.println("    " + i + " [color=red];");
            } else {
                System.out.println("    " + i + ";");
            }
        }
        for (Edge edge : edges) {
            if (bestComponentVertices.contains(edge.u) && bestComponentVertices.contains(edge.v)) {
                System.out.println("    " + edge.u + " -- " + edge.v + " [color=red];");
            } else {
                System.out.println("    " + edge.u + " -- " + edge.v + ";");
            }
        }
        System.out.println("}");
    }

    static List<ComponentInfo> findConnectedComponents(int n, List<List<Integer>> graph) {
        List<ComponentInfo> components = new ArrayList<>();
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                ComponentInfo component = bfs(i, visited, graph);
                components.add(component);
            }
        }
        return components;
    }

    static ComponentInfo bfs(int start, boolean[] visited, List<List<Integer>> graph) {
        Set<Integer> vertices = new HashSet<>();
        int edgesCount = 0;
        int minVertex = Integer.MAX_VALUE;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            vertices.add(current);
            minVertex = Math.min(minVertex, current);

            edgesCount += graph.get(current).size();

            for (int neighbor : graph.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }

        edgesCount /= 2;
        return new ComponentInfo(vertices, edgesCount, minVertex);
    }

    static int findBestComponent(List<ComponentInfo> components) {
        int bestIndex = 0;

        for (int i = 1; i < components.size(); i++) {
            ComponentInfo current = components.get(i);
            ComponentInfo best = components.get(bestIndex);

            if (current.vertices.size() > best.vertices.size()) {
                bestIndex = i;
            } else if (current.vertices.size() == best.vertices.size()) {
                if (current.edgesCount > best.edgesCount) {
                    bestIndex = i;
                } else if (current.edgesCount == best.edgesCount) {
                    if (current.minVertex < best.minVertex) {
                        bestIndex = i;
                    }
                }
            }
        }

        return bestIndex;
    }

    static class ComponentInfo {
        Set<Integer> vertices;
        int edgesCount;
        int minVertex;

        ComponentInfo(Set<Integer> vertices, int edgesCount, int minVertex) {
            this.vertices = vertices;
            this.edgesCount = edgesCount;
            this.minVertex = minVertex;
        }
    }

    static class Edge {
        int u;
        int v;

        Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }
}
