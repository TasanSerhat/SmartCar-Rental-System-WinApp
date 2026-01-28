package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Vector;

public class BranchDAO {

    public Vector<Vector<Object>> getAllBranches() {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT branchID, branchName, city, phone FROM Branch";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("branchID"));
                row.add(rs.getString("branchName"));
                row.add(rs.getString("city"));
                row.add(rs.getString("phone"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
