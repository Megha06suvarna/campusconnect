
package com.campusconnect.dao;

import com.campusconnect.model.Event;
import com.campusconnect.util.DBUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public boolean createEvent(Event event) throws SQLException {
        if (event == null) {
            return false;
        }

        LocalDateTime start = parseDateTime(event.getStartTime());
        LocalDateTime end = parseDateTime(event.getEndTime());
        LocalDateTime deadline =
                parseDateTime(event.getRegistrationDeadline());

        if (start == null || end == null || deadline == null
                || !start.isBefore(end)
                || deadline.isAfter(start)
                || event.getOrganizerId() <= 0
                || isBlank(event.getTitle())
                || isBlank(event.getDepartment())) {
            return false;
        }

        String venue = event.getVenue();
        String location = event.getLocation();

        if (isBlank(venue)) {
            venue = location;
        }
        if (isBlank(location)) {
            location = venue;
        }
        if (isBlank(venue)) {
            return false;
        }

        String sql = "INSERT INTO events "
                + "(title, description, department, category, venue, location, "
                + "event_date, capacity, start_time, end_time, "
                + "registration_deadline, registration_type, external_url, "
                + "created_by, organizer_id, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, event.getTitle().trim());
            statement.setString(2, event.getDescription());
            statement.setString(3, event.getDepartment());
            statement.setString(4, event.getCategory());
            statement.setString(5, venue);
            statement.setString(6, location);
            statement.setDate(7, Date.valueOf(start.toLocalDate()));

            if (event.getCapacity() > 0) {
                statement.setInt(8, event.getCapacity());
            } else {
                statement.setNull(8, Types.INTEGER);
            }

            statement.setTimestamp(9, Timestamp.valueOf(start));
            statement.setTimestamp(10, Timestamp.valueOf(end));
            statement.setTimestamp(11, Timestamp.valueOf(deadline));
            statement.setNull(12, Types.VARCHAR);
            statement.setNull(13, Types.VARCHAR);
            statement.setInt(14, event.getOrganizerId());
            statement.setInt(15, event.getOrganizerId());
            statement.setString(16, "PUBLISHED");

            return statement.executeUpdate() == 1;
        }
    }

    public List<Event> getAllEvents() throws SQLException {
        String sql = "SELECT * FROM events "
                + "WHERE status = 'PUBLISHED' ORDER BY start_time ASC";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return mapResults(resultSet);
        }
    }

    public List<Event> getFilteredEvents(
            String category, String location, String search)
            throws SQLException {

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM events WHERE status = 'PUBLISHED' ");
        List<String> values = new ArrayList<>();

        if (!isBlank(category)
                && !"all".equalsIgnoreCase(category.trim())) {
            sql.append("AND category = ? ");
            values.add(category.trim());
        }

        if (!isBlank(location)) {
            sql.append("AND (location LIKE ? OR venue LIKE ?) ");
            values.add("%" + location.trim() + "%");
            values.add("%" + location.trim() + "%");
        }

        if (!isBlank(search)) {
            sql.append("AND (title LIKE ? OR description LIKE ?) ");
            values.add("%" + search.trim() + "%");
            values.add("%" + search.trim() + "%");
        }

        sql.append("ORDER BY start_time ASC");

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < values.size(); i++) {
                statement.setString(i + 1, values.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                return mapResults(resultSet);
            }
        }
    }

    public Event getEventById(int eventId) throws SQLException {
        String sql = "SELECT * FROM events WHERE event_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapEvent(resultSet) : null;
            }
        }
    }

    public boolean updateEvent(Event event) throws SQLException {
        if (event == null) {
            return false;
        }

        LocalDateTime start = parseDateTime(event.getStartTime());
        LocalDateTime end = parseDateTime(event.getEndTime());
        LocalDateTime deadline =
                parseDateTime(event.getRegistrationDeadline());

        if (start == null || end == null || deadline == null
                || !start.isBefore(end)
                || deadline.isAfter(start)
                || event.getEventId() <= 0
                || event.getOrganizerId() <= 0
                || isBlank(event.getTitle())
                || isBlank(event.getDepartment())) {
            return false;
        }

        String venue = event.getVenue();
        String location = event.getLocation();

        if (isBlank(venue)) {
            venue = location;
        }
        if (isBlank(location)) {
            location = venue;
        }
        if (isBlank(venue)) {
            return false;
        }

        String sql = "UPDATE events SET title = ?, description = ?, "
                + "department = ?, category = ?, venue = ?, location = ?, "
                + "event_date = ?, capacity = ?, start_time = ?, "
                + "end_time = ?, registration_deadline = ? "
                + "WHERE event_id = ? AND organizer_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, event.getTitle().trim());
            statement.setString(2, event.getDescription());
            statement.setString(3, event.getDepartment());
            statement.setString(4, event.getCategory());
            statement.setString(5, venue);
            statement.setString(6, location);
            statement.setDate(7, Date.valueOf(start.toLocalDate()));

            if (event.getCapacity() > 0) {
                statement.setInt(8, event.getCapacity());
            } else {
                statement.setNull(8, Types.INTEGER);
            }

            statement.setTimestamp(9, Timestamp.valueOf(start));
            statement.setTimestamp(10, Timestamp.valueOf(end));
            statement.setTimestamp(11, Timestamp.valueOf(deadline));
            statement.setInt(12, event.getEventId());
            statement.setInt(13, event.getOrganizerId());

            return statement.executeUpdate() == 1;
        }
    }

    public boolean deleteEvent(int eventId, int organizerId)
            throws SQLException {

        String sql = "DELETE FROM events "
                + "WHERE event_id = ? AND organizer_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, eventId);
            statement.setInt(2, organizerId);

            return statement.executeUpdate() == 1;
        }
    }

    public List<Event> getRegisteredEventsByUserId(int userId)
            throws SQLException {

        String sql = "SELECT e.* FROM events e "
                + "INNER JOIN event_registrations r "
                + "ON e.event_id = r.event_id "
                + "WHERE r.user_id = ? AND r.status = 'REGISTERED' "
                + "ORDER BY e.start_time ASC";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return mapResults(resultSet);
            }
        }
    }

    public List<Event> getSavedEventsByUserId(int userId)
            throws SQLException {

        String sql = "SELECT e.* FROM events e "
                + "INNER JOIN saved_events s "
                + "ON e.event_id = s.event_id "
                + "WHERE s.user_id = ? ORDER BY e.start_time ASC";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return mapResults(resultSet);
            }
        }
    }

    private List<Event> mapResults(ResultSet resultSet)
            throws SQLException {

        List<Event> events = new ArrayList<>();

        while (resultSet.next()) {
            events.add(mapEvent(resultSet));
        }

        return events;
    }

    private Event mapEvent(ResultSet rs) throws SQLException {
        Event event = new Event();

        event.setEventId(rs.getInt("event_id"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setDepartment(rs.getString("department"));
        event.setCategory(rs.getString("category"));
        event.setVenue(rs.getString("venue"));
        event.setLocation(rs.getString("location"));

        Timestamp start = rs.getTimestamp("start_time");
        Timestamp end = rs.getTimestamp("end_time");
        Timestamp deadline = rs.getTimestamp("registration_deadline");

        if (start != null) {
            event.setStartTime(start.toLocalDateTime().toString());
        }

        Date eventDate = rs.getDate("event_date");
        if (eventDate != null) {
            event.setEventDate(eventDate.toString());
        }

        if (end != null) {
            event.setEndTime(end.toLocalDateTime().toString());
        }

        if (deadline != null) {
            event.setRegistrationDeadline(
                    deadline.toLocalDateTime().toString());
        }

        event.setCapacity(rs.getInt("capacity"));
        event.setOrganizerId(rs.getInt("organizer_id"));

        return event;
    }

    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDateTime.parse(value.trim().replace(' ', 'T'));
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
