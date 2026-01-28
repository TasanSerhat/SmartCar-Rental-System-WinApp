package ui;

import dao.BranchDAO;
import dao.EmployeeDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class MasterDetailForm extends JFrame {
    private JTable branchTable;
    private JTable employeeTable;
    private DefaultTableModel branchModel;
    private DefaultTableModel employeeModel;

    private BranchDAO branchDAO;
    private EmployeeDAO employeeDAO;

    public MasterDetailForm() {
        branchDAO = new BranchDAO();
        employeeDAO = new EmployeeDAO();

        setTitle("Branch & Employee Management (Master-Detail)");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1, 0, 10));
        getContentPane().setBackground(new Color(236, 240, 241));

        // --- MASTER VIEW (Branches) ---
        JPanel masterPanel = new JPanel(new BorderLayout());
        masterPanel.setBackground(Color.WHITE);
        masterPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 5, 10),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                        "Branches (Select to view employees)")));

        branchModel = new DefaultTableModel(new String[] { "ID", "Branch Name", "City", "Phone" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        branchTable = createStyledTable(branchModel);
        masterPanel.add(new JScrollPane(branchTable), BorderLayout.CENTER);

        // --- DETAIL VIEW (Employees) ---
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(5, 10, 10, 10),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                        "Employees in Selected Branch")));

        employeeModel = new DefaultTableModel(new String[] { "ID", "First Name", "Last Name", "Role", "Salary" }, 0);
        employeeTable = createStyledTable(employeeModel);
        detailPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        add(masterPanel);
        add(detailPanel);

        // Load Branches
        loadBranches();

        // Listener for Master Table
        branchTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = branchTable.getSelectedRow();
                if (selectedRow != -1) {
                    int branchID = Integer.parseInt(branchModel.getValueAt(selectedRow, 0).toString());
                    loadEmployees(branchID);
                }
            }
        });

        setVisible(true);
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(44, 62, 80)); // Dark Blue
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        return table;
    }

    private void loadBranches() {
        branchModel.setRowCount(0);
        Vector<Vector<Object>> data = branchDAO.getAllBranches();
        for (Vector<Object> row : data) {
            branchModel.addRow(row);
        }
    }

    private void loadEmployees(int branchID) {
        employeeModel.setRowCount(0);
        Vector<Vector<Object>> data = employeeDAO.getEmployeesByBranch(branchID);
        for (Vector<Object> row : data) {
            employeeModel.addRow(row);
        }
    }
}
