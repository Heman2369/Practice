package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ConnectCities_2 {
    static class Edge implements Comparable<Edge> {
        int u, v, cost;

        Edge(int u, int v, int cost) {
            this.u = u;
            this.v = v;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    static int[] parent;

    static int find(int u) {
        while (parent[u] != u) {
            parent[u] = parent[parent[u]];
            u = parent[u];
        }
        return u;
    }

    static void union(int u, int v) {
        int rootU = find(u);
        int rootV = find(v);
        if (rootU != rootV) {
            parent[rootV] = rootU;
        }
    }

    public static int minimumCost(int[] costs, int[][] offers) {
        int n = costs.length;
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        int minCostCity = 0;
        for (int i = 1; i < n; i++) {
            if (costs[i] < costs[minCostCity]) {
                minCostCity = i;
            }
        }

        List<Edge> edges = new ArrayList<>();

        // Add edges from the minimal cost city to all others
        for (int j = 0; j < n; j++) {
            if (j != minCostCity) {
                int cost = costs[minCostCity] + costs[j];
                edges.add(new Edge(minCostCity, j, cost));
            }
        }

        // Add all offers
        for (int[] offer : offers) {
            int i = offer[0] - 1; // Assuming offers are 1-based
            int j = offer[1] - 1;
            int k = offer[2];
            edges.add(new Edge(i, j, k));
        }

        // Sort all edges by cost
        Collections.sort(edges);

        int totalCost = 0;
        int edgesUsed = 0;

        for (Edge edge : edges) {
            if (find(edge.u) != find(edge.v)) {
                union(edge.u, edge.v);
                totalCost += edge.cost;
                edgesUsed++;
                if (edgesUsed == n - 1) {
                    break;
                }
            }
        }

        return totalCost;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read number of cities and their costs
        System.out.print("Enter the number of cities: ");
        int n = scanner.nextInt();
        int[] costs = new int[n];
        System.out.println("Enter the cost for each city:");
        for (int i = 0; i < n; i++) {
            costs[i] = scanner.nextInt();
        }

        // Read number of offers
        System.out.print("Enter the number of offers: ");
        int m = scanner.nextInt();
        int[][] offers = new int[m][3];
        System.out.println("Enter the offers (each as i j k):");
        for (int i = 0; i < m; i++) {
            offers[i][0] = scanner.nextInt();
            offers[i][1] = scanner.nextInt();
            offers[i][2] = scanner.nextInt();
        }

        System.out.println("Minimum cost to connect all cities: " + minimumCost(costs, offers));
    }
}
