package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {
    @Test
    void testAddition() {
        int result = 2 + 3;
        assertEquals(5, result, "2 + 3 should be 5");
    }

    public static void main(String[] args) {

    }
}
