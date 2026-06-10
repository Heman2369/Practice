package org.example;

import java.util.Arrays;
import java.util.List;
public class MathHomeWorkString {

    public static void main(String arg[]){
        //TEST CASE 1
        int threshold=650;
        List<Integer> points = Arrays.asList(82,112,134,178,206,229,238,278,293,335);
        System.out.println(minNum(threshold,points)); //EXPECTED 10
        //TEST CASE 2
        threshold=4;
        points = Arrays.asList(1,2,3,5,8);
        System.out.println(minNum(threshold,points));  //EXPECTED 3
        //TEST CASE 3
        threshold=4;
        points = Arrays.asList(1,2,3,4,5);
        System.out.println(minNum(threshold,points)); //EXPECTED 3
        //TEST CASE 4
        threshold=2;
        points = Arrays.asList(1,2,3);
        System.out.println(minNum(threshold,points)); //EXPECTED 2
        //TEST CASE 5
        threshold=4;
        points = Arrays.asList(1,3,4,7);
        System.out.println(minNum(threshold,points)); //EXPECTED 3
        //TEST CASE 6
        threshold=402;
        points = Arrays.asList(162,206,224,264,288,334,364,367,389,405,454,478,479,482,509,517,545,578,626,657,692,705,720,734,747);
        System.out.println(minNum(threshold,points)); //EXPECTED 2

    }

    public static int minNum(int threshold, List<Integer> points) {
        int n = points.size();
        if (n == 0) return 0;

        int minVal = points.get(0);
        int j = -1;

        for (int i = 0; i < n; i++) {
            if (points.get(i) - minVal >= threshold) {
                j = i;
                break;
            }
        }

        if (j == -1) {
            return n;  // Need all problems
        }

        // The key formula: minimum problems needed is ceil(j/2) + 1
        return (j + 1) / 2 + 1;
    }
}