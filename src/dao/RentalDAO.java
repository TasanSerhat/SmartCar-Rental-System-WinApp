package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Vector;

public class RentalDAO {

    public Vector<Vector<Object>> getAllRentals() {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT r.rentalID, res.reservationID, c.firstName || ' ' || c.lastName AS Customer, " +
                "v.plateNo, r.totalDays, r.totalCost " +
                "FROM Rental r " +
                "JOIN Reservation res ON r.reservationID = res.reservationID " +
                "JOIN Customer c ON res.customerID = c.customerID " +
                "JOIN Vehicle v ON res.vehicleID = v.vehicleID " +
                "ORDER BY r.rentalID DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("rentalID"));
                row.add(rs.getInt("reservationID"));
                row.add(rs.getString("Customer"));
                row.add(rs.getString("plateNo"));
                row.add(rs.getInt("totalDays"));
                row.add(rs.getBigDecimal("totalCost"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
