package ui;

import dao.VehicleDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Vector;

public class VehicleForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private VehicleDAO vehicleDAO;
    private JTextField txtSearch;

    public VehicleForm() {
        vehicleDAO = new VehicleDAO();

        setTitle("Vehicle Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(236, 240, 241));
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Vehicle Inventory", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // Buttons & Search Panel
        JPanel topActionPanel = new JPanel(new BorderLayout());
        topActionPanel.setBackground(new Color(236, 240, 241));
        topActionPanel.setBorder(new EmptyBorder(0, 20, 10, 20));

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(236, 240, 241));
        searchPanel.add(new JLabel("Search (Plate/Brand): "));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        topActionPanel.add(searchPanel, BorderLayout.EAST);

        add(topActionPanel, BorderLayout.CENTER);

        //North wrapper for Header + Search
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(new Color(236, 240, 241));
        topContainer.add(header, BorderLayout.NORTH);
        topContainer.add(topActionPanel, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[] { "ID", "Plate", "Model", "Branch", "Status", "Price/Day" }, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom Refresh Button
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(236, 240, 241));
        JButton btnRefresh = new JButton("Refresh List");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> loadVehicles(""));
        btnPanel.add(btnRefresh);
        add(btnPanel, BorderLayout.SOUTH);

        loadVehicles("");

        // Live Search Listener
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                loadVehicles(txtSearch.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                loadVehicles(txtSearch.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                loadVehicles(txtSearch.getText());
            }
        });

        setVisible(true);
    }

    private void loadVehicles(String query) {
        model.setRowCount(0);
        Vector<Vector<Object>> data;

        if (query == null || query.trim().isEmpty()) {
            data = vehicleDAO.getAllVehicles();
        } else {
            data = vehicleDAO.searchVehicles(query);
        }

        for (Vector<Object> row : data) {
            model.addRow(row);
        }
    }
}
