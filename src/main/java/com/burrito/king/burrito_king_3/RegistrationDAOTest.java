package com.burrito.king.burrito_king_3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationDAOTest {

    public static void main(String[] args) {
        testRegistration();
    }

    private static void testRegistration() {
        try {
            // Clean up the test user if it exists
            deleteTestUser();

            // Test registration with a new user
            RegistrationDAO.insert("testuser", "testpassword", "Test", "User", "1234567890");

            // Verify that the user was inserted
            boolean userExists = checkUserExists("testuser", "testpassword");
            System.out.println("Registration successful: " + userExists); // Should print: true

            // Clean up the test user after the test
        } catch (SQLException e) {
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean checkUserExists(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Customers WHERE log_username = ? AND log_password = ?";
        try (Connection conn = DBUtil.dbConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private static void deleteTestUser() throws SQLException {
        String sql = "DELETE FROM Customers WHERE log_username = 'testuser'";
        try (Connection conn = DBUtil.dbConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
        }
    }
}
