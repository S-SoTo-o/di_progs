import java.util.Scanner;

public class Friends {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt();
        int INF = 1000000;
        int[][] dist = new int[n][n];
        
        // Инициализация матрицы расстояний
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = INF;
                }
            }
        }
        
        // Чтение ребер
        int m = scanner.nextInt();
        for (int i = 0; i < m; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            dist[x][y] = 1;
            dist[y][x] = 1;
        }
        
        // Вычисление количества итераций
        int iterations = 0;
        if (n > 1) {
            iterations = (int) Math.ceil(Math.log(n - 1) / Math.log(2));
        }
        
        // Возведение матрицы в квадрат в (min, +)-алгебре
        for (int it = 0; it < iterations; it++) {
            int[][] newDist = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    newDist[i][j] = dist[i][j];
                }
            }
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        if (dist[i][k] < INF && dist[k][j] < INF) {
                            int newVal = dist[i][k] + dist[k][j];
                            if (newVal < newDist[i][j]) {
                                newDist[i][j] = newVal;
                            }
                        }
                    }
                }
            }
            
            dist = newDist;
        }
        
        // Обработка запросов
        int k = scanner.nextInt();
        for (int i = 0; i < k; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            if (dist[x][y] == INF) {
                System.out.println("-");
            } else {
                System.out.println(dist[x][y]);
            }
        }
        
        scanner.close();
    }
}
