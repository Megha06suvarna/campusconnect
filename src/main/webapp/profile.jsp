```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="com.campusconnect.model.User" %>
<%@ page import="com.campusconnect.model.util.HtmlUtil" %>

<%
    User user = (User) session.getAttribute("user");

    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String message = (String) request.getAttribute("message");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile | CampusConnect</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>

<header class="navbar">
    <div class="container">
        <a href="<%= request.getContextPath() %>/events" class="logo">
            CampusConnect
        </a>

        <nav>
            <a href="<%= request.getContextPath() %>/events">Events</a>
            <a href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </nav>
    </div>
</header>

<main class="container">
    <section class="card">
        <h1>My Profile</h1>
        <p>View and update your account information.</p>

        <% if (message != null) { %>
            <p class="success-message">
                <%= HtmlUtil.escape(message) %>
            </p>
        <% } %>

        <% if (errorMessage != null) { %>
            <p class="error-message">
                <%= HtmlUtil.escape(errorMessage) %>
            </p>
        <% } %>

        <form action="<%= request.getContextPath() %>/profile"
              method="post">

            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input
                    type="text"
                    id="fullName"
                    name="fullName"
                    value="<%= HtmlUtil.escape(user.getFullName()) %>"
                    maxlength="100"
                    required>
            </div>

            <div class="form-group">
                <label for="email">Email Address</label>
                <input
                    type="email"
                    id="email"
                    value="<%= HtmlUtil.escape(user.getEmail()) %>"
                    readonly>
                <small>Your email address cannot be changed here.</small>
            </div>

            <div class="form-group">
                <label for="department">Department</label>
                <input
                    type="text"
                    id="department"
                    name="department"
                    value="<%= HtmlUtil.escape(user.getDepartment()) %>"
                    maxlength="100"
                    required>
            </div>

            <button type="submit" class="btn btn-primary">
                Save Changes
            </button>

            <a href="<%= request.getContextPath() %>/dashboard"
               class="btn">
                Cancel
            </a>
        </form>
    </section>
</main>

</body>
</html>
```