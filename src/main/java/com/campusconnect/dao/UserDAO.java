
package com.campusconnect.dao;

import com.campusconnect.model.User;
import com.campusconnect.util.DBUtil;
import com.campusconnect.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    public User loginUser(String email, String password)
            throws SQLException {

        if (email == null || password == null
                || email.trim().isEmpty() || password.isEmpty()) {
            return null;
        }

        String sql = "SELECT user_id, full_name, email, password_hash, "
                + "role, department, created_at "
                + "FROM users WHERE email = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email.trim());

            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String storedPassword = rs.getString("password_hash");

                if (!PasswordUtil.verifyPassword(
                        password, storedPassword)) {
                    return null;
                }

                return mapUser(rs);
            }
        }
    }

    public boolean registerUser(User user) throws SQLException {

        if (user == null
                || isBlank(user.getFullName())
                || isBlank(user.getEmail())
                || isBlank(user.getPasswordHash())) {
            return false;
        }

        String sql = "INSERT INTO users "
                + "(full_name, email, password, password_hash, "
                + "role, department) VALUES (?, ?, ?, ?, ?, ?)";

        String passwordHash =
                PasswordUtil.hashPassword(user.getPasswordHash());

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getFullName().trim());
            statement.setString(2, user.getEmail().trim());
            statement.setString(3, passwordHash);
            statement.setString(4, passwordHash);
            statement.setString(5, "STUDENT");
            statement.setString(6, user.getDepartment());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                return false;
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setUserId(keys.getInt(1));
                }
            }

            user.setRole("STUDENT");
            return true;

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                return false;
            }
            throw e;
        }
    }

    public boolean updateUserProfile(User user)
            throws SQLException {

        if (user == null || user.getUserId() <= 0
                || isBlank(user.getFullName())
                || isBlank(user.getEmail())) {
            return false;
        }

        String sql = "UPDATE users SET full_name = ?, email = ?, "
                + "department = ? WHERE user_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, user.getFullName().trim());
            statement.setString(2, user.getEmail().trim());
            statement.setString(3, user.getDepartment());
            statement.setInt(4, user.getUserId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                return false;
            }
            throw e;
        }
    }

    public User getUserById(int userId) throws SQLException {

        String sql = "SELECT user_id, full_name, email, password_hash, "
                + "role, department, created_at "
                + "FROM users WHERE user_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapUser(rs) : null;
            }
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {

        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(rs.getString("role"));
        user.setDepartment(rs.getString("department"));
        user.setCreatedAt(rs.getTimestamp("created_at"));

        return user;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
