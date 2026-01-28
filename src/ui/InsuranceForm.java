package ui;

import dao.InsuranceDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class InsuranceForm extends JFrame {

    public InsuranceForm() {
        setTitle("Vehicle Insurance Records");
        setSize(900, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(236, 240, 241));
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Insurance Management", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(192, 57, 43)); // Red
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "ID", "Plate", "Vehicle", "Policy Type", "Provider", "Start", "Expiry" }, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(192, 57, 43));
        table.getTableHeader().setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadInsurance(model);
        setVisible(true);
    }

    private void loadInsurance(DefaultTableModel model) {
        InsuranceDAO dao = new InsuranceDAO();
        Vector<Vector<Object>> data = dao.getAllInsuranceRecords();
        for (Vector<Object> row : data) {
            model.addRow(row);
        }
    }
}
