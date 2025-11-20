import java.util.*;

public class Mars {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();

        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            String line = in.nextLine();
            for (int j = 0; j < n; j++) {
                matrix[i][j] = line.charAt(j * 2);
            }
        }

        List<List<Integer>> allSolutions = new ArrayList<>();

        for (int mask = 0; mask < (1 << n); mask++) {
            List<Integer> group1 = new ArrayList<>();
            List<Integer> group2 = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    group1.add(i + 1);
                } else {
                    group2.add(i + 1);
                }
            }

            if (isValidGroup(group1, matrix) && isValidGroup(group2, matrix)) {
                if (group1.size() <= group2.size()) {
                    allSolutions.add(group1);
                } else {
                    allSolutions.add(group2);
                }
            }
        }

        if (allSolutions.isEmpty()) {
            System.out.println("No solution");
        } else {
            List<Integer> bestSolution = allSolutions.get(0);
            int minDiff = Math.abs(bestSolution.size() - (n - bestSolution.size()));

            for (List<Integer> solution : allSolutions) {
                int currentDiff = Math.abs(solution.size() - (n - solution.size()));

                if (currentDiff < minDiff) {
                    minDiff = currentDiff;
                    bestSolution = solution;
                } else if (currentDiff == minDiff) {
                    if (isLexicographicallySmaller(solution, bestSolution)) {
                        bestSolution = solution;
                    }
                }
            }

            Collections.sort(bestSolution);
            for (int i = 0; i < bestSolution.size(); i++) {
                System.out.print(bestSolution.get(i));
                if (i < bestSolution.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    static boolean isValidGroup(List<Integer> group, char[][] matrix) {
        for (int i = 0; i < group.size(); i++) {
            for (int j = i + 1; j < group.size(); j++) {
                int candidate1 = group.get(i) - 1;
                int candidate2 = group.get(j) - 1;
                if (matrix[candidate1][candidate2] == '+') {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean isLexicographicallySmaller(List<Integer> a, List<Integer> b) {
        int minSize = Math.min(a.size(), b.size());
        for (int i = 0; i < minSize; i++) {
            if (a.get(i) < b.get(i)) {
                return true;
            } else if (a.get(i) > b.get(i)) {
                return false;
            }
        }
        return a.size() < b.size();
    }
}
