package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Vector;

public class ReservationDAO {

    public Vector<Vector<Object>> getAllReservations() {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT r.reservationID, c.firstName || ' ' || c.lastName AS Customer, " +
                "v.plateNo, m.brand || ' ' || m.modelName AS Vehicle, " +
                "r.startDate, r.endDate, r.totalPrice, r.status " +
                "FROM Reservation r " +
                "JOIN Customer c ON r.customerID = c.customerID " +
                "JOIN Vehicle v ON r.vehicleID = v.vehicleID " +
                "JOIN VehicleModel m ON v.modelID = m.modelID " +
                "ORDER BY r.reservationID DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("reservationID"));
                row.add(rs.getString("Customer"));
                row.add(rs.getString("plateNo"));
                row.add(rs.getString("Vehicle"));
                row.add(rs.getDate("startDate"));
                row.add(rs.getDate("endDate"));
                row.add(rs.getBigDecimal("totalPrice"));
                row.add(rs.getString("status"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public boolean cancelReservation(int id) {
        String sqlGetVehicle = "SELECT vehicleID FROM Reservation WHERE reservationID = ?";
        String sqlUpdateRes = "UPDATE Reservation SET status = 'Cancelled' WHERE reservationID = ?";
        String sqlUpdateVeh = "UPDATE Vehicle SET status = 'Available' WHERE vehicleID = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); 

            // 1. Get Vehicle ID
            int vehicleID = -1;
            try (PreparedStatement pst1 = conn.prepareStatement(sqlGetVehicle)) {
                pst1.setInt(1, id);
                ResultSet rs = pst1.executeQuery();
                if (rs.next()) {
                    vehicleID = rs.getInt("vehicleID");
                }
            }

            // 2. Cancel Reservation
            try (PreparedStatement pst2 = conn.prepareStatement(sqlUpdateRes)) {
                pst2.setInt(1, id);
                pst2.executeUpdate();
            }

            // 3. Update Vehicle Status (if found)
            if (vehicleID != -1) {
                try (PreparedStatement pst3 = conn.prepareStatement(sqlUpdateVeh)) {
                    pst3.setInt(1, vehicleID);
                    pst3.executeUpdate();
                }
            }

            conn.commit(); 
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
