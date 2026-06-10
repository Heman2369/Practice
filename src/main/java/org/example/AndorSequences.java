package org.example;

import java.util.*;

public class AndorSequences {
    private static final int MOD = 1000000007;

    public static class Result {
        int count;
        List<Long> maxSequence;

        public Result(int count, List<Long> maxSequence) {
            this.count = count;
            this.maxSequence = maxSequence;
        }
    }

    public static Result findSequences(long x, long y) {
        if (x == y) {
            List<Long> sequence = new ArrayList<>();
            sequence.add(x);
            return new Result(1, sequence);
        }

        int minMsb = Long.SIZE - Long.numberOfLeadingZeros(x) - 1;
        int maxMsb = Long.SIZE - Long.numberOfLeadingZeros(y) - 1;
        int maxLen = maxMsb - minMsb + 1;

        long count = 1;
        List<Long> maxSequence = new ArrayList<>();

        for (int s = minMsb; s <= maxMsb; s++) {
            long lower = 1L << s;
            long upper = (1L << (s + 1)) - 1;
            long currentLower = Math.max(lower, x);
            long currentUpper = Math.min(upper, y);
            if (currentLower > currentUpper) {
                continue;
            }
            count = (count * (currentUpper - currentLower + 1)) % MOD;
            maxSequence.add(currentUpper);
        }

        if (maxLen == 1) {
            count = (y - x + 1) % MOD;
            maxSequence.clear();
            maxSequence.add(y);
        }

        return new Result((int) count, maxSequence);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long x = scanner.nextLong();
        long y = scanner.nextLong();
        Result result = findSequences(x, y);
        System.out.println(result.count);
        for (long num : result.maxSequence) {
            System.out.print(num + " ");
        }
    }
}
