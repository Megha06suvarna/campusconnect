
package com.campusconnect.servlet;

import com.campusconnect.dao.EventDAO;
import com.campusconnect.model.Event;
import com.campusconnect.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/create-event")
public class CreateEventServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("user") instanceof User)) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (!"ORGANIZER".equalsIgnoreCase(user.getRole())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only organizers can create events.");
            return;
        }

        request.getRequestDispatcher("/create-event.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("user") instanceof User)) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (!"ORGANIZER".equalsIgnoreCase(user.getRole())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only organizers can create events.");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String department = request.getParameter("department");
        String category = request.getParameter("category");
        String venue = request.getParameter("venue");
        String location = request.getParameter("location");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String registrationDeadline =
                request.getParameter("registrationDeadline");
        String capacityValue = request.getParameter("capacity");

        int capacity;

        try {
            capacity = (capacityValue == null
                    || capacityValue.trim().isEmpty())
                    ? 0 : Integer.parseInt(capacityValue.trim());

            if (capacity < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showError(request, response,
                    "Capacity must be zero or a positive whole number.");
            return;
        }

        LocalDateTime start;
        LocalDateTime end;
        LocalDateTime deadline;

        try {
            start = LocalDateTime.parse(startTime);
            end = LocalDateTime.parse(endTime);
            deadline = LocalDateTime.parse(registrationDeadline);
        } catch (Exception e) {
            showError(request, response,
                    "Enter a valid start time, end time and registration deadline.");
            return;
        }

        if (isBlank(title) || isBlank(description)
                || isBlank(department) || isBlank(category)
                || (isBlank(venue) && isBlank(location))) {
            showError(request, response,
                    "Please complete all required event fields.");
            return;
        }

        if (!start.isAfter(LocalDateTime.now())
                || !start.isBefore(end)) {
            showError(request, response,
                    "The start time must be in the future and before the end time.");
            return;
        }

        if (deadline.isAfter(start)) {
            showError(request, response,
                    "The registration deadline cannot be after the event starts.");
            return;
        }

        Event event = new Event();

        event.setTitle(title.trim());
        event.setDescription(description.trim());
        event.setDepartment(department.trim());
        event.setCategory(category.trim());
        event.setVenue(isBlank(venue) ? location.trim() : venue.trim());
        event.setLocation(isBlank(location) ? venue.trim() : location.trim());
        event.setStartTime(start.toString());
        event.setEventDate(start.toLocalDate().toString());
        event.setEndTime(end.toString());
        event.setRegistrationDeadline(deadline.toString());
        event.setCapacity(capacity);
        event.setOrganizerId(user.getUserId());

        try {
            boolean created = eventDAO.createEvent(event);

            if (created) {
                response.sendRedirect(
                        request.getContextPath() + "/events?success=created");
            } else {
                showError(request, response,
                        "The event could not be created. Check the details and try again.");
            }

        } catch (SQLException e) {
            log("Database error while creating an event.", e);

            showError(request, response,
                    "A database error occurred while creating the event. "
                    + "Check the server console for details.");
        }
    }

    private void showError(
            HttpServletRequest request,
            HttpServletResponse response,
            String message)
            throws ServletException, IOException {

        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("/create-event.jsp")
                .forward(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
