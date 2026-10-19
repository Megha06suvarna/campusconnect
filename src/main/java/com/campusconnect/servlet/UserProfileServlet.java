
package com.campusconnect.servlet;

import com.campusconnect.dao.UserDAO;
import com.campusconnect.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/profile")
public class UserProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserDAO userDAO = new UserDAO();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request, response);
        if (user == null) return;

        request.setAttribute("user", user);
        request.getRequestDispatcher("/profile.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        User sessionUser = getLoggedInUser(request, response);
        if (sessionUser == null) return;

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String department = request.getParameter("department");

        if (isBlank(fullName) || isBlank(email)
                || isBlank(department)) {
            showError(request, response,
                    "Please complete all required fields.");
            return;
        }

        fullName = fullName.trim();
        email = email.trim();
        department = department.trim();

        if (fullName.length() > 100) {
            showError(request, response,
                    "Full name must be 100 characters or fewer.");
            return;
        }

        if (email.length() > 150
                || !EMAIL_PATTERN.matcher(email).matches()) {
            showError(request, response,
                    "Please enter a valid email address.");
            return;
        }

        User updatedUser = new User();
        updatedUser.setUserId(sessionUser.getUserId());
        updatedUser.setFullName(fullName);
        updatedUser.setEmail(email);
        updatedUser.setDepartment(department);

        try {
            boolean updated = userDAO.updateUserProfile(updatedUser);

            if (updated) {
                // Keep the session consistent with the database.
                sessionUser.setFullName(fullName);
                sessionUser.setEmail(email);
                sessionUser.setDepartment(department);

                response.sendRedirect(
                        request.getContextPath()
                        + "/profile?updated=success");
            } else {
                showError(request, response,
                        "Your profile could not be updated. "
                        + "The email may already be in use.");
            }

        } catch (SQLException e) {
            log("Profile update database error.", e);

            showError(request, response,
                    "Unable to update your profile right now.");
        }
    }

    private User getLoggedInUser(HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || !(session.getAttribute("user") instanceof User)) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return null;
        }

        return (User) session.getAttribute("user");
    }

    private void showError(HttpServletRequest request,
                           HttpServletResponse response,
                           String message)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            request.setAttribute("user", session.getAttribute("user"));
        }

        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("/profile.jsp")
               .forward(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
