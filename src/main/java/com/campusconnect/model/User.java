package com.campusconnect.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String fullName;
    private String email;
    private String passwordHash;
    private String role;
    private String department;
    private Timestamp createdAt;

    public User() {
    }

    public User(int userId, String fullName, String email, String passwordHash, String role, String department, Timestamp createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.department = department;
        this.createdAt = createdAt;
    }

    // Role helper methods used by EventDAO and other classes
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }

    public boolean isOrganizer() {
        return "ORGANIZER".equalsIgnoreCase(this.role);
    }

    public boolean isStudent() {
        return "STUDENT".equalsIgnoreCase(this.role);
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}