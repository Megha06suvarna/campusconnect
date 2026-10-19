
package com.campusconnect.dao;

import com.campusconnect.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationDAO {

    public boolean registerUserForEvent(int userId, int eventId)
            throws SQLException {

        if (userId <= 0 || eventId <= 0) {
            return false;
        }

        String eventSql =
                "SELECT capacity FROM events "
                + "WHERE event_id = ? AND status = 'PUBLISHED'";

        String duplicateSql =
                "SELECT registration_id FROM event_registrations "
                + "WHERE user_id = ? AND event_id = ?";

        String countSql =
                "SELECT COUNT(*) FROM event_registrations "
                + "WHERE event_id = ? AND status = 'REGISTERED'";

        String insertSql =
                "INSERT INTO event_registrations "
                + "(user_id, event_id, status) "
                + "VALUES (?, ?, 'REGISTERED')";

        try (Connection connection = DBUtil.getConnection()) {
            connection.setAutoCommit(false);

            try {
                // 1. Check that the event exists and is published.
                int capacity;

                try (PreparedStatement statement =
                             connection.prepareStatement(eventSql)) {

                    statement.setInt(1, eventId);

                    try (ResultSet result = statement.executeQuery()) {
                        if (!result.next()) {
                            connection.rollback();
                            return false;
                        }

                        capacity = result.getInt("capacity");

                        if (result.wasNull()) {
                            capacity = 0;
                        }
                    }
                }

                // 2. Prevent duplicate registrations.
                try (PreparedStatement statement =
                             connection.prepareStatement(duplicateSql)) {

                    statement.setInt(1, userId);
                    statement.setInt(2, eventId);

                    try (ResultSet result = statement.executeQuery()) {
                        if (result.next()) {
                            connection.rollback();
                            return false;
                        }
                    }
                }

                // 3. Check event capacity; zero means unlimited.
                if (capacity > 0) {
                    try (PreparedStatement statement =
                                 connection.prepareStatement(countSql)) {

                        statement.setInt(1, eventId);

                        try (ResultSet result = statement.executeQuery()) {
                            if (result.next()
                                    && result.getInt(1) >= capacity) {
                                connection.rollback();
                                return false;
                            }
                        }
                    }
                }

                // 4. Save the registration.
                try (PreparedStatement statement =
                             connection.prepareStatement(insertSql)) {

                    statement.setInt(1, userId);
                    statement.setInt(2, eventId);

                    int rows = statement.executeUpdate();

                    if (rows != 1) {
                        connection.rollback();
                        return false;
                    }
                }

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();

                // Another request may have registered the same user.
                if ("23000".equals(e.getSQLState())) {
                    return false;
                }

                throw e;

            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
}
