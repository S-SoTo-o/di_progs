import java.util.*;
import java.io.*;

public class AutomataComposition {
    
    static class StatePair {
        int q1;
        int q2;
        
        StatePair(int q1, int q2) {
            this.q1 = q1;
            this.q2 = q2;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StatePair statePair = (StatePair) o;
            return q1 == statePair.q1 && q2 == statePair.q2;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(q1, q2);
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Read first automaton
        int n1 = scanner.nextInt();
        int mx = scanner.nextInt();
        int q01 = scanner.nextInt();
        
        int[][] trans1 = new int[n1 + 1][mx];
        char[] out1 = new char[n1 + 1];
        
        for (int i = 1; i <= n1; i++) {
            for (int j = 0; j < mx; j++) {
                trans1[i][j] = scanner.nextInt();
            }
            out1[i] = scanner.next().charAt(0);
        }
        
        // Read second automaton
        int n2 = scanner.nextInt();
        int my = scanner.nextInt();
        int q02 = scanner.nextInt();
        
        int[][] trans2 = new int[n2 + 1][my];
        char[] out2 = new char[n2 + 1];
        
        for (int i = 1; i <= n2; i++) {
            for (int j = 0; j < my; j++) {
                trans2[i][j] = scanner.nextInt();
            }
            out2[i] = scanner.next().charAt(0);
        }
        
        // Build composition automaton with canonical numbering
        Map<StatePair, Integer> stateToIndex = new HashMap<>();
        List<StatePair> indexToState = new ArrayList<>();
        indexToState.add(null); // index 0 is unused
        
        Queue<StatePair> queue = new LinkedList<>();
        StatePair start = new StatePair(q01, q02);
        
        stateToIndex.put(start, 1);
        indexToState.add(start);
        queue.add(start);
        
        while (!queue.isEmpty()) {
            StatePair current = queue.poll();
            
            for (int i = 0; i < mx; i++) {
                int q1_next = trans1[current.q1][i];
                char y = out1[current.q1];
                int yIndex = y - 'a';
                int q2_next = trans2[current.q2][yIndex];
                
                StatePair next = new StatePair(q1_next, q2_next);
                
                if (!stateToIndex.containsKey(next)) {
                    int newIndex = indexToState.size();
                    stateToIndex.put(next, newIndex);
                    indexToState.add(next);
                    queue.add(next);
                }
            }
        }
        
        int n = indexToState.size() - 1;
        
        // Build transitions for the result automaton
        int[][] resultTransitions = new int[n + 1][mx];
        char[] resultOutputs = new char[n + 1];
        
        for (int i = 1; i <= n; i++) {
            StatePair sp = indexToState.get(i);
            resultOutputs[i] = out2[sp.q2];
            
            for (int j = 0; j < mx; j++) {
                int q1_next = trans1[sp.q1][j];
                char y = out1[sp.q1];
                int yIndex = y - 'a';
                int q2_next = trans2[sp.q2][yIndex];
                
                StatePair next = new StatePair(q1_next, q2_next);
                resultTransitions[i][j] = stateToIndex.get(next);
            }
        }
        
        // Output the result automaton
        System.out.println(n + " " + mx + " 1");
        
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < mx; j++) {
                System.out.print(resultTransitions[i][j]);
                if (j < mx - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println(" " + resultOutputs[i]);
        }
    }
}
