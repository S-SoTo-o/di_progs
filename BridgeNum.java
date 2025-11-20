import java.util.ArrayList;
import java.util.Scanner;

public class BridgeNum {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int m = scan.nextInt();

        ArrayList<ArrayList<Integer>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; ++i) {
            int u = scan.nextInt();
            int v = scan.nextInt();
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        int bridges = countBridges(graph, n);
        System.out.println(bridges);
        scan.close();
    }

    static int countBridges(ArrayList<ArrayList<Integer>> graph, int n) {
        int bridges = 0;
        boolean[] c = new boolean[n];
        int[] s = new int[n];
        int[] l = new int[n];
        int t = 0;

        for (int i = 0; i < n; ++i) {
            if (!c[i]) {
                bridges += dfs(graph, i, -1, c, s, l, t);
            }
        }
        return bridges;
    }

    static int dfs(ArrayList<ArrayList<Integer>> graph, int v, int p, boolean[] c, int[] s, int[] l, int t) {
        c[v] = true;
        s[v] = l[v] = t++;
        int bridges = 0;
        for (int to : graph.get(v)) {
            if (to == p) continue;
            if (c[to]) {
                l[v] = Math.min(l[v], s[to]);
            } else {
                int childBridges = dfs(graph, to, v, c, s, l, t);
                l[v] = Math.min(l[v], l[to]);
                if (l[to] > s[v]) {
                    bridges++;
                }
                bridges += childBridges;
            }
        }
        return bridges;
    }
}
