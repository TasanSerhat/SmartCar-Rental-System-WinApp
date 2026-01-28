package ui;

import dao.CustomerDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class CustomerForm extends JFrame {
    private JTextField txtFirstName, txtLastName, txtLicense, txtPhone, txtEmail, txtAddress, txtSearch;
    private JTable table;
    private DefaultTableModel model;
    private int selectedCustomerId = -1;
    private CustomerDAO customerDAO;

    private final Color BG_COLOR = new Color(236, 240, 241);

    public CustomerForm() {
        customerDAO = new CustomerDAO();

        setTitle("Customer Management");
        setSize(900, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_COLOR);

        JLabel lblHeader = new JLabel("Customer Operations", JLabel.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(new Color(44, 62, 80));
        lblHeader.setBorder(new EmptyBorder(20, 0, 20, 0));
        headerPanel.add(lblHeader, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // --- Form Panel (GridBagLayout) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 50, 10, 50),
                BorderFactory.createTitledBorder("Customer Details")));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtFirstName = createStyledTextField();
        txtLastName = createStyledTextField();
        txtLicense = createStyledTextField();
        txtPhone = createStyledTextField();
        txtEmail = createStyledTextField();
        txtAddress = createStyledTextField();

        // Row 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtFirstName, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        formPanel.add(txtLastName, gbc);

        // Row 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("License No:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtLicense, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        formPanel.add(txtPhone, gbc);

        // Row 3
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        formPanel.add(txtAddress, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- Bottom Panel ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(BG_COLOR);

        JButton btnAdd = createStyledButton("Add", new Color(46, 204, 113));
        JButton btnUpdate = createStyledButton("Update", new Color(241, 196, 15));
        JButton btnDelete = createStyledButton("Delete", new Color(231, 76, 60));
        JButton btnClear = createStyledButton("Clear", new Color(149, 165, 166));

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        bottomPanel.add(btnPanel, BorderLayout.NORTH);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(BG_COLOR);
        searchPanel.add(new JLabel("Search (Name/License): "));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        bottomPanel.add(searchPanel, BorderLayout.CENTER);

        // Table
        model = new DefaultTableModel(
                new String[] { "ID", "First Name", "Last Name", "License", "Phone", "Email", "Address" }, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 250));
        bottomPanel.add(scrollPane, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        loadCustomers("");

        // Listeners
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedCustomerId = Integer.parseInt(model.getValueAt(row, 0).toString());
                    txtFirstName.setText(model.getValueAt(row, 1).toString());
                    txtLastName.setText(model.getValueAt(row, 2).toString());
                    txtLicense.setText(model.getValueAt(row, 3).toString());
                    txtPhone.setText(model.getValueAt(row, 4).toString());
                    txtEmail.setText(model.getValueAt(row, 5).toString());
                    txtAddress.setText(model.getValueAt(row, 6).toString());
                }
            }
        });

        // Live Search Listener
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                loadCustomers(txtSearch.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                loadCustomers(txtSearch.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                loadCustomers(txtSearch.getText());
            }
        });

        btnAdd.addActionListener(e -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnClear.addActionListener(e -> clearFields());

        setVisible(true);
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return tf;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(100, 40));
        return btn;
    }

    private void loadCustomers(String query) {
        model.setRowCount(0);
        Vector<Vector<Object>> data;
        if (query == null || query.trim().isEmpty()) {
            data = customerDAO.getAllCustomers();
        } else {
            data = customerDAO.searchCustomers(query);
        }

        for (Vector<Object> row : data) {
            model.addRow(row);
        }
    }

    private void addCustomer() {
        if (customerDAO.addCustomer(txtFirstName.getText(), txtLastName.getText(), txtLicense.getText(),
                txtPhone.getText(), txtEmail.getText(), txtAddress.getText())) {
            loadCustomers("");
            clearFields();
            JOptionPane.showMessageDialog(this, "Customer added successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Error adding customer.");
        }
    }

    private void updateCustomer() {
        if (selectedCustomerId == -1)
            return;
        if (customerDAO.updateCustomer(selectedCustomerId, txtFirstName.getText(), txtLastName.getText(),
                txtLicense.getText(), txtPhone.getText(), txtEmail.getText(), txtAddress.getText())) {
            loadCustomers("");
            clearFields();
            JOptionPane.showMessageDialog(this, "Customer updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Error updating customer.");
        }
    }

    private void deleteCustomer() {
        if (selectedCustomerId == -1)
            return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this customer?");
        if (confirm == JOptionPane.YES_OPTION) {
            if (customerDAO.deleteCustomer(selectedCustomerId)) {
                loadCustomers("");
                clearFields();
                JOptionPane.showMessageDialog(this, "Customer deleted.");
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting customer.");
            }
        }
    }

    private void clearFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtLicense.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtSearch.setText("");
        selectedCustomerId = -1;
        table.clearSelection();
    }
}
