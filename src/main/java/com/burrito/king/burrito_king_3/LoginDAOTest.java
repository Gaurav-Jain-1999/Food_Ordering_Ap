package com.burrito.king.burrito_king_3;

public class LoginDAOTest {

    public static void main(String[] args) {
        testLogin();
    }

    private static void testLogin() {
        // Test with correct credentials
        boolean result1 = LoginDAO.isLogin("johndoe", "password123");
        System.out.println("Login with correct credentials: " + result1); // Should print: true

        // Test with incorrect credentials
        boolean result2 = LoginDAO.isLogin("testuser", "wrongpassword");
        System.out.println("Login with incorrect credentials: " + result2); // Should print: false

        // Test with non-existing user
        boolean result3 = LoginDAO.isLogin("nonuser", "nopassword");
        System.out.println("Login with non-existing user: " + result3); // Should print: false
    }
}
