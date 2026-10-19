
package com.campusconnect.servlet;

import com.campusconnect.dao.SavedEventDAO;
import com.campusconnect.model.User;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/save-event")
public class SaveEventServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final SavedEventDAO savedEventDAO = new SavedEventDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null
                || !(session.getAttribute("user") instanceof User)) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only student accounts can bookmark events.");
            return;
        }

        int eventId;

        try {
            eventId = Integer.parseInt(
                    request.getParameter("eventId"));

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
            boolean saved = savedEventDAO.saveEvent(
                    user.getUserId(), eventId);

            if (saved) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/dashboard?bookmark=success");
            } else {
                response.sendRedirect(
                        request.getContextPath()
                        + "/event-details?id=" + eventId
                        + "&bookmark=failed");
            }

        } catch (SQLException e) {
            log("Error bookmarking event " + eventId
                    + " for user " + user.getUserId(), e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to bookmark this event right now.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Use the bookmark form to save an event.");
    }
}
