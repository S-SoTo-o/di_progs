import java.util.*;

public class Prim {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int numBuildings = in.nextInt();
        int numRoads = in.nextInt();

        ArrayList<Road> roads = new ArrayList<>();
        for (int i = 0; i < numRoads; i++) {
            int b1 = in.nextInt();
            int b2 = in.nextInt();
            int length = in.nextInt();
            roads.add(new Road(b1, b2, length));
        }

        long totalLength =  minSpanningTree(numBuildings, roads);
        System.out.println(totalLength);
        in.close();
    }

    static class Road {
        int building1;
        int building2;
        int distance;

        Road(int b1, int b2, int dist) {
            building1 = b1;
            building2 = b2;
            distance = dist;
        }
    }


    static long minSpanningTree(int numBuildings, ArrayList<Road> roads) {
        long minDist = 0;
        boolean[] visited = new boolean[numBuildings];
        PriorityQueue<Road> pq = new PriorityQueue<>(Comparator.comparingInt(r -> r.distance));

        visited[0] = true; //Начинаем с первой бытовки

        //Добавляем все дороги из первой бытовки
        for (Road road : roads) {
            if (road.building1 == 0 || road.building2 == 0) {
                pq.add(road);
            }
        }


        while (!pq.isEmpty()) {
            Road currentRoad = pq.poll();
            int b1 = currentRoad.building1;
            int b2 = currentRoad.building2;

            if (visited[b1] &;& visited[b2]) continue; // Обе бытовки уже подключены


            minDist += currentRoad.distance;
            if (!visited[b1]) {
                visited[b1] = true;
                for(Road road : roads){
                    if(road.building1 == b1 || road.building2 == b1){
                        pq.add(road);
                    }
                }
            }
            if (!visited[b2]) {
                visited[b2] = true;
                for(Road road : roads){
                    if(road.building1 == b2 || road.building2 == b2){
                        pq.add(road);
                    }
                }
            }
        }

        return minDist;
    }
}
