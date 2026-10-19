
package com.campusconnect.servlet;

import com.campusconnect.dao.EventDAO;
import com.campusconnect.model.Event;
import com.campusconnect.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest request,
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

        try {
            List<Event> registeredEvents =
                    eventDAO.getRegisteredEventsByUserId(
                            user.getUserId());

            List<Event> savedEvents =
                    eventDAO.getSavedEventsByUserId(
                            user.getUserId());

            request.setAttribute("registeredEvents", registeredEvents);
            request.setAttribute("savedEvents", savedEvents);

            request.getRequestDispatcher("/dashboard.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            log("Error loading dashboard for user "
                    + user.getUserId(), e);

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            request.setAttribute("errorMessage",
                    "Unable to load your dashboard. Please try again.");

            request.getRequestDispatcher("/error.jsp")
                   .forward(request, response);
        }
    }
}
