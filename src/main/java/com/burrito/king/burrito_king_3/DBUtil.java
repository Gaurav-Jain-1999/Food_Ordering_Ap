package com.burrito.king.burrito_king_3;

import javafx.stage.Stage;
import java.sql.*;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class DBUtil {

    private static final String DB_URL = "jdbc:sqlite:Database/Burrito_King_Database.db";
    private static Connection connection = null;
    private static Stage stage;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found: " + e.getMessage());
        }
    }

    // Connect to the database
    public static Connection dbConnect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    // Disconnect the database
    public static void dbDisconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Execute SQL queries like INSERT, UPDATE, DELETE
    public static void dbExecuteQuery(String sqlStmt) throws SQLException {
        try (Connection conn = dbConnect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at dbExecuteQuery: " + e);
            throw e;
        } finally {
            dbDisconnect();
        }
    }

    // Execute SQL SELECT queries
    public static ResultSet dbExecute(String sqlQuery) throws SQLException {
        ResultSet rs = null;
        CachedRowSet crs = null;
        try (Connection conn = dbConnect();
             Statement stmt = conn.createStatement()) {
            rs = stmt.executeQuery(sqlQuery);
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(rs);
        } catch (SQLException e) {
            System.out.println("Problem occurred in dbExecute: " + e);
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            dbDisconnect();
        }
        return crs;
    }

}
