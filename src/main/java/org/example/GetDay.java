package org.example;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class GetDay {
    public static String findDay(int month, int day, int year) {
        // Create a LocalDate instance for the given date
        LocalDate date = LocalDate.of(year, month, day);

        // Get the day of the week
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // Format the day of the week in uppercase
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US).toUpperCase();
    }

    public static void main(String[] args) {
                System.out.println(findDay(9, 23, 2000));
    }
}