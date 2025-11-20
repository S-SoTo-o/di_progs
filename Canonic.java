import java.util.*;
import java.io.*;

public class Canonic {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int q0 = in.nextInt();
        int[][] transitions = new int[n][m];
        String[][] outputs = new String[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                transitions[i][j] = in.nextInt();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                outputs[i][j] = in.next();
            }
        }

        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();

        stack.push(q0);

        while (!stack.isEmpty()) {
            int s = stack.pop();
            if (visited[s]) continue;
            visited[s] = true;
            order.add(s);
            for (int i = m - 1; i >= 0; i--) {
                int next = transitions[s][i];
                if (!visited[next]) {
                    stack.push(next);
                }
            }
        }

        int[] newIndex = new int[n];
        for (int i = 0; i < n; i++) {
            newIndex[order.get(i)] = i;
        }

        int[][] newTransitions = new int[n][m];
        String[][] newOutputs = new String[n][m];

        for (int i = 0; i < n; i++) {
            int oldState = order.get(i);
            for (int j = 0; j < m; j++) {
                newTransitions[i][j] = newIndex[transitions[oldState][j]];
                newOutputs[i][j] = outputs[oldState][j];
            }
        }

        System.out.println(n);
        System.out.println(m);
        System.out.println(0);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (j > 0) System.out.print(" ");
                System.out.print(newTransitions[i][j]);
            }
            System.out.println();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (j > 0) System.out.print(" ");
                System.out.print(newOutputs[i][j]);
            }
            System.out.println();
        }
    }
}
