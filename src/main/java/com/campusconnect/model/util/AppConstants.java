package com.campusconnect.model.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Fixed lists and names shared by forms, validation and filters.
 * Keeping them in one place means the register form, the event form and the
 * server-side validation always agree with each other.
 */
public final class AppConstants {

    public static final String APP_NAME = "CampusConnect";
    public static final String TAGLINE = "One Platform. Every Campus Event.";

    /** Departments offered on the registration form and the event form. */
    public static final List<String> DEPARTMENTS = Collections.unmodifiableList(Arrays.asList(
            "Computer Science",
            "Electronics & Communication",
            "Mechanical Engineering",
            "Civil Engineering",
            "Business Administration",
            "Student Affairs"));

    /** Event categories offered on the event form and the filter bar. */
    public static final List<String> CATEGORIES = Collections.unmodifiableList(Arrays.asList(
            "Technical",
            "Workshop",
            "Seminar",
            "Hackathon",
            "Cultural",
            "Sports",
            "Career",
            "Club Activity"));

    private AppConstants() {
    }
}