import java.util.*;
import java.io.*;

public class AutomataEquality {
    static class Automaton {
        int M, N, start;
        boolean[] finalStates;
        int[][] transitions;
        
        Automaton(int M, int N, int start, boolean[] finalStates, int[][] transitions) {
            this.M = M;
            this.N = N;
            this.start = start;
            this.finalStates = finalStates;
            this.transitions = transitions;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int M = Integer.parseInt(br.readLine().trim());
        
        Automaton aut1 = readAutomaton(br, M);

        Automaton aut2 = readAutomaton(br, M);
        
        findShortestDistinguishingString(aut1, aut2);
    }
    
    private static Automaton readAutomaton(BufferedReader br, int M) throws IOException {
        String[] firstLine = br.readLine().trim().split(" ");
        int N = Integer.parseInt(firstLine[0]);
        int start = Integer.parseInt(firstLine[1]);
        
        boolean[] finalStates = new boolean[N];
        int[][] transitions = new int[N][M];
        
        for (int i = 0; i < N; i++) {
            String[] line = br.readLine().trim().split(" ");
            finalStates[i] = line[0].equals("+");
            
            for (int j = 0; j < M; j++) {
                transitions[i][j] = Integer.parseInt(line[j + 1]);
            }
        }
        
        return new Automaton(M, N, start, finalStates, transitions);
    }
    
    private static void findShortestDistinguishingString(Automaton aut1, Automaton aut2) {
        Set<String> visited = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();
        
        int start1 = aut1.start;
        int start2 = aut2.start;
        String startKey = start1 + "," + start2;

        if (aut1.finalStates[start1] != aut2.finalStates[start2]) {
            System.out.println(0);
            return;
        }
        
        visited.add(startKey);
        queue.offer(new int[]{start1, start2, 0});
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int state1 = current[0];
            int state2 = current[1];
            int length = current[2];
            
            for (int symbol = 0; symbol < aut1.M; symbol++) {
                int next1 = aut1.transitions[state1][symbol];
                int next2 = aut2.transitions[state2][symbol];
                String nextKey = next1 + "," + next2;
                
                if (!visited.contains(nextKey)) {
                    visited.add(nextKey);
                    int newLength = length + 1;

                    if (aut1.finalStates[next1] != aut2.finalStates[next2]) {
                        System.out.println(newLength);
                        return;
                    }
                    
                    queue.offer(new int[]{next1, next2, newLength});
                }
            }
        }
        System.out.println("=");
    }
}
