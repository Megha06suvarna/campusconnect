
package com.campusconnect.servlet;

import com.campusconnect.dao.EventDAO;
import com.campusconnect.model.Event;
import com.campusconnect.model.User;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/delete-event")
public class DeleteEventServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Use the delete form to delete an event.");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || !(session.getAttribute("user") instanceof User)) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (!"ORGANIZER".equalsIgnoreCase(user.getRole())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only organizers can delete events.");
            return;
        }

        int eventId;

        try {
            eventId = Integer.parseInt(request.getParameter("id"));

            if (eventId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "A valid event ID is required.");
            return;
        }

        try {
            Event event = eventDAO.getEventById(eventId);

            if (event == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Event not found.");
                return;
            }

            if (event.getOrganizerId() != user.getUserId()) {
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "You can only delete your own events.");
                return;
            }

            // Pass both the event ID and the logged-in organizer ID.
            boolean deleted =
                    eventDAO.deleteEvent(eventId, user.getUserId());

            if (deleted) {
                response.sendRedirect(
                        request.getContextPath()
                                + "/events?success=deleted");
            } else {
                response.sendError(
                        HttpServletResponse.SC_CONFLICT,
                        "The event could not be deleted.");
            }

        } catch (SQLException e) {
            log("Unable to delete event with ID " + eventId, e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "A database error occurred while deleting the event.");
        }
    }
}
