package ui;

import dao.EmployeeDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class EmployeeListForm extends JFrame {

    public EmployeeListForm() {
        setTitle("Employee List (Roles)");
        setSize(800, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(236, 240, 241));
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Employee Directory", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(52, 73, 94));
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "ID", "First Name", "Last Name", "Role", "Salary", "Branch" }, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadEmployees(model);
        setVisible(true);
    }

    private void loadEmployees(DefaultTableModel model) {
        EmployeeDAO dao = new EmployeeDAO();
        Vector<Vector<Object>> data = dao.getAllEmployees();
        for (Vector<Object> row : data) {
            model.addRow(row);
        }
    }
}
