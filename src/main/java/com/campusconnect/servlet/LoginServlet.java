
package com.campusconnect.servlet;

import com.campusconnect.dao.UserDAO;
import com.campusconnect.model.User;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty()
                || password == null || password.isEmpty()) {

            request.setAttribute(
                    "errorMessage",
                    "Please enter both your email and password.");

            request.getRequestDispatcher("/login.jsp")
                   .forward(request, response);
            return;
        }

        try {
            // UserDAO must verify the password before returning a user.
            User user = userDAO.loginUser(email.trim(), password);

            if (user == null) {
                request.setAttribute(
                        "errorMessage",
                        "Invalid email or password.");

                request.getRequestDispatcher("/login.jsp")
                       .forward(request, response);
                return;
            }

            // Prevent session fixation after successful authentication.
            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60);

            response.sendRedirect(
                    request.getContextPath() + "/dashboard");

        } catch (SQLException e) {
            log("Login database error.", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to log in right now. Please try again later.");
        }
    }
}
