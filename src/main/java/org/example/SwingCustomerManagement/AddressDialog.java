package org.example.SwingCustomerManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddressDialog extends JDialog {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/datajpa";
    private static final String USER = "postgres";
    private static final String PASS = "Vtu@14932";

    private Customer customer;
    private JTextField[] addressLines = new JTextField[3];
    private JTextField cityField, postalCodeField, countryField;
    private JButton saveButton, cancelButton;
    private JComboBox<String> addressTypeCombo;

    public AddressDialog(JFrame parent, Customer customer) {
        super(parent, "Manage Addresses for " + customer.getShortName(), true);
        this.customer = customer;
        initializeUI();
        loadAddresses();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Address type selection
        mainPanel.add(new JLabel("Address Type:"));
        addressTypeCombo = new JComboBox<>(new String[]{"Home", "Work", "Other"});
        mainPanel.add(addressTypeCombo);

        // Address lines
        for (int i = 0; i < 3; i++) {
            mainPanel.add(new JLabel("Address Line " + (i + 1) + ":"));
            addressLines[i] = new JTextField(80);
            mainPanel.add(addressLines[i]);
        }

        // City and Postal Code
        mainPanel.add(new JLabel("City:"));
        cityField = new JTextField(50);
        mainPanel.add(cityField);

        mainPanel.add(new JLabel("Country:"));
        countryField = new JTextField(30);
        mainPanel.add(countryField);

        mainPanel.add(new JLabel("Postal Code:"));
        postalCodeField = new JTextField(20);
        mainPanel.add(postalCodeField);

        // Validation for postal code
        postalCodeField.setInputVerifier(new InputVerifier() {
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                String country = countryField.getText().trim();
                String postalCode = field.getText().trim();

                if (country.isEmpty()) {
                    return PostalCodeValidator.isValidPostalCode(postalCode);
                } else {
                    return PostalCodeValidator.isValidPostalCode(postalCode, country);
                }
            }
        });

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners
        saveButton.addActionListener(e -> saveAddress());
        cancelButton.addActionListener(e -> dispose());
    }

    private void loadAddresses() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT * FROM addresses WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customer.getCustomerId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                addressLines[0].setText(rs.getString("address_line1"));
                addressLines[1].setText(rs.getString("address_line2"));
                addressLines[2].setText(rs.getString("address_line3"));
                cityField.setText(rs.getString("city"));
                postalCodeField.setText(rs.getString("postal_code"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading address: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAddress() {
        // Validate postal code
        String country = countryField.getText().trim();
        String postalCode = postalCodeField.getText().trim();

        boolean isValid;
        if (country.isEmpty()) {
            isValid = PostalCodeValidator.isValidPostalCode(postalCode);
        } else {
            isValid = PostalCodeValidator.isValidPostalCode(postalCode, country);
        }

        if (!isValid) {
            JOptionPane.showMessageDialog(this,
                    "Invalid postal code format for the specified country",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save to database
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // For PostgreSQL, use different syntax for upsert
            String query = "INSERT INTO addresses (customer_id, address_line1, address_line2, address_line3, city, postal_code) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (customer_id) DO UPDATE SET " +
                    "address_line1 = EXCLUDED.address_line1, " +
                    "address_line2 = EXCLUDED.address_line2, " +
                    "address_line3 = EXCLUDED.address_line3, " +
                    "city = EXCLUDED.city, " +
                    "postal_code = EXCLUDED.postal_code";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customer.getCustomerId());
            stmt.setString(2, addressLines[0].getText());
            stmt.setString(3, addressLines[1].getText());
            stmt.setString(4, addressLines[2].getText());
            stmt.setString(5, cityField.getText());
            stmt.setString(6, postalCodeField.getText());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Address saved successfully!");
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving address: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}