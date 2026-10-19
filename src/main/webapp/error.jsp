```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="com.campusconnect.model.util.HtmlUtil" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error | CampusConnect</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>
    <main class="container">
        <section class="card">
            <h1>Oops! Something went wrong.</h1>

            <p>
                We're sorry, but your request could not be completed.
                Please try again.
            </p>

            <%
                Object status = request.getAttribute(
                    "jakarta.servlet.error.status_code");

                if (status == null) {
                    status = request.getAttribute(
                        "javax.servlet.error.status_code");
                }

                Object message = request.getAttribute(
                    "jakarta.servlet.error.message");

                if (message == null) {
                    message = request.getAttribute(
                        "javax.servlet.error.message");
                }
            %>

            <% if (status != null) { %>
                <p>
                    <strong>Error code:</strong>
                    <%= HtmlUtil.escape(String.valueOf(status)) %>
                </p>
            <% } %>

            <p>
                If the problem continues, please contact the project
                administrator.
            </p>

            <a href="<%= request.getContextPath() %>/events"
               class="btn btn-primary">
                Browse Events
            </a>

            <a href="<%= request.getContextPath() %>/"
               class="btn">
                Go to Home
            </a>
        </section>
    </main>
</body>
</html>
```