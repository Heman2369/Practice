package org.example.SwingCustomerManagement;

public class PostalCodeValidator {
    public static boolean isValidPostalCode(String postalCode, String countryCode) {
        if (postalCode == null || postalCode.trim().isEmpty()) {
            return false;
        }

        // Basic pattern matching - extend for specific countries
        switch (countryCode.toUpperCase()) {
            case "US":
                return postalCode.matches("\\d{5}(-\\d{4})?");
            case "UK":
                return postalCode.matches("[A-Z]{1,2}\\d[A-Z\\d]? \\d[A-Z]{2}");
            case "MY": // Malaysia postal code
                return postalCode.matches("\\d{5}");
            case "SG": // Singapore postal code
                return postalCode.matches("\\d{6}");
            default:
                return postalCode.matches("[A-Z0-9\\-\\s]+");
        }
    }
    public static boolean isValidPostalCode(String postalCode) {
        return postalCode != null && postalCode.matches("[A-Z0-9\\-\\s]+");
    }
}