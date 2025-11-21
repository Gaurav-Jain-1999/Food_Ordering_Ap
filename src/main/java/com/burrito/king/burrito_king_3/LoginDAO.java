package com.burrito.king.burrito_king_3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    public static boolean isLogin(String username, String password) {
        String sql = "SELECT * FROM Customers WHERE log_username = ? AND log_password = ?";

        try (Connection conn = DBUtil.dbConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.out.println("Login validation failed: " + e.getMessage());
            return false;
        }
    }

    public static int getCusId(String username, String password) {
        String sql = "SELECT Cus_ID FROM Customers WHERE log_username = ? AND log_password = ?";

        try (Connection conn = DBUtil.dbConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Cus_ID");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching Cus_ID: " + e.getMessage());
        }
        return -1; // Return -1 or some other invalid ID if not found
    }
}
