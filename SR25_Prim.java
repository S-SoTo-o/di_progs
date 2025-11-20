import java.util.*;
import java.io.*;

public class StronglyRegular {
    static class Rule {
        char left;
        String right;
        
        Rule(char left, String right) {
            this.left = left;
            this.right = right;
        }
    }
    
    private static Set<Character> nonTerminals;
    private static Map<Character, Set<Character>> graph;
    private static List<Rule> rules;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt();
        String start = scanner.next().trim();
        
        rules = new ArrayList<>();
        nonTerminals = new HashSet<>();
        
        for (int i = 0; i < n; i++) {
            String line = scanner.next().trim();
            String[] parts = line.split("->");
            char left = parts[0].charAt(0);
            String right = parts[1];
            rules.add(new Rule(left, right));
            nonTerminals.add(left);
        }
        
        buildGraph();
        
        List<Set<Character>> components = findSCCs();
        
        if (checkStronglyRegular(components)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }
    
    private static void buildGraph() {
        graph = new HashMap<>();
        
        for (char nt : nonTerminals) {
            graph.put(nt, new HashSet<>());
        }
        
        for (Rule rule : rules) {
            String right = rule.right;
            for (char c : right.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    graph.get(rule.left).add(c);
                }
            }
        }
    }
    
    private static List<Set<Character>> findSCCs() {
        Set<Character> visited = new HashSet<>();
        Stack<Character> stack = new Stack<>();
        
        for (char nt : nonTerminals) {
            if (!visited.contains(nt)) {
                dfs1(nt, visited, stack);
            }
        }
        
        Map<Character, Set<Character>> reverseGraph = buildReverseGraph();
        visited.clear();
        List<Set<Character>> components = new ArrayList<>();
        
        while (!stack.isEmpty()) {
            char node = stack.pop();
            if (!visited.contains(node)) {
                Set<Character> component = new HashSet<>();
                dfs2(node, reverseGraph, visited, component);
                components.add(component);
            }
        }
        
        return components;
    }
    
    private static void dfs1(char node, Set<Character> visited, Stack<Character> stack) {
        visited.add(node);
        for (char neighbor : graph.getOrDefault(node, new HashSet<>())) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor, visited, stack);
            }
        }
        stack.push(node);
    }
    
    private static Map<Character, Set<Character>> buildReverseGraph() {
        Map<Character, Set<Character>> reverseGraph = new HashMap<>();
        
        for (char nt : nonTerminals) {
            reverseGraph.put(nt, new HashSet<>());
        }
        
        for (char from : graph.keySet()) {
            for (char to : graph.get(from)) {
                reverseGraph.get(to).add(from);
            }
        }
        
        return reverseGraph;
    }
    
    private static void dfs2(char node, Map<Character, Set<Character>> reverseGraph, 
                            Set<Character> visited, Set<Character> component) {
        visited.add(node);
        component.add(node);
        for (char neighbor : reverseGraph.getOrDefault(node, new HashSet<>())) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, reverseGraph, visited, component);
            }
        }
    }
    
    private static boolean checkStronglyRegular(List<Set<Character>> components) {
        for (Set<Character> comp : components) {
            boolean hasLeftRecursive = false;
            boolean hasRightRecursive = false;
            
            for (Rule rule : rules) {
                if (!comp.contains(rule.left)) {
                    continue;
                }
                
                List<Integer> recursivePositions = new ArrayList<>();
                String right = rule.right;
                
                for (int i = 0; i < right.length(); i++) {
                    char c = right.charAt(i);
                    if (Character.isUpperCase(c) && comp.contains(c)) {
                        recursivePositions.add(i);
                    }
                }
                
                if (recursivePositions.isEmpty()) {
                    continue;
                }
                
                if (recursivePositions.size() > 1) {
                    return false;
                }
                
                int pos = recursivePositions.get(0);
                if (pos == 0) {
                    if (hasRightRecursive) {
                        return false;
                    }
                    hasLeftRecursive = true;
                } else if (pos == right.length() - 1) {
                    if (hasLeftRecursive) {
                        return false;
                    }
                    hasRightRecursive = true;
                } else {
                    return false;
                }
            }
        }
        
        return true;
    }
}
