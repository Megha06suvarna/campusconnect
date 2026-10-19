
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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserDAO userDAO = new UserDAO();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/register.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword =
                request.getParameter("confirmPassword");
        String department = request.getParameter("department");

        if (isBlank(fullName) || isBlank(email)
                || isBlank(password) || isBlank(confirmPassword)
                || isBlank(department)) {

            showError(request, response,
                    "Please complete all required fields.");
            return;
        }

        fullName = fullName.trim();
        email = email.trim();

        if (fullName.length() > 100) {
            showError(request, response,
                    "Full name must be 100 characters or fewer.");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()
                || email.length() > 150) {
            showError(request, response,
                    "Please enter a valid email address.");
            return;
        }

        if (password.length() < 8 || password.length() > 128) {
            showError(request, response,
                    "Password must contain between 8 and 128 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError(request, response,
                    "Passwords do not match.");
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setRole("STUDENT");
        user.setDepartment(department.trim());

        try {
            boolean registered = userDAO.registerUser(user);

            if (registered) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/login.jsp?registered=success");
            } else {
                showError(request, response,
                        "This email may already be registered. "
                        + "Please try another email.");
            }

        } catch (SQLException e) {
            log("Registration database error.", e);

            showError(request, response,
                    "Unable to create your account right now. "
                    + "Please try again later.");
        }
    }

    private void showError(HttpServletRequest request,
                           HttpServletResponse response,
                           String message)
            throws ServletException, IOException {

        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("/register.jsp")
               .forward(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
