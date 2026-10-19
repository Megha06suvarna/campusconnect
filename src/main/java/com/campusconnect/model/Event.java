package com.campusconnect.model;

public class Event {
    private int eventId;
    private String title;
    private String description;
    private String venue;
    private String location;
    private String startTime;
    private String eventDate;
    private String endTime;
    private int organizerId;
    private int capacity;
    private String category;
    private String department;
    private String registrationDeadline;

    // Getters and Setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { 
        this.venue = venue; 
        if (this.location == null) this.location = venue;
    }

    public String getLocation() { return location != null ? location : venue; }
    public void setLocation(String location) { 
        this.location = location; 
        if (this.venue == null) this.venue = location;
    }

    public String getStartTime() { return startTime != null ? startTime : eventDate; }
    public void setStartTime(String startTime) { 
        this.startTime = startTime; 
        if (this.eventDate == null) this.eventDate = startTime;
    }

    public String getEventDate() { return eventDate != null ? eventDate : startTime; }
    public void setEventDate(String eventDate) { 
        this.eventDate = eventDate; 
        if (this.startTime == null) this.startTime = eventDate;
    }
    
    // Overload for Object/Timestamp type binding
    public void setEventDate(Object eventDate) {
        if (eventDate != null) {
            this.eventDate = eventDate.toString();
            if (this.startTime == null) this.startTime = this.eventDate;
        }
    }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setEndTime(Object endTime) { 
        if (endTime != null) this.endTime = endTime.toString(); 
    }

    public int getOrganizerId() { return organizerId; }
    public void setOrganizerId(int organizerId) { this.organizerId = organizerId; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getRegistrationDeadline() { 
        return registrationDeadline != null ? registrationDeadline : eventDate; 
    }
    public void setRegistrationDeadline(String registrationDeadline) { 
        this.registrationDeadline = registrationDeadline; 
    }
    public void setRegistrationDeadline(Object registrationDeadline) {
        if (registrationDeadline != null) {
            this.registrationDeadline = registrationDeadline.toString();
        }
    }
}