package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Vector;

public class VehicleDAO {

    // Fetch only available vehicles for reservation
    public Vector<String> getAvailableVehicles() {
        Vector<String> vehicles = new Vector<>();
        String sql = "SELECT v.vehicleID, m.brand, m.modelName, v.plateNo, m.pricePerDay " +
                "FROM Vehicle v " +
                "JOIN VehicleModel m ON v.modelID = m.modelID " +
                "WHERE v.status = 'Available'";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String item = rs.getInt("vehicleID") + ": " +
                        rs.getString("brand") + " " +
                        rs.getString("modelName") + " - " +
                        rs.getString("plateNo") +
                        " ($" + rs.getBigDecimal("pricePerDay") + ")";
                vehicles.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    //Search Method
    public Vector<Vector<Object>> searchVehicles(String query) {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT v.vehicleID, v.plateNo, m.brand || ' ' || m.modelName, b.branchName, v.status, m.pricePerDay "
                +
                "FROM Vehicle v " +
                "JOIN VehicleModel m ON v.modelID = m.modelID " +
                "JOIN Branch b ON v.branchID = b.branchID " +
                "WHERE v.plateNo ILIKE ? OR m.brand ILIKE ? OR m.modelName ILIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String pattern = "%" + query + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt(1));
                    row.add(rs.getString(2));
                    row.add(rs.getString(3));
                    row.add(rs.getString(4));
                    row.add(rs.getString(5));
                    row.add(rs.getBigDecimal(6));
                    data.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    //Load Method
    public Vector<Vector<Object>> getAllVehicles() {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT v.vehicleID, v.plateNo, m.brand || ' ' || m.modelName, b.branchName, v.status, m.pricePerDay "
                +
                "FROM Vehicle v " +
                "JOIN VehicleModel m ON v.modelID = m.modelID " +
                "JOIN Branch b ON v.branchID = b.branchID";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt(1));
                row.add(rs.getString(2));
                row.add(rs.getString(3));
                row.add(rs.getString(4));
                row.add(rs.getString(5));
                row.add(rs.getBigDecimal(6));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
