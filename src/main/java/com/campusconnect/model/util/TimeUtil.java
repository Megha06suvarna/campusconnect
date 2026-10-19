package com.campusconnect.model.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Timezone policy for the whole application:
 *
 *   - Every DATETIME value in MySQL is a wall-clock time in India Standard Time (Asia/Kolkata).
 *   - Java code uses LocalDateTime (no zone attached) for those values.
 *   - "Now" is ALWAYS taken from TimeUtil.now(), never from the database NOW() and never
 *     from LocalDateTime.now(), so the app behaves the same whatever timezone the server uses.
 */
public final class TimeUtil {

    public static final ZoneId ZONE = ZoneId.of("Asia/Kolkata");

    private static final Locale EN = Locale.ENGLISH;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", EN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a", EN);
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", EN);
    private static final DateTimeFormatter INPUT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter DATE_INPUT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private TimeUtil() {
    }

    /** Current wall-clock time in the application timezone. */
    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE);
    }

    /** Parses the value of an HTML datetime-local input (yyyy-MM-ddTHH:mm). Returns null if blank or invalid. */
    public static LocalDateTime parseDateTimeLocal(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(text.trim());
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    /** Parses the value of an HTML date input (yyyy-MM-dd). Returns null if blank or invalid. */
    public static LocalDate parseDate(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(text.trim());
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    public static String formatDate(LocalDateTime value) {
        return value == null ? "" : DATE_FMT.format(value);
    }

    public static String formatTime(LocalDateTime value) {
        return value == null ? "" : TIME_FMT.format(value);
    }

    public static String formatDateTime(LocalDateTime value) {
        return value == null ? "" : DATE_TIME_FMT.format(value);
    }

    /** Value to put into an HTML datetime-local input. */
    public static String formatForInput(LocalDateTime value) {
        return value == null ? "" : INPUT_FMT.format(value);
    }

    /** Value to put into an HTML date input. */
    public static String formatDateForInput(LocalDate value) {
        return value == null ? "" : DATE_INPUT_FMT.format(value);
    }
}