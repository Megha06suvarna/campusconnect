package com.campusconnect.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.campusconnect.model.util.TimeUtil;

/**
 * One row of event_registrations. Only INTERNAL registrations are ever stored here.
 * Depending on the query, either {@code event} (student's own list) or the student
 * fields (organizer's list) are filled in.
 */
public class Registration implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String STATUS_REGISTERED = "REGISTERED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private int registrationId;
    private int userId;
    private int eventId;
    private String status = STATUS_REGISTERED;
    private LocalDateTime registeredAt;

    private Event event;               // joined event (My Registrations)
    private String studentName;        // joined student (organizer view)
    private String studentEmail;
    private String studentDepartment;

    public Registration() {
    }

    public boolean isActive() {
        return STATUS_REGISTERED.equals(status);
    }

    public String getRegisteredAtDisplay() {
        return TimeUtil.formatDateTime(registeredAt);
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentDepartment() {
        return studentDepartment;
    }

    public void setStudentDepartment(String studentDepartment) {
        this.studentDepartment = studentDepartment;
    }
}