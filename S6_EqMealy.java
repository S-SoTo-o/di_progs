import java.util.*;

public class EqMealy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Automaton aut1 = readAutomaton(scanner);
        Automaton aut2 = readAutomaton(scanner);

        if (areEquivalent(aut1, aut2)) {
            System.out.println("EQUAL");
        } else {
            System.out.println("NOT EQUAL");
        }
    }

    static class Automaton {
        int n;
        int m;
        int q0;
        int[][] transitions;
        String[][] outputs;

        Automaton(int n, int m, int q0, int[][] transitions, String[][] outputs) {
            this.n = n;
            this.m = m;
            this.q0 = q0;
            this.transitions = transitions;
            this.outputs = outputs;
        }
    }

    static Automaton readAutomaton(Scanner scanner) {
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int q0 = scanner.nextInt();

        int[][] transitions = new int[n][m];
        String[][] outputs = new String[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                transitions[i][j] = scanner.nextInt();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                outputs[i][j] = scanner.next();
            }
        }

        return new Automaton(n, m, q0, transitions, outputs);
    }

    static boolean areEquivalent(Automaton aut1, Automaton aut2) {
        if (aut1.m != aut2.m) {
            return false;
        }

        Queue<StatePair> queue = new LinkedList<>();
        boolean[][] visited = new boolean[aut1.n][aut2.n];

        queue.add(new StatePair(aut1.q0, aut2.q0));
        visited[aut1.q0][aut2.q0] = true;

        while (!queue.isEmpty()) {
            StatePair current = queue.poll();
            int state1 = current.state1;
            int state2 = current.state2;

            for (int input = 0; input < aut1.m; input++) {
                int next1 = aut1.transitions[state1][input];
                int next2 = aut2.transitions[state2][input];

                String output1 = aut1.outputs[state1][input];
                String output2 = aut2.outputs[state2][input];

                if (!output1.equals(output2)) {
                    return false;
                }

                if (!visited[next1][next2]) {
                    visited[next1][next2] = true;
                    queue.add(new StatePair(next1, next2));
                }
            }
        }

        return true;
    }

    static class StatePair {
        int state1;
        int state2;

        StatePair(int state1, int state2) {
            this.state1 = state1;
            this.state2 = state2;
        }
    }
}
