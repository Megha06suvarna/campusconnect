
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.campusconnect.model.util.HtmlUtil" %>

<%
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | CampusConnect</title>

    <!-- Shared CSS connection -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <style>
        .auth-container {
            max-width: 520px;
            margin: 40px auto;
            padding: 30px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 18px rgba(0, 0, 0, .07);
        }

        .auth-container h1 {
            margin-top: 0;
            text-align: center;
        }

        .auth-description {
            text-align: center;
            color: #666;
            margin-bottom: 25px;
        }

        .auth-field {
            display: flex;
            flex-direction: column;
            gap: 7px;
            margin-bottom: 17px;
        }

        .auth-field label {
            font-weight: bold;
        }

        .auth-field input,
        .auth-field select {
            width: 100%;
            padding: 12px;
            border: 1px solid #d7dbe5;
            border-radius: 6px;
            font: inherit;
            box-sizing: border-box;
        }

        .auth-container button {
            width: 100%;
        }

        .auth-footer {
            text-align: center;
            margin-top: 20px;
        }

        .error-message {
            background: #fff0f0;
            border: 1px solid #e7aaaa;
            color: #9c2020;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 18px;
        }

        @media (max-width: 560px) {
            .auth-container {
                margin: 22px 16px;
                padding: 22px;
            }
        }
    </style>
</head>

<body>

<header>
    <strong>CampusConnect</strong>
    <nav>
        <a href="${pageContext.request.contextPath}/events">Browse Events</a>
        <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
    </nav>
</header>

<main class="auth-container">

    <h1>Create Your Account</h1>

    <p class="auth-description">
        Join CampusConnect to discover and register for campus events.
    </p>

    <% if (errorMessage != null && !errorMessage.isBlank()) { %>
        <div class="error-message">
            <%= HtmlUtil.escape(errorMessage) %>
        </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/register" method="post">

        <div class="auth-field">
            <label for="fullName">Full Name</label>
            <input id="fullName"
                   name="fullName"
                   type="text"
                   maxlength="100"
                   autocomplete="name"
                   required>
        </div>

        <div class="auth-field">
            <label for="email">Email Address</label>
            <input id="email"
                   name="email"
                   type="email"
                   maxlength="150"
                   autocomplete="email"
                   required>
        </div>

        <div class="auth-field">
            <label for="department">Department</label>
            <select id="department" name="department" required>
                <option value="">Select your department</option>
                <option value="CSE">Computer Science (CSE)</option>
                <option value="ISE">Information Science (ISE)</option>
                <option value="ECE">Electronics (ECE)</option>
                <option value="EEE">Electrical (EEE)</option>
                <option value="ME">Mechanical</option>
                <option value="CIVIL">Civil</option>
                <option value="ALL">Other / All Departments</option>
            </select>
        </div>

        <div class="auth-field">
            <label for="password">Password</label>
            <input id="password"
                   name="password"
                   type="password"
                   minlength="8"
                   maxlength="128"
                   autocomplete="new-password"
                   required>
            <small>Use at least 8 characters.</small>
        </div>

        <div class="auth-field">
            <label for="confirmPassword">Confirm Password</label>
            <input id="confirmPassword"
                   name="confirmPassword"
                   type="password"
                   minlength="8"
                   maxlength="128"
                   autocomplete="new-password"
                   required>
        </div>

        <button type="submit">Create Account</button>

    </form>

    <div class="auth-footer">
        Already have an account?
        <a href="${pageContext.request.contextPath}/login.jsp">Login here</a>
    </div>

</main>

</body>
</html>
