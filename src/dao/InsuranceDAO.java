package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Vector;

public class InsuranceDAO {

    public Vector<Vector<Object>> getAllInsuranceRecords() {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT i.insuranceID, v.plateNo, m.brand, i.policyType, i.provider, i.startDate, i.expiryDate " +
                "FROM Insurance i " +
                "JOIN Vehicle v ON i.vehicleID = v.vehicleID " +
                "JOIN VehicleModel m ON v.modelID = m.modelID";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("insuranceID"));
                row.add(rs.getString("plateNo"));
                row.add(rs.getString("brand"));
                row.add(rs.getString("policyType"));
                row.add(rs.getString("provider"));
                row.add(rs.getDate("startDate"));
                row.add(rs.getDate("expiryDate"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
