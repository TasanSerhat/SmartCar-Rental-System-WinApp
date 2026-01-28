package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AdditionalServiceDAO {

    // Returns Map of ServiceID -> "Name ($Cost)"
    public Map<Integer, String> getAllServices() {
        Map<Integer, String> services = new HashMap<>();
        String sql = "SELECT serviceID, serviceName, cost FROM AdditionalServices";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String label = rs.getString("serviceName") + " ($" + rs.getBigDecimal("cost") + ")";
                services.put(rs.getInt("serviceID"), label);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
}
