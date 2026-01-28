package ui;

import dao.ReservationDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class ReservationManagementForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private ReservationDAO dao;
    private int selectedId = -1;

    public ReservationManagementForm() {
        dao = new ReservationDAO();
        setTitle("Reservation Management");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(236, 240, 241));
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Manage Reservations", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(41, 128, 185));
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(236, 240, 241));

        JButton btnCancel = new JButton("Cancel Selected");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(231, 76, 60)); // Red
        btnCancel.setForeground(Color.WHITE);

        btnCancel.addActionListener(e -> cancelReservation());
        btnPanel.add(btnCancel);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> loadData());
        btnPanel.add(btnRefresh);

        add(btnPanel, BorderLayout.SOUTH);

        // Table
        model = new DefaultTableModel(
                new String[] { "ID", "Customer", "Plate", "Vehicle", "Start", "End", "Price", "Status" }, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedId = Integer.parseInt(model.getValueAt(row, 0).toString());
                }
            }
        });

        loadData();
        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        Vector<Vector<Object>> data = dao.getAllReservations();
        for (Vector<Object> row : data) {
            model.addRow(row);
        }
    }

    private void cancelReservation() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Select a reservation to cancel.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel Reservation #" + selectedId + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.cancelReservation(selectedId)) {
                JOptionPane.showMessageDialog(this, "Reservation Cancelled.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Error cancelling reservation.");
            }
        }
    }
}
