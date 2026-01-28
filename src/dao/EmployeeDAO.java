package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Vector;

public class EmployeeDAO {

    public Vector<Vector<Object>> getEmployeesByBranch(int branchID) {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT employeeID, firstName, lastName, role, salary FROM Employee WHERE branchID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, branchID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("employeeID"));
                    row.add(rs.getString("firstName"));
                    row.add(rs.getString("lastName"));
                    row.add(rs.getString("role"));
                    row.add(rs.getBigDecimal("salary"));
                    data.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public Vector<Vector<Object>> getAllEmployees() {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT e.employeeID, e.firstName, e.lastName, e.role, e.salary, b.branchName " +
                "FROM Employee e " +
                "JOIN Branch b ON e.branchID = b.branchID " +
                "ORDER BY e.role, e.lastName";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("employeeID"));
                row.add(rs.getString("firstName"));
                row.add(rs.getString("lastName"));
                row.add(rs.getString("role"));
                row.add(rs.getBigDecimal("salary"));
                row.add(rs.getString("branchName"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
