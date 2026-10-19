
package com.campusconnect.servlet;

import com.campusconnect.dao.EventDAO;
import com.campusconnect.model.Event;
import com.campusconnect.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/edit-event")
public class EditEventServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInOrganizer(request, response);
        if (user == null) {
            return;
        }

        int eventId = readEventId(request, response);
        if (eventId <= 0) {
            return;
        }

        try {
            Event event = eventDAO.getEventById(eventId);

            if (event == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Event not found."
                );
                return;
            }

            if (event.getOrganizerId() != user.getUserId()) {
                response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You can only edit your own events."
                );
                return;
            }

            request.setAttribute("event", event);
            request.getRequestDispatcher("/edit-event.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            log("Unable to load event for editing: " + eventId, e);

            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Unable to load the event for editing."
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        User user = getLoggedInOrganizer(request, response);
        if (user == null) {
            return;
        }

        int eventId = readEventId(request, response);
        if (eventId <= 0) {
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String department = request.getParameter("department");
        String category = request.getParameter("category");
        String venue = request.getParameter("venue");
        String location = request.getParameter("location");
        String startValue = request.getParameter("startTime");
        String endValue = request.getParameter("endTime");
        String deadlineValue =
            request.getParameter("registrationDeadline");
        String capacityValue = request.getParameter("capacity");

        if (isBlank(title)
                || isBlank(description)
                || isBlank(department)
                || isBlank(category)
                || isBlank(venue)
                || isBlank(startValue)
                || isBlank(endValue)
                || isBlank(deadlineValue)) {

            showError(
                request, response, eventId,
                "Please complete all required fields."
            );
            return;
        }

        LocalDateTime start;
        LocalDateTime end;
        LocalDateTime deadline;
        int capacity;

        try {
            start = LocalDateTime.parse(startValue);
            end = LocalDateTime.parse(endValue);
            deadline = LocalDateTime.parse(deadlineValue);

            capacity = isBlank(capacityValue)
                ? 0
                : Integer.parseInt(capacityValue.trim());

            if (capacity < 0) {
                throw new IllegalArgumentException(
                    "Capacity cannot be negative."
                );
            }

        } catch (DateTimeParseException | NumberFormatException e) {
            showError(
                request, response, eventId,
                "Enter valid dates, times and a non-negative capacity."
            );
            return;
        } catch (IllegalArgumentException e) {
            showError(
                request, response, eventId,
                "Capacity cannot be negative."
            );
            return;
        }

        if (!start.isBefore(end)) {
            showError(
                request, response, eventId,
                "The event end time must be after its start time."
            );
            return;
        }

        if (deadline.isAfter(start)) {
            showError(
                request, response, eventId,
                "The registration deadline must not be after the event start time."
            );
            return;
        }

        try {
            Event existing = eventDAO.getEventById(eventId);

            if (existing == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Event not found."
                );
                return;
            }

            if (existing.getOrganizerId() != user.getUserId()) {
                response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You can only edit your own events."
                );
                return;
            }

            Event event = new Event();

            event.setEventId(eventId);
            event.setOrganizerId(user.getUserId());
            event.setTitle(title.trim());
            event.setDescription(description.trim());
            event.setDepartment(department.trim());
            event.setCategory(category.trim());
            event.setVenue(venue.trim());
            event.setLocation(isBlank(location) ? venue.trim() : location.trim());
            event.setStartTime(start.toString());
            event.setEventDate(start.toLocalDate().toString());
            event.setEndTime(end.toString());
            event.setRegistrationDeadline(deadline.toString());
            event.setCapacity(capacity);

            boolean updated = eventDAO.updateEvent(event);

            if (updated) {
                response.sendRedirect(
                    request.getContextPath()
                    + "/event-details?id=" + eventId
                    + "&updated=success"
                );
            } else {
                showError(
                    request, response, eventId,
                    "The event could not be updated. Please try again."
                );
            }

        } catch (SQLException e) {
            log("Unable to update event: " + eventId, e);

            showError(
                request, response, eventId,
                "A database error occurred while updating the event."
            );
        }
    }

    private User getLoggedInOrganizer(HttpServletRequest request,
                                      HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || !(session.getAttribute("user") instanceof User)) {

            response.sendRedirect(
                request.getContextPath() + "/login.jsp"
            );
            return null;
        }

        User user = (User) session.getAttribute("user");

        if (!"ORGANIZER".equalsIgnoreCase(user.getRole())) {
            response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Only organizers can edit events."
            );
            return null;
        }

        return user;
    }

    private int readEventId(HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            if (id > 0) {
                return id;
            }
        } catch (NumberFormatException e) {
            // Send a client error below.
        }

        response.sendError(
            HttpServletResponse.SC_BAD_REQUEST,
            "A valid event ID is required."
        );

        return -1;
    }

    private void showError(HttpServletRequest request,
                           HttpServletResponse response,
                           int eventId,
                           String message)
            throws ServletException, IOException {

        try {
            Event event = eventDAO.getEventById(eventId);
            request.setAttribute("event", event);

        } catch (SQLException e) {
            log("Unable to reload event after edit error.", e);
        }

        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("/edit-event.jsp")
               .forward(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
