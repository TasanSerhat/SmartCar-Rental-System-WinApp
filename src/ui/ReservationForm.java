package ui;

import dao.CustomerDAO;
import dao.VehicleDAO;
import dao.AdditionalServiceDAO;
import db.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ReservationForm extends JFrame {
    private JComboBox<String> cmbCustomers;
    private JComboBox<String> cmbVehicles;
    private JTextField txtStartDate, txtEndDate;
    private JPanel servicesPanel;
    private Map<Integer, JCheckBox> serviceCheckBoxes;

    private CustomerDAO customerDAO;
    private VehicleDAO vehicleDAO;
    private AdditionalServiceDAO serviceDAO;

    public ReservationForm() {
        customerDAO = new CustomerDAO();
        vehicleDAO = new VehicleDAO();
        serviceDAO = new AdditionalServiceDAO();
        serviceCheckBoxes = new HashMap<>();

        setTitle("New Reservation (Smart Selection)");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(236, 240, 241));

        // Header
        JLabel header = new JLabel("Create Reservation", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        header.setForeground(new Color(44, 62, 80));
        add(header, BorderLayout.NORTH);

        // Main Panel (Form + Services)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Form Section
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 20));
        formPanel.setBackground(Color.WHITE);

        Vector<String> customerList = getCustomerList();
        Vector<String> vehicleList = vehicleDAO.getAvailableVehicles();

        cmbCustomers = new JComboBox<>(customerList);
        cmbVehicles = new JComboBox<>(vehicleList);
        txtStartDate = new JTextField(LocalDate.now().toString());
        txtEndDate = new JTextField(LocalDate.now().plusDays(3).toString());

        // Style inputs
        styleComponent(cmbCustomers);
        styleComponent(cmbVehicles);
        styleComponent(txtStartDate);
        styleComponent(txtEndDate);

        formPanel.add(new JLabel("Select Customer:"));
        formPanel.add(cmbCustomers);
        formPanel.add(new JLabel("Select Vehicle:"));
        formPanel.add(cmbVehicles);
        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        formPanel.add(txtStartDate);
        formPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        formPanel.add(txtEndDate);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Services Section
        JLabel lblServices = new JLabel("Additional Services:");
        lblServices.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblServices.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblServices);

        servicesPanel = new JPanel(new GridLayout(0, 2, 10, 5)); // 2 columns
        servicesPanel.setBackground(Color.WHITE);
        loadServices(); // Load checkboxes
        mainPanel.add(servicesPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Button
        JButton btnReserve = new JButton("Confirm Reservation");
        btnReserve.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnReserve.setBackground(new Color(46, 204, 113)); // Green
        btnReserve.setForeground(Color.WHITE);
        btnReserve.setFocusPainted(false);
        btnReserve.setPreferredSize(new Dimension(100, 50));
        btnReserve.addActionListener(e -> createReservation());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(236, 240, 241));
        btnPanel.add(btnReserve);
        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void styleComponent(JComponent comp) {
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comp.setBackground(Color.WHITE);
    }

    private Vector<String> getCustomerList() {
        Vector<String> list = new Vector<>();
        Vector<Vector<Object>> rawData = customerDAO.getAllCustomers();
        for (Vector<Object> row : rawData) {
            list.add(row.get(0) + ": " + row.get(1) + " " + row.get(2));
        }
        return list;
    }

    private void loadServices() {
        Map<Integer, String> services = serviceDAO.getAllServices();
        for (Map.Entry<Integer, String> entry : services.entrySet()) {
            JCheckBox cb = new JCheckBox(entry.getValue());
            cb.setBackground(Color.WHITE);
            servicesPanel.add(cb);
            serviceCheckBoxes.put(entry.getKey(), cb);
        }
    }

    private void createReservation() {
        if (cmbCustomers.getSelectedItem() == null || cmbVehicles.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select customer and vehicle.");
            return;
        }

        String custStr = (String) cmbCustomers.getSelectedItem();
        int custID = Integer.parseInt(custStr.split(":")[0]);

        String vehStr = (String) cmbVehicles.getSelectedItem();
        int vehID = Integer.parseInt(vehStr.split(":")[0]);

        String sqlProc = "CALL sp_CreateReservation(?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Call SP
                try (CallableStatement stmt = conn.prepareCall(sqlProc)) {
                    Date start = Date.valueOf(txtStartDate.getText());
                    Date end = Date.valueOf(txtEndDate.getText());
                    stmt.setInt(1, custID);
                    stmt.setInt(2, vehID);
                    stmt.setDate(3, start);
                    stmt.setDate(4, end);
                    stmt.execute();
                }

                // Get the generated ReservationID (Most recent one for this vehicle)
                int reservationID = -1;
                String sqlGetID = "SELECT MAX(reservationID) FROM Reservation WHERE vehicleID = ? AND customerID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlGetID)) {
                    pstmt.setInt(1, vehID);
                    pstmt.setInt(2, custID);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        reservationID = rs.getInt(1);
                    }
                }

                // Insert Selected Services
                if (reservationID != -1) {
                    String sqlService = "INSERT INTO Reservation_Services (reservationID, serviceID) VALUES (?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlService)) {
                        for (Map.Entry<Integer, JCheckBox> entry : serviceCheckBoxes.entrySet()) {
                            if (entry.getValue().isSelected()) {
                                pstmt.setInt(1, reservationID);
                                pstmt.setInt(2, entry.getKey());
                                pstmt.addBatch();
                            }
                        }
                        pstmt.executeBatch();
                    }
                }

                conn.commit();
                JOptionPane.showMessageDialog(this, "Reservation Success! Services added.");
                dispose();

            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date Format.");
        }
    }
}
