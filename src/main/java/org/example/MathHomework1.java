package org.example;

import java.util.*;

public class MathHomework1 {
    public static int minNum(int threshold, int[] points) {
        int n = points.length;

        if (n == 1) return 1; // Only one problem, must solve it.

        int minProblems = n; // Worst case, solving all problems.

        // Iterate and try different start points
        for (int start = 0; start < n; start++) {
            int minValue = points[start];  // First problem must be solved
            int maxValue = points[start];
            int problemsSolved = 1;  // Count the first problem

            for (int i = start + 1; i < n; ) {
                // Solve this problem
                maxValue = points[i];
                problemsSolved++;

                // Check if we met the threshold
                if (maxValue - minValue >= threshold) {
                    minProblems = Math.min(minProblems, problemsSolved);
                    break;
                }

                // Move to either i+1 or i+2 (greedy choice)
                if (i + 2 < n) {
                    i += 2;
                } else {
                    i++;
                }
            }
        }

        return minProblems;
    }


    public static void main(String[] args) {
        // Example test cases
        //int[] points1 = {1, 2, 3, 5, 8};
        //System.out.println(minNum(4, points1)); // Output: 3

        //int[] points2 = {1, 2, 3, 5, 8};
        //System.out.println(minNum(7, points2)); // Output: 3

        //int[] points3 = {1, 2, 3};
        //System.out.println(minNum(2, points3)); // Output: 2

        //int[] points4 = {1, 2, 3, 4, 5};
        //System.out.println(minNum(4, points4)); // Output: 3

        int[] points5 = {3, 1, 2, 3};
        System.out.println(minNum(2, points5)); // Output: 2
    }
}