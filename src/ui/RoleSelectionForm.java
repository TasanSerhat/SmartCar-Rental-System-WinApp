package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoleSelectionForm extends JFrame {

    public RoleSelectionForm() {
        setTitle("Select User Role");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(44, 62, 80));
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("Login As...", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(30, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        btnPanel.setBackground(new Color(44, 62, 80));
        btnPanel.setBorder(new EmptyBorder(20, 80, 40, 80));

        JButton btnManager = createRoleButton("Manager", "Full Access");
        JButton btnSales = createRoleButton("Salesperson", "Rental & Customers");
        JButton btnTech = createRoleButton("Technician", "Maintenance & Vehicles");

        // Listeners
        btnManager.addActionListener(e -> openDashboard("Manager"));
        btnSales.addActionListener(e -> openDashboard("Salesperson"));
        btnTech.addActionListener(e -> openDashboard("Technician"));

        btnPanel.add(btnManager);
        btnPanel.add(btnSales);
        btnPanel.add(btnTech);

        add(btnPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createRoleButton(String role, String desc) {
        JButton btn = new JButton("<html><center>" + role + "<br><small>" + desc + "</small></center></html>");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(44, 62, 80));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void openDashboard(String role) {
        new Dashboard(role).setVisible(true);
        dispose();
    }
}
