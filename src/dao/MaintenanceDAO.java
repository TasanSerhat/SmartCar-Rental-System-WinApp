package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Vector;

public class MaintenanceDAO {

    public Vector<Vector<Object>> getAllMaintenanceRecords() {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT m.vehicleID, v.plateNo, mo.brand, m.date, m.description, m.cost " +
                "FROM Maintenance m " +
                "JOIN Vehicle v ON m.vehicleID = v.vehicleID " +
                "JOIN VehicleModel mo ON v.modelID = mo.modelID " +
                "ORDER BY m.date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("vehicleID"));
                row.add(rs.getString("plateNo"));
                row.add(rs.getString("brand"));
                row.add(rs.getDate("date"));
                row.add(rs.getString("description"));
                row.add(rs.getBigDecimal("cost"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
