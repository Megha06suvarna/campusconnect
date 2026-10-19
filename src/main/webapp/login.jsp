
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.campusconnect.model.util.HtmlUtil" %>

<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    String registered = request.getParameter("registered");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Login | CampusConnect</title>

    <!-- Connect the shared CSS file -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <style>
        .auth-container {
            max-width: 460px;
            margin: 55px auto;
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
            margin-bottom: 18px;
        }

        .auth-field label {
            font-weight: bold;
        }

        .auth-field input {
            padding: 12px;
            border: 1px solid #d7dbe5;
            border-radius: 6px;
            font: inherit;
            box-sizing: border-box;
            width: 100%;
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

        .success-message {
            background: #e9f8ed;
            border: 1px solid #b5dfc0;
            color: #176b35;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 18px;
        }

        @media (max-width: 520px) {
            .auth-container {
                margin: 25px 16px;
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
        <a href="${pageContext.request.contextPath}/register.jsp">Create Account</a>
    </nav>
</header>

<main class="auth-container">

    <h1>Welcome Back!</h1>

    <p class="auth-description">
        Sign in to discover and manage campus events.
    </p>

    <% if ("success".equals(registered)) { %>
        <div class="success-message">
            Registration successful! Please log in.
        </div>
    <% } %>

    <% if (errorMessage != null && !errorMessage.isBlank()) { %>
        <div class="error-message">
            <%= HtmlUtil.escape(errorMessage) %>
        </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/login" method="post">

        <div class="auth-field">
            <label for="email">Email Address</label>

            <input
                id="email"
                name="email"
                type="email"
                maxlength="150"
                autocomplete="email"
                required>
        </div>

        <div class="auth-field">
            <label for="password">Password</label>

            <input
                id="password"
                name="password"
                type="password"
                autocomplete="current-password"
                required>
        </div>

        <button type="submit">Login</button>

    </form>

    <div class="auth-footer">
        Don't have an account?
        <a href="${pageContext.request.contextPath}/register.jsp">
            Register here
        </a>
    </div>

</main>

</body>
</html>
