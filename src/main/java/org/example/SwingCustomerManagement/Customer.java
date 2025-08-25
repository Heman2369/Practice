package org.example.SwingCustomerManagement;

public class Customer {
    private int customerId;
    private String shortName;
    private String fullName;

    public Customer(int customerId, String shortName, String fullName) {
        this.customerId = customerId;
        this.shortName = shortName;
        this.fullName = fullName;
    }

    // Getters and setters
    public int getCustomerId() { return customerId; }
    public String getShortName() { return shortName; }
    public String getFullName() { return fullName; }

    @Override
    public String toString() {
        return shortName + " (" + fullName + ")";
    }
}