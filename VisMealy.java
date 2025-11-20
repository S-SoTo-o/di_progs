import java.util.Scanner;

public class VisMealy {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int numStates = in.nextInt();
        int alphabetSize = in.nextInt();
        int startState = in.nextInt();

        int[][] transitions = new int[numStates][alphabetSize];
        for (int i = 0; i < numStates; ++i) {
            for (int j = 0; j < alphabetSize; ++j) {
                transitions[i][j] = in.nextInt();
            }
        }

        String[][] outputs = new String[numStates][alphabetSize];
        for (int i = 0; i < numStates; ++i) {
            for (int j = 0; j < alphabetSize; ++j) {
                outputs[i][j] = in.next();
            }
        }

        System.out.println("digraph {");
        System.out.println("    rankdir = LR");

        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < alphabetSize; j++) {
                char inputSymbol = (char)('a' + j);
                System.out.println("    " + i + " -> " + transitions[i][j] + " [label = \"" + inputSymbol + "(" + outputs[i][j] + ")\"]");
            }
        }

        System.out.println("}");
        in.close();
    }
}
