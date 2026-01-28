package ui;

import dao.RentalDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class RentalHistoryForm extends JFrame {

    public RentalHistoryForm() {
        setTitle("Rental History");
        setSize(900, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(236, 240, 241));
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Completed Rentals History", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(243, 156, 18)); // Orange
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Rental ID", "Res ID", "Customer", "Plate", "Days", "Total Cost" }, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(243, 156, 18));
        table.getTableHeader().setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData(model);
        setVisible(true);
    }

    private void loadData(DefaultTableModel model) {
        RentalDAO dao = new RentalDAO();
        Vector<Vector<Object>> data = dao.getAllRentals();
        for (Vector<Object> row : data) {
            model.addRow(row);
        }
    }
}
