package ui;

import db.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class PaymentForm extends JFrame {

    public PaymentForm() {
        setTitle("Payment History");
        setSize(800, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(236, 240, 241));
        setLayout(new BorderLayout());

        JLabel header = new JLabel("All Payment Records", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(39, 174, 96)); // Green
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[] { "ID", "Rental ID", "Amount", "Date", "Method" },
                0);
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(39, 174, 96));
        table.getTableHeader().setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadPayments(model);
        setVisible(true);
    }

    private void loadPayments(DefaultTableModel model) {
        String sql = "SELECT * FROM Payment ORDER BY paymentDate DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("paymentID"));
                row.add(rs.getInt("rentalID"));
                row.add(rs.getBigDecimal("amount"));
                row.add(rs.getDate("paymentDate"));
                row.add(rs.getString("paymentMethod"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading payments: " + e.getMessage());
        }
    }
}
