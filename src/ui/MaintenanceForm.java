package ui;

import dao.MaintenanceDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class MaintenanceForm extends JFrame {

    public MaintenanceForm() {
        setTitle("Vehicle Maintenance Records");
        setSize(900, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(236, 240, 241));
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Maintenance Log", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(127, 140, 141)); 
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Vehicle ID", "Plate", "Brand", "Date", "Description", "Cost" }, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(127, 140, 141));
        table.getTableHeader().setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadMaintenance(model);
        setVisible(true);
    }

    private void loadMaintenance(DefaultTableModel model) {
        MaintenanceDAO dao = new MaintenanceDAO();
        Vector<Vector<Object>> data = dao.getAllMaintenanceRecords();
        for (Vector<Object> row : data) {
            model.addRow(row);
        }
    }
}
