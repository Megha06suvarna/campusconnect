
package com.campusconnect.servlet;

import com.campusconnect.dao.EventDAO;
import com.campusconnect.model.Event;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/event-details")
public class EventDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String idParameter = request.getParameter("id");

        int eventId;

        try {
            eventId = Integer.parseInt(idParameter);

            if (eventId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException | NullPointerException e) {
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

            request.setAttribute("event", event);

            // Must match the actual JSP filename exactly.
            request.getRequestDispatcher("/event-detail.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            log("Unable to retrieve event with ID " + eventId, e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load event details right now.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "This endpoint only supports GET requests.");
    }
}
