package ui;

import db.DatabaseConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

public class ReportForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ReportForm() {
        setTitle("Rental Reports");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(236, 240, 241));
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(236, 240, 241));

        JLabel header = new JLabel("Detailed Rental Report (View)", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(142, 68, 173)); // Purple
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        headerPanel.add(header, BorderLayout.CENTER);

        JButton btnExport = new JButton("Export to CSV");
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExport.setBackground(new Color(39, 174, 96));
        btnExport.setForeground(Color.WHITE);
        btnExport.addActionListener(e -> exportToCSV());

        JPanel btnWrapper = new JPanel();
        btnWrapper.setBackground(new Color(236, 240, 241));
        btnWrapper.add(btnExport);
        headerPanel.add(btnWrapper, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(
                new String[] { "ID", "Customer", "Vehicle", "Plate", "Start", "End", "Cost", "Days", "Paid", "Method" },
                0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(142, 68, 173));
        table.getTableHeader().setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadReport();
        setVisible(true);
    }

    private void loadReport() {
        // Query the View: vw_RentalDetails
        String sql = "SELECT * FROM vw_RentalDetails";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("rentalID"));
                row.add(rs.getString("CustomerName"));
                row.add(rs.getString("VehicleModel"));
                row.add(rs.getString("plateNo"));
                row.add(rs.getDate("startDate"));
                row.add(rs.getDate("endDate"));
                row.add(rs.getBigDecimal("totalCost"));
                row.add(rs.getInt("totalDays"));
                row.add(rs.getBigDecimal("PaidAmount"));
                row.add(rs.getString("paymentMethod"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading report: " + e.getMessage());
        }
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Ensure .csv extension
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            try (FileWriter fw = new FileWriter(fileToSave)) {
                // Write Headers
                for (int i = 0; i < model.getColumnCount(); i++) {
                    fw.write(model.getColumnName(i) + (i == model.getColumnCount() - 1 ? "" : ","));
                }
                fw.write("\n");

                // Write Data
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object val = model.getValueAt(i, j);
                        String str = (val == null) ? "" : val.toString();
                        // Escape commas logic if needed, simple approach for now
                        fw.write(str + (j == model.getColumnCount() - 1 ? "" : ","));
                    }
                    fw.write("\n");
                }

                JOptionPane.showMessageDialog(this, "Report exported successfully!");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting file: " + ex.getMessage());
            }
        }
    }
}
