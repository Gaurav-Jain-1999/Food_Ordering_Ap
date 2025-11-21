package com.burrito.king.burrito_king_3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationDAO {

    public static void insert(String username, String password, String name, String surname, String mobile) throws SQLException {
        String sql = "INSERT INTO Customers (log_username, log_password, Cus_FName, Cus_LName, mobile) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.dbConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, surname);
            pstmt.setString(5, mobile);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while inserting data: " + e.getMessage());
            throw e;
        } finally {
            DBUtil.dbDisconnect();
        }
    }
}
