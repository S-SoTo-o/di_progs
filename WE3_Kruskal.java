import java.util.*;

public class Kruskal {

    static class Edge implements Comparable<Edge> {
        int a, b;
        double ves;

        Edge(int a, int b, double ves) {
            this.a = a;
            this.b = b;
            this.ves = ves;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.ves, other.ves);
        }
    }

    static class Set {
        int[] parent;
        int[] rang;

        Set(int n) {
            parent = new int[n];
            rang = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rang[i] = 0;
            }
        }

        int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }

        void unite(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (rang[rootI] < rang[rootJ]) {
                    parent[rootI] = rootJ;
                } else if (rang[rootI] > rang[rootJ]) {
                    parent[rootJ] = rootI;
                } else {
                    parent[rootJ] = rootI;
                    rang[rootI]++;
                }
            }
        }
    }

    public static double distance(int[] p1, int[] p2) {
        return Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2));
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int[][] coords = new int[n][2];
        for (int i = 0; i < n; i++) {
            coords[i][0] = scan.nextInt();
            coords[i][1] = scan.nextInt();
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double weight = distance(coords[i], coords[j]);
                edges.add(new Edge(i, j, weight));
            }
        }

        Collections.sort(edges);

        Set un = new Set(n);
        double minLen = 0;
        for (Edge edge : edges) {
            if (un.find(edge.a) != un.find(edge.b)) {
                un.unite(edge.a, edge.b);
                minLen += edge.ves;
            }
        }

        System.out.printf("%.2f\n", minLen);
        scan.close();
    }
}
