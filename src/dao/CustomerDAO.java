package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Vector;

public class CustomerDAO {

    public Vector<Vector<Object>> getAllCustomers() {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT * FROM Customer ORDER BY customerID";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("customerID"));
                row.add(rs.getString("firstName"));
                row.add(rs.getString("lastName"));
                row.add(rs.getString("licenseNo"));
                row.add(rs.getString("phone"));
                row.add(rs.getString("email"));
                row.add(rs.getString("address"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public Vector<Vector<Object>> searchCustomers(String query) {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT * FROM Customer WHERE firstName ILIKE ? OR lastName ILIKE ? OR licenseNo ILIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("customerID"));
                    row.add(rs.getString("firstName"));
                    row.add(rs.getString("lastName"));
                    row.add(rs.getString("licenseNo"));
                    row.add(rs.getString("phone"));
                    row.add(rs.getString("email"));
                    row.add(rs.getString("address"));
                    data.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public boolean addCustomer(String firstName, String lastName, String license, String phone, String email,
            String address) {
        String sql = "INSERT INTO Customer (firstName, lastName, licenseNo, phone, email, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, license);
            pstmt.setString(4, phone);
            pstmt.setString(5, email);
            pstmt.setString(6, address);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCustomer(int id, String firstName, String lastName, String license, String phone, String email,
            String address) {
        String sql = "UPDATE Customer SET firstName=?, lastName=?, licenseNo=?, phone=?, email=?, address=? WHERE customerID=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, license);
            pstmt.setString(4, phone);
            pstmt.setString(5, email);
            pstmt.setString(6, address);
            pstmt.setInt(7, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Begin Transaction

            // 1. Find all Reservations for this customer
            String sqlGetRes = "SELECT reservationID FROM Reservation WHERE customerID = ?";

            try (PreparedStatement pstRes = conn.prepareStatement(sqlGetRes)) {
                pstRes.setInt(1, id);
                ResultSet rsRes = pstRes.executeQuery();

                while (rsRes.next()) {
                    int reservationID = rsRes.getInt("reservationID");

                    // 2. For each Reservation, check for Rental
                    String sqlGetRental = "SELECT rentalID FROM Rental WHERE reservationID = ?";
                    try (PreparedStatement pstRental = conn.prepareStatement(sqlGetRental)) {
                        pstRental.setInt(1, reservationID);
                        ResultSet rsRental = pstRental.executeQuery();

                        while (rsRental.next()) {
                            int rentalID = rsRental.getInt("rentalID");

                            // 3. Break Circular Dependency: Link Rental -> Payment
                            // Set Rental.paymentID to NULL first
                            String sqlUnlink = "UPDATE Rental SET paymentID = NULL WHERE rentalID = ?";
                            try (PreparedStatement pstUnlink = conn.prepareStatement(sqlUnlink)) {
                                pstUnlink.setInt(1, rentalID);
                                pstUnlink.executeUpdate();
                            }

                            // 4. Delete Payment (depends on Rental)
                            String sqlDelPay = "DELETE FROM Payment WHERE rentalID = ?";
                            try (PreparedStatement pstDelPay = conn.prepareStatement(sqlDelPay)) {
                                pstDelPay.setInt(1, rentalID);
                                pstDelPay.executeUpdate();
                            }

                            // 5. Delete Rental
                            String sqlDelRental = "DELETE FROM Rental WHERE rentalID = ?";
                            try (PreparedStatement pstDelRental = conn.prepareStatement(sqlDelRental)) {
                                pstDelRental.setInt(1, rentalID);
                                pstDelRental.executeUpdate();
                            }
                        }
                    }
                }
            }

            // 6. Finally Delete Customer (Cascade will handle Reservation)
            String sqlDelCust = "DELETE FROM Customer WHERE customerID=?";
            try (PreparedStatement pstDelCust = conn.prepareStatement(sqlDelCust)) {
                pstDelCust.setInt(1, id);
                pstDelCust.executeUpdate();
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
