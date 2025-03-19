package org.example;

import java.util.Locale;
import java.util.Scanner;

public class NumberFormat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double payment = scanner.nextDouble();
        scanner.close();

        // Format for US currency
        java.text.NumberFormat usFormat = java.text.NumberFormat.getCurrencyInstance(Locale.US);
        String us = usFormat.format(payment);

        // Format for Indian currency
        Locale indiaLocale = new Locale("en", "IN");
        java.text.NumberFormat indiaFormat = java.text.NumberFormat.getCurrencyInstance(indiaLocale);
        String india = indiaFormat.format(payment).replace("¤", "Rs."); // Replace currency symbol with "Rs."

        // Format for Chinese currency
        java.text.NumberFormat chinaFormat = java.text.NumberFormat.getCurrencyInstance(Locale.CHINA);
        String china = chinaFormat.format(payment);

        // Format for French currency
        java.text.NumberFormat franceFormat = java.text.NumberFormat.getCurrencyInstance(Locale.FRANCE);
        String france = franceFormat.format(payment);

        // Print the formatted values
        System.out.println("US: " + us);
        System.out.println("India: " + india);
        System.out.println("China: " + china);
        System.out.println("France: " + france);
    }
}