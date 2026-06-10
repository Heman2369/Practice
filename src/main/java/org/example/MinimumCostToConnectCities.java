package org.example;

import java.util.*;

public class MinimumCostToConnectCities {

    public static int minimumCost(List<Integer> cost, List<List<Integer>> offer) {
        int n = cost.size();
        boolean[] visited = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        // Start with the first city
        pq.offer(new int[]{cost.get(0), 0}); // {cost, city index}
        int totalCost = 0;

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int city = current[1];

            // If the city is already visited, skip it
            if (visited[city]) continue;

            // Mark the city as visited and add its cost
            visited[city] = true;
            totalCost += current[0];

            // Add the costs of connecting to other cities
            for (List<Integer> offerList : offer) {
                if (offerList.get(0) == city) {
                    int targetCity = offerList.get(1);
                    int offerCost = offerList.get(2);

                    if (!visited[targetCity]) {
                        // Only add offer cost if we haven't visited the target city
                        pq.offer(new int[]{offerCost, targetCity});
                    }
                }
            }
        }

        // Return the total cost to connect all cities
        return totalCost;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read number of cities and their costs
        System.out.print("Enter the number of cities: ");
        int n = scanner.nextInt();
        List<Integer> costs = new ArrayList<>();
        System.out.println("Enter the cost for each city:");
        for (int i = 0; i < n; i++) {
            costs.add(scanner.nextInt());
        }

        // Read number of offers
        System.out.print("Enter the number of offers: ");
        int m = scanner.nextInt();
        List<List<Integer>> offers = new ArrayList<>();
        System.out.println("Enter the offers (each as fromCity toCity cost):");
        for (int i = 0; i < m; i++) {
            int fromCity = scanner.nextInt();
            int toCity = scanner.nextInt();
            int cost = scanner.nextInt();
            offers.add(Arrays.asList(fromCity, toCity, cost));
        }

        int result = minimumCost(costs, offers);
        System.out.println("Minimum cost to connect all cities: " + result);
    }
}