package org.example.SwingCustomerManagement;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerManagementApp extends JFrame {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/datajpa";
    private static final String USER = "postgres";
    private static final String PASS = "Vtu@14932";

    private JList<Customer> customerList;
    private DefaultListModel<Customer> listModel;
    private JButton viewButton, addButton, modifyButton, deleteButton;

    public CustomerManagementApp() {
        initializeUI();
        loadCustomers();
    }

    private void initializeUI() {
        setTitle("Customer Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Customer list
        listModel = new DefaultListModel<>();
        customerList = new JList<>(listModel);
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(customerList);
        listScrollPane.setPreferredSize(new Dimension(300, 400));

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        viewButton = new JButton("View Address");
        addButton = new JButton("Add Customer");
        modifyButton = new JButton("Modify Customer");
        deleteButton = new JButton("Delete Customer");

        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        // Layout
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Customers:"), BorderLayout.NORTH);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.EAST);

        add(mainPanel);

        // Event listeners
        viewButton.addActionListener(e -> viewCustomerAddress());
        addButton.addActionListener(e -> addCustomer());
        modifyButton.addActionListener(e -> modifyCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());

        // Enable/disable buttons based on selection
        customerList.addListSelectionListener(e -> {
            boolean hasSelection = customerList.getSelectedValue() != null;
            viewButton.setEnabled(hasSelection);
            modifyButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });
    }

    private void loadCustomers() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT * FROM customers ORDER BY short_name";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            listModel.clear();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("short_name"),
                        rs.getString("full_name")
                );
                listModel.addElement(customer);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading customers: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewCustomerAddress() {
        Customer selected = customerList.getSelectedValue();
        if (selected != null) {
            new AddressDialog(this, selected).setVisible(true);
        }
    }

    private void addCustomer() {
        JTextField shortNameField = new JTextField(20);
        JTextField fullNameField = new JTextField(30);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Short Name:"));
        panel.add(shortNameField);
        panel.add(new JLabel("Full Name:"));
        panel.add(fullNameField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Customer", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String shortName = shortNameField.getText().trim();
            String fullName = fullNameField.getText().trim();

            if (!shortName.isEmpty() && !fullName.isEmpty()) {
                addCustomerToDatabase(shortName, fullName);
                loadCustomers(); // Refresh list
            }
        }
    }

    private void addCustomerToDatabase(String shortName, String fullName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "INSERT INTO customers (short_name, full_name) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, shortName);
            stmt.setString(2, fullName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding customer: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifyCustomer() {
        Customer selected = customerList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer to modify");
            return;
        }

        JTextField shortNameField = new JTextField(selected.getShortName(), 20);
        JTextField fullNameField = new JTextField(selected.getFullName(), 30);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Short Name:"));
        panel.add(shortNameField);
        panel.add(new JLabel("Full Name:"));
        panel.add(fullNameField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Modify Customer", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String shortName = shortNameField.getText().trim();
            String fullName = fullNameField.getText().trim();

            if (!shortName.isEmpty() && !fullName.isEmpty()) {
                modifyCustomerInDatabase(selected.getCustomerId(), shortName, fullName);
                loadCustomers(); // Refresh list
            }
        }
    }

    private void modifyCustomerInDatabase(int customerId, String shortName, String fullName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "UPDATE customers SET short_name = ?, full_name = ? WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, shortName);
            stmt.setString(2, fullName);
            stmt.setInt(3, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error modifying customer: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        Customer selected = customerList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete customer: " + selected.getShortName() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            deleteCustomerFromDatabase(selected.getCustomerId());
            loadCustomers(); // Refresh list
        }
    }

    private void deleteCustomerFromDatabase(int customerId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // First delete addresses (due to foreign key constraint)
            String deleteAddresses = "DELETE FROM addresses WHERE customer_id = ?";
            PreparedStatement stmt1 = conn.prepareStatement(deleteAddresses);
            stmt1.setInt(1, customerId);
            stmt1.executeUpdate();

            // Then delete customer
            String deleteCustomer = "DELETE FROM customers WHERE customer_id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(deleteCustomer);
            stmt2.setInt(1, customerId);
            stmt2.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting customer: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new CustomerManagementApp().setVisible(true);
        });
    }
}