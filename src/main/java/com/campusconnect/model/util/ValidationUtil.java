package com.campusconnect.model.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.campusconnect.model.Event;

public final class ValidationUtil {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidationUtil() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.length() <= 150 && EMAIL.matcher(email.trim()).matches();
    }

    public static boolean isAcceptablePassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 100) {
            return false;
        }
        boolean letter = false;
        boolean digit = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLetter(c)) {
                letter = true;
            } else if (Character.isDigit(c)) {
                digit = true;
            }
        }
        return letter && digit;
    }

    public static boolean isValidHttpUrl(String url) {
        if (url == null) {
            return false;
        }
        String trimmed = url.trim();
        if (trimmed.isEmpty() || trimmed.length() > 500) {
            return false;
        }
        try {
            URI uri = new URI(trimmed);
            String scheme = uri.getScheme();
            boolean httpScheme = "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
            return httpScheme && uri.getHost() != null && !uri.getHost().isEmpty();
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    public static List<String> validateEvent(Event e, LocalDateTime now, boolean requireFutureStart) {
        List<String> errors = new ArrayList<>();

        if (isBlank(e.getTitle())) {
            errors.add("Title is required.");
        }
        if (isBlank(e.getDescription())) {
            errors.add("Description is required.");
        }
        if (isBlank(e.getStartTime())) {
            errors.add("Start date and time is missing.");
        }
        if (isBlank(e.getEndTime())) {
            errors.add("End date and time is missing.");
        }

        // Parse and compare date-times safely
        if (!isBlank(e.getStartTime()) && !isBlank(e.getEndTime())) {
            try {
                // Handle standard ISO or ' ' separator formats from datetime-local input fields
                String startStr = e.getStartTime().replace(" ", "T");
                String endStr = e.getEndTime().replace(" ", "T");
                
                // If seconds are missing (e.g. yyyy-MM-ddTHH:mm), append :00
                if (startStr.length() == 16) startStr += ":00";
                if (endStr.length() == 16) endStr += ":00";

                LocalDateTime startDt = LocalDateTime.parse(startStr);
                LocalDateTime endDt = LocalDateTime.parse(endStr);

                if (!startDt.isBefore(endDt)) {
                    errors.add("The start time must be before the end time.");
                }
                if (requireFutureStart && !startDt.isAfter(now)) {
                    errors.add("The start time must be in the future.");
                }
            } catch (DateTimeParseException ex) {
                errors.add("Invalid date and time format.");
            }
        }

        return errors;
    }
}