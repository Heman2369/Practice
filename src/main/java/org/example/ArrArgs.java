package org.example;

public class ArrArgs {
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("No arguments provided.");
            return;
        }

        try {
            for (int i = 0; i < args.length; i++) {
                System.out.println("Value of input is " + i + " and argument: " + args[i]);
            }
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }
}