
package com.campusconnect.servlet;

import com.campusconnect.dao.RegistrationDAO;
import com.campusconnect.model.User;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register-event")
public class RegisterEventServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final RegistrationDAO registrationDAO = new RegistrationDAO();

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
                    "Only student accounts can register for events.");
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
            boolean registered = registrationDAO.registerUserForEvent(
                    user.getUserId(), eventId);

            if (registered) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/dashboard?registration=success");
            } else {
                response.sendRedirect(
                        request.getContextPath()
                        + "/event-details?id=" + eventId
                        + "&registration=failed");
            }

        } catch (SQLException e) {
            log("Error registering user " + user.getUserId()
                    + " for event " + eventId, e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to register for this event right now.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Use the event registration form to register.");
    }
}
