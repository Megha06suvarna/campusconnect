package com.campusconnect.model.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.campusconnect.util.DBConnection;

/**
 * Developer helper: right-click > Run As > Java Application to confirm that
 * the driver, the credentials in DBConnection and the SQL script all work.
 * It prints the number of rows in each of the four tables.
 */
public final class DatabaseCheck {

    private DatabaseCheck() {
    }

    public static void main(String[] args) {
        String[] tables = {"users", "events", "saved_events", "event_registrations"};
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {
            System.out.println("Connected. MySQL version: " + con.getMetaData().getDatabaseProductVersion());
            for (String table : tables) {
                try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
                    rs.next();
                    System.out.println(table + ": " + rs.getInt(1) + " rows");
                }
            }
            System.out.println("Database check passed.");
        } catch (SQLException ex) {
            System.out.println("Database check FAILED: " + ex.getMessage());
        }
    }
}