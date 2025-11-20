import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class MapRoute {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = scanner.nextInt();
            }
        }
        scanner.close();

        long[][] dist = new long[n][n];
        for (long[] row : dist) {
            Arrays.fill(row, Long.MAX_VALUE);
        }
        dist[0][0] = grid[0][0];

        PriorityQueue<State> pq = new PriorityQueue<>();
        pq.offer(new State(0, 0, dist[0][0]));

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        while (!pq.isEmpty()) {
            State current = pq.poll();
            int x = current.x;
            int y = current.y;
            long d = current.dist;

            for (int i = 0; i < 4; ++i) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (nx >= 0 && nx < n && ny >= 0 && ny < n) {
                    long newDist = d + grid[nx][ny];
                    if (newDist < dist[nx][ny]) {
                        dist[nx][ny] = newDist;
                        pq.offer(new State(nx, ny, newDist));
                    }
                }
            }
        }

        System.out.println(dist[n - 1][n - 1]);
    }

    static class State implements Comparable<State> {
        int x, y;
        long dist;

        State(int x, int y, long dist) {
            this.x = x;
            this.y = y;
            this.dist = dist;
        }

        @Override
        public int compareTo(State other) {
            return Long.compare(this.dist, other.dist);
        }
    }
}
