
package com.campusconnect.dao;

import com.campusconnect.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SavedEventDAO {

    public boolean saveEvent(int userId, int eventId)
            throws SQLException {

        if (userId <= 0 || eventId <= 0) {
            return false;
        }

        String eventSql =
                "SELECT event_id FROM events "
                + "WHERE event_id = ? AND status = 'PUBLISHED'";

        String saveSql =
                "INSERT INTO saved_events (user_id, event_id) "
                + "VALUES (?, ?)";

        try (Connection connection = DBUtil.getConnection()) {

            // Confirm the event exists and is published.
            try (PreparedStatement statement =
                         connection.prepareStatement(eventSql)) {

                statement.setInt(1, eventId);

                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) {
                        return false;
                    }
                }
            }

            // Save the bookmark.
            try (PreparedStatement statement =
                         connection.prepareStatement(saveSql)) {

                statement.setInt(1, userId);
                statement.setInt(2, eventId);

                return statement.executeUpdate() == 1;

            } catch (SQLException e) {
                // MySQL duplicate-key error: bookmark already exists.
                if ("23000".equals(e.getSQLState())) {
                    return false;
                }
                throw e;
            }
        }
    }

    public boolean removeSavedEvent(int userId, int eventId)
            throws SQLException {

        if (userId <= 0 || eventId <= 0) {
            return false;
        }

        String sql =
                "DELETE FROM saved_events "
                + "WHERE user_id = ? AND event_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, eventId);

            return statement.executeUpdate() == 1;
        }
    }

    public List<Integer> getSavedEventIdsByUserId(int userId)
            throws SQLException {

        List<Integer> eventIds = new ArrayList<>();

        if (userId <= 0) {
            return eventIds;
        }

        String sql =
                "SELECT event_id FROM saved_events "
                + "WHERE user_id = ? ORDER BY saved_at DESC";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    eventIds.add(result.getInt("event_id"));
                }
            }
        }

        return eventIds;
    }

    public boolean isEventSaved(int userId, int eventId)
            throws SQLException {

        if (userId <= 0 || eventId <= 0) {
            return false;
        }

        String sql =
                "SELECT 1 FROM saved_events "
                + "WHERE user_id = ? AND event_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, eventId);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }
}
