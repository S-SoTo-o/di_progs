import java.util.*;
import java.util.stream.Collectors;

public class MinMealy {

    static class DSU {
        private int[] parent;
        private int[] rank;
        private int count;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
            count = n;
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return;

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            count--;
        }

        public int getCount() {
            return count;
        }
    }

    static class Transition {
        int from;
        int to;
        char input;
        String output;

        Transition(int from, int to, char input, String output) {
            this.from = from;
            this.to = to;
            this.input = input;
            this.output = output;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int q0 = scanner.nextInt();

        int[][] delta = new int[n][m];
        String[][] phi = new String[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                delta[i][j] = scanner.nextInt();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                phi[i][j] = scanner.next();
            }
        }

        int[] pi = new int[n];
        int classesCount = minimizeMealy(n, m, delta, phi, pi);
        printDOT(n, m, q0, delta, phi, pi, classesCount);
    }

    private static int minimizeMealy(int n, int m, int[][] delta, String[][] phi, int[] pi) {
        int classesCount = split1(n, m, phi, pi);
        int oldClassesCount;
        do {
            oldClassesCount = classesCount;
            classesCount = split(n, m, delta, pi, classesCount);
        } while (classesCount != oldClassesCount);
        return classesCount;
    }

    private static int split1(int n, int m, String[][] phi, int[] pi) {
        DSU dsu = new DSU(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (dsu.find(i) != dsu.find(j)) {
                    boolean equivalent = true;
                    for (int x = 0; x < m; x++) {
                        if (!phi[i][x].equals(phi[j][x])) {
                            equivalent = false;
                            break;
                        }
                    }
                    if (equivalent) {
                        dsu.union(i, j);
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            pi[i] = dsu.find(i);
        }
        return dsu.getCount();
    }

    private static int split(int n, int m, int[][] delta, int[] pi, int currentClasses) {
        DSU dsu = new DSU(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (pi[i] == pi[j] && dsu.find(i) != dsu.find(j)) {
                    boolean equivalent = true;
                    for (int x = 0; x < m; x++) {
                        int next1 = delta[i][x];
                        int next2 = delta[j][x];
                        if (pi[next1] != pi[next2]) {
                            equivalent = false;
                            break;
                        }
                    }
                    if (equivalent) {
                        dsu.union(i, j);
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            pi[i] = dsu.find(i);
        }
        return dsu.getCount();
    }

    private static void printDOT(int n, int m, int q0,
                                 int[][] delta, String[][] phi,
                                 int[] pi, int classesCount) {
        Map<Integer, Integer> classToMinState = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int classRep = pi[i];
            if (!classToMinState.containsKey(classRep) || i < classToMinState.get(classRep)) {
                classToMinState.put(classRep, i);
            }
        }

        Map<Integer, Integer> classToNewState = new HashMap<>();
        int initialClass = pi[q0];
        classToNewState.put(initialClass, 0);
        dfs(initialClass, classToNewState, classToMinState, delta, pi, m);

        System.out.println("digraph {");
        System.out.println("    rankdir = LR");

        List<Transition> transitions = new ArrayList<>();
        for (int classRep : classToNewState.keySet()) {
            int newStateFrom = classToNewState.get(classRep);
            int representative = classToMinState.get(classRep);
            for (int x = 0; x < m; x++) {
                char inputChar = (char) ('a' + x);
                String output = phi[representative][x];
                int nextOldState = delta[representative][x];
                int nextClass = pi[nextOldState];
                int newStateTo = classToNewState.get(nextClass);
                transitions.add(new Transition(newStateFrom, newStateTo, inputChar, output));
            }
        }

        transitions.sort((t1, t2) -> {
            if (t1.from != t2.from) {
                return Integer.compare(t1.from, t2.from);
            }
            return Character.compare(t1.input, t2.input);
        });

        for (Transition t : transitions) {
            System.out.println("    " + t.from + " -> "
                    + t.to + " [label = \"" +
                    t.input + "(" + t.output + ")\"]");
        }

        System.out.println("}");
    }

    private static void dfs(int classRep, Map<Integer,
            Integer> classToNewState, Map<Integer,
            Integer> classToMinState, int[][] delta, int[] pi, int m) {
        int representative = classToMinState.get(classRep);
        for (int x = 0; x < m; x++) {
            int nextOldState = delta[representative][x];
            int nextClass = pi[nextOldState];
            if (!classToNewState.containsKey(nextClass)) {
                int newState = classToNewState.size();
                classToNewState.put(nextClass, newState);
                dfs(nextClass, classToNewState, classToMinState, delta, pi, m);
            }
        }
    }
}
