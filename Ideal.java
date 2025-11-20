import java.util.*;
import java.io.*;

public class Ideal {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] firstLine = reader.readLine().split(" ");
        int n = Integer.parseInt(firstLine[0]);
        int m = Integer.parseInt(firstLine[1]);

        List<List<int[]>> graph = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            String[] edge = reader.readLine().split(" ");
            int a = Integer.parseInt(edge[0]);
            int b = Integer.parseInt(edge[1]);
            int c = Integer.parseInt(edge[2]);

            graph.get(a).add(new int[]{b, c});
            graph.get(b).add(new int[]{a, c});
        }

        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[n] = 0;

        Queue<Integer> queue = new LinkedList<>();
        queue.offer(n);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int[] neighbor : graph.get(current)) {
                int nextRoom = neighbor[0];
                if (dist[nextRoom] == Integer.MAX_VALUE) {
                    dist[nextRoom] = dist[current] + 1;
                    queue.offer(nextRoom);
                }
            }
        }

        List<Integer> pathColors = new ArrayList<>();
        List<Integer> currentLevel = new ArrayList<>();
        currentLevel.add(1);

        boolean[] visited = new boolean[n + 1];
        visited[1] = true;

        for (int i = dist[1]; i > 0; i--) {
            int minColor = Integer.MAX_VALUE;
            List<Integer> nextLevel = new ArrayList<>();

            for (int room : currentLevel) {
                for (int[] neighbor : graph.get(room)) {
                    int nextRoom = neighbor[0];
                    int color = neighbor[1];

                    if (dist[nextRoom] == i - 1 && color < minColor) {
                        minColor = color;
                    }
                }
            }

            for (int room : currentLevel) {
                for (int[] neighbor : graph.get(room)) {
                    int nextRoom = neighbor[0];
                    int color = neighbor[1];

                    if (dist[nextRoom] == i - 1 && color == minColor && !visited[nextRoom]) {
                        visited[nextRoom] = true;
                        nextLevel.add(nextRoom);
                    }
                }
            }

            pathColors.add(minColor);
            currentLevel = nextLevel;
        }

        System.out.println(pathColors.size());
        for (int i = 0; i < pathColors.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(pathColors.get(i));
        }
        System.out.println();
    }
}
