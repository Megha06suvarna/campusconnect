
package com.campusconnect.servlet;

import com.campusconnect.dao.EventDAO;
import com.campusconnect.model.Event;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/events")
public class EventServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String category = request.getParameter("category");
        String location = request.getParameter("location");
        String search = request.getParameter("search");

        try {
            List<Event> events;

            if (hasValue(category)
                    || hasValue(location)
                    || hasValue(search)) {

                events = eventDAO.getFilteredEvents(
                        category, location, search);

            } else {
                events = eventDAO.getAllEvents();
            }

            request.setAttribute("events", events);

            request.getRequestDispatcher("/events.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            log("Error loading campus events.", e);

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            request.setAttribute("errorMessage",
                    "Unable to load events right now. Please try again.");

            request.getRequestDispatcher("/error.jsp")
                   .forward(request, response);
        }
    }

    private boolean hasValue(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
