import java.util.*;

public class Mealy2Moore {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int kIn = scanner.nextInt();
        String[] inputAlphabet = new String[kIn];
        for (int i = 0; i < kIn; i++) {
            inputAlphabet[i] = scanner.next();
        }

        int kOut = scanner.nextInt();
        String[] outputAlphabet = new String[kOut];
        for (int i = 0; i < kOut; i++) {
            outputAlphabet[i] = scanner.next();
        }

        int n = scanner.nextInt();

        int[][] transitions = new int[n][kIn];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < kIn; j++) {
                transitions[i][j] = scanner.nextInt();
            }
        }

        int[][] outputs = new int[n][kIn];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < kIn; j++) {
                outputs[i][j] = scanner.nextInt();
            }
        }

        Map<String, Integer> stateMap = new LinkedHashMap<>();
        List<Integer> stateNumbers = new ArrayList<>();
        List<Integer> outputIndices = new ArrayList<>();

        int stateCounter = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < kIn; j++) {
                int nextState = transitions[i][j];
                int outputIndex = outputs[i][j];
                String stateKey = nextState + "," + outputIndex;

                if (!stateMap.containsKey(stateKey)) {
                    stateMap.put(stateKey, stateCounter);
                    stateNumbers.add(nextState);
                    outputIndices.add(outputIndex);
                    stateCounter++;
                }
            }
        }

        System.out.println("digraph {");
        System.out.println("    rankdir = LR");

        for (int i = 0; i < stateMap.size(); i++) {
            int state = stateNumbers.get(i);
            int outputIndex = outputIndices.get(i);
            String outputStr = outputAlphabet[outputIndex];
            System.out.println("    " + i + " [label = \"(" + state + "," + outputStr + ")\"]");
        }

        for (int i = 0; i < stateMap.size(); i++) {
            int currentState = stateNumbers.get(i);
            int currentOutput = outputIndices.get(i);

            for (int j = 0; j < kIn; j++) {
                int nextState = transitions[currentState][j];
                int nextOutputIndex = outputs[currentState][j];
                String nextKey = nextState + "," + nextOutputIndex;
                int targetState = stateMap.get(nextKey);

                System.out.println("    " + i + " -> " + targetState +
                        " [label = \"" + inputAlphabet[j] + "\"]");
            }
        }

        System.out.println("}");
    }
}
