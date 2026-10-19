package com.campusconnect.model;

public class EventFilter {
    private String searchQuery;
    private String category;
    private String location;

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}