import java.util.*;

public class MealyDFA {
    
    static class AutomatonState {
        int mealyState;
        int dfaState;
        String word;
        
        AutomatonState(int mealyState, int dfaState, String word) {
            this.mealyState = mealyState;
            this.dfaState = dfaState;
            this.word = word;
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int mealyStatesCount = scanner.nextInt();
        int inputSymbolsCount = scanner.nextInt();
        int initialMealyState = scanner.nextInt();
        
        int[][] mealyTransitions = new int[mealyStatesCount][inputSymbolsCount];
        for (int i = 0; i < mealyStatesCount; i++) {
            for (int j = 0; j < inputSymbolsCount; j++) {
                mealyTransitions[i][j] = scanner.nextInt();
            }
        }
          
        int[][] mealyOutputs = new int[mealyStatesCount][inputSymbolsCount];
        for (int i = 0; i < mealyStatesCount; i++) {
            for (int j = 0; j < inputSymbolsCount; j++) {
                mealyOutputs[i][j] = scanner.nextInt();
            }
        }
        
        int dfaStatesCount = scanner.nextInt();
        int outputSymbolsCount = scanner.nextInt();
        int initialDfaState = scanner.nextInt();
        
        boolean[] dfaFinalStates = new boolean[dfaStatesCount];
        int[][] dfaTransitions = new int[dfaStatesCount][outputSymbolsCount];
        
        for (int i = 0; i < dfaStatesCount; i++) {
            String finalMarker = scanner.next();
            dfaFinalStates[i] = finalMarker.equals("+");
            
            for (int j = 0; j < outputSymbolsCount; j++) {
                dfaTransitions[i][j] = scanner.nextInt();
            }
        }
        
        int[][] dfaOutputMapping = new int[dfaStatesCount][outputSymbolsCount];
        for (int i = 0; i < dfaStatesCount; i++) {
            for (int j = 0; j < outputSymbolsCount; j++) {
                dfaOutputMapping[i][j] = scanner.nextInt();
            }
        }
        
        String result = findShortestWord(mealyTransitions, mealyOutputs, dfaTransitions, 
                                       dfaFinalStates, initialMealyState, initialDfaState);
        System.out.println(result);
        
        scanner.close();
    }
    
    private static String findShortestWord(int[][] mealyTransitions, int[][] mealyOutputs,
                                         int[][] dfaTransitions, boolean[] dfaFinalStates,
                                         int startMealyState, int startDfaState) {
        Queue<AutomatonState> queue = new LinkedList<>();
        Set<String> visitedStates = new HashSet<>();
        
        queue.offer(new AutomatonState(startMealyState, startDfaState, ""));
        visitedStates.add(startMealyState + "," + startDfaState);
        
        while (!queue.isEmpty()) {
            AutomatonState current = queue.poll();

            if (dfaFinalStates[current.dfaState]) {
                return String.valueOf(current.word.length());
            }
            
            for (int inputSymbol = 0; inputSymbol < mealyTransitions[0].length; inputSymbol++) {
                int nextMealyState = mealyTransitions[current.mealyState][inputSymbol];
                int mealyOutput = mealyOutputs[current.mealyState][inputSymbol];
                int nextDfaState = dfaTransitions[current.dfaState][mealyOutput];
                
                String stateKey = nextMealyState + "," + nextDfaState;
                if (!visitedStates.contains(stateKey)) {
                    visitedStates.add(stateKey);
                    char outputChar = (char)('a' + mealyOutput);
                    String newWord = current.word + outputChar;
                    queue.offer(new AutomatonState(nextMealyState, nextDfaState, newWord));
                }
            }
        }
        
        return "none";
    }
}
