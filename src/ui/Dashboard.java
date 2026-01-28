package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        this("Manager");
    }

    public Dashboard(String role) {
        setTitle("SmartCar Rental System - Dashboard [" + role + "]");
        setSize(1300, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(236, 240, 241));

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel headerLabel = new JLabel("SmartCar Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        JLabel userLabel = new JLabel("User: " + role);
        userLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        userLabel.setForeground(new Color(189, 195, 199));
        headerPanel.add(userLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Buttons Grid (3x4 or Custom Layout) ---
        // flexible GridLayout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 3, 20, 20));
        buttonPanel.setBorder(new EmptyBorder(30, 60, 30, 60));
        buttonPanel.setBackground(new Color(236, 240, 241));

        // Define Buttons
        JButton btnReservation = createStyledButton("New Reservation", new Color(46, 204, 113));
        JButton btnResManage = createStyledButton("Manage Reservations", new Color(39, 174, 96)); // Darker Green
        JButton btnCustomers = createStyledButton("Manage Customers", new Color(52, 152, 219));

        JButton btnRentals = createStyledButton("Rental Operations", new Color(243, 156, 18));
        JButton btnRentalHistory = createStyledButton("Rental History", new Color(211, 84, 0)); // Darker Orange
        JButton btnVehicles = createStyledButton("Vehicle Inventory", new Color(155, 89, 182));

        JButton btnInsurance = createStyledButton("Insurance Info", new Color(231, 76, 60));
        JButton btnMaintenance = createStyledButton("Maintenance Log", new Color(127, 140, 141));
        JButton btnEmployees = createStyledButton("Staff Directory", new Color(22, 160, 133));

        JButton btnPayments = createStyledButton("Payment History", new Color(52, 73, 94));
        JButton btnReports = createStyledButton("Reports & Analytics", new Color(149, 165, 166));
        JButton btnExit = createStyledButton("Exit / Logout", new Color(44, 62, 80));

        // Apply Role Restrictions
        applyRoleRestrictions(role, btnReservation, btnResManage, btnCustomers, btnRentals, btnRentalHistory,
                btnVehicles, btnInsurance, btnMaintenance, btnEmployees, btnPayments, btnReports);

        // Action Listeners
        if (btnReservation.isEnabled())
            btnReservation.addActionListener(e -> new ReservationForm());
        if (btnResManage.isEnabled())
            btnResManage.addActionListener(e -> new ReservationManagementForm());
        if (btnCustomers.isEnabled())
            btnCustomers.addActionListener(e -> new CustomerForm());

        if (btnRentals.isEnabled())
            btnRentals.addActionListener(e -> new MasterDetailForm());
        if (btnRentalHistory.isEnabled())
            btnRentalHistory.addActionListener(e -> new RentalHistoryForm());
        if (btnVehicles.isEnabled())
            btnVehicles.addActionListener(e -> new VehicleForm());

        if (btnInsurance.isEnabled())
            btnInsurance.addActionListener(e -> new InsuranceForm());
        if (btnMaintenance.isEnabled())
            btnMaintenance.addActionListener(e -> new MaintenanceForm());
        if (btnEmployees.isEnabled())
            btnEmployees.addActionListener(e -> new EmployeeListForm());

        if (btnPayments.isEnabled())
            btnPayments.addActionListener(e -> new PaymentForm());
        if (btnReports.isEnabled())
            btnReports.addActionListener(e -> new ReportForm());
        btnExit.addActionListener(e -> {
            dispose();
            new RoleSelectionForm(); // Back to login
        });

        // Add
        buttonPanel.add(btnReservation);
        buttonPanel.add(btnResManage);
        buttonPanel.add(btnCustomers);

        buttonPanel.add(btnRentals);
        buttonPanel.add(btnRentalHistory);
        buttonPanel.add(btnVehicles);

        buttonPanel.add(btnInsurance);
        buttonPanel.add(btnMaintenance);
        buttonPanel.add(btnEmployees);

        buttonPanel.add(btnPayments);
        buttonPanel.add(btnReports);
        buttonPanel.add(btnExit);

        add(buttonPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel footerLabel = new JLabel("Database Management Project | Spring 2026", JLabel.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void applyRoleRestrictions(String role, JButton... buttons) {

        boolean isSales = role.equals("Salesperson");
        boolean isTech = role.equals("Technician");

        if (isSales) {
            disableButton(buttons[6]); // Insurance
            disableButton(buttons[7]); // Maintenance
            disableButton(buttons[8]); // Employees
            disableButton(buttons[10]); // Reports
        } else if (isTech) {
            disableButton(buttons[0]); // Res
            disableButton(buttons[1]); // ResManage
            disableButton(buttons[2]); // Cust
            disableButton(buttons[3]); // Rental
            disableButton(buttons[4]); // RentalHist
            disableButton(buttons[8]); // Employees 
            disableButton(buttons[9]); // Payment
            disableButton(buttons[10]); // Reports
        }
    }

    private void disableButton(JButton btn) {
        btn.setEnabled(false);
        btn.setBackground(new Color(236, 240, 241));
        btn.setForeground(Color.LIGHT_GRAY);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

    private JButton createStyledButton(String text, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(44, 62, 80));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                new EmptyBorder(10, 20, 10, 20)));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(hoverColor);
                    btn.setForeground(Color.WHITE);
                    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(new Color(44, 62, 80));
                    btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        return btn;
    }
}
