
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.campusconnect.model.Event" %>
<%@ page import="com.campusconnect.model.User" %>
<%@ page import="com.campusconnect.model.util.HtmlUtil" %>
<%@ page import="jakarta.servlet.http.HttpServletResponse" %>

<%!
    private String safe(String value) {
        return HtmlUtil.escape(value == null ? "" : value);
    }
%>

<%
    User user = (User) session.getAttribute("user");
    Event event = (Event) request.getAttribute("event");

    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    if (!"ORGANIZER".equalsIgnoreCase(user.getRole())) {
        response.sendError(
            HttpServletResponse.SC_FORBIDDEN,
            "Only organizers can edit events."
        );
        return;
    }

    if (event == null) {
        response.sendError(
            HttpServletResponse.SC_NOT_FOUND,
            "Event not found."
        );
        return;
    }

    String errorMessage = (String) request.getAttribute("errorMessage");

    String startValue = "";
    String endValue = "";
    String deadlineValue = "";

    if (event.getStartTime() != null) {
        startValue = event.getStartTime().replace(" ", "T");
        if (startValue.length() > 16) {
            startValue = startValue.substring(0, 16);
        }
    }

    if (event.getEndTime() != null) {
        endValue = event.getEndTime().replace(" ", "T");
        if (endValue.length() > 16) {
            endValue = endValue.substring(0, 16);
        }
    }

    if (event.getRegistrationDeadline() != null) {
        deadlineValue = event.getRegistrationDeadline().replace(" ", "T");
        if (deadlineValue.length() > 16) {
            deadlineValue = deadlineValue.substring(0, 16);
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Event | CampusConnect</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <style>
        .edit-container {
            max-width: 760px;
            margin: 35px auto;
            padding: 28px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 18px rgba(0,0,0,.07);
        }

        .edit-container h1 {
            margin-top: 0;
        }

        .edit-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 16px;
        }

        .edit-field {
            display: flex;
            flex-direction: column;
            gap: 7px;
            margin-bottom: 15px;
            min-width: 0;
        }

        .edit-field.full {
            grid-column: 1 / -1;
        }

        .edit-field label {
            font-weight: bold;
        }

        .edit-field input,
        .edit-field select,
        .edit-field textarea {
            width: 100%;
            padding: 11px;
            border: 1px solid #d7dbe5;
            border-radius: 6px;
            font: inherit;
        }

        .edit-field textarea {
            min-height: 120px;
            resize: vertical;
        }

        .form-actions {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            margin-top: 18px;
        }

        .form-actions button,
        .form-actions a {
            display: inline-block;
            padding: 11px 16px;
            border: 0;
            border-radius: 6px;
            background: #243b80;
            color: white;
            text-decoration: none;
            cursor: pointer;
            font: inherit;
        }

        .form-actions a {
            background: #e8eaf0;
            color: #222;
        }

        .error-message {
            padding: 12px;
            margin-bottom: 18px;
            border-radius: 6px;
            background: #fff0f0;
            color: #9c2020;
        }

        @media (max-width: 600px) {
            .edit-grid {
                grid-template-columns: 1fr;
            }

            .edit-field.full {
                grid-column: auto;
            }

            .edit-container {
                margin: 18px;
                padding: 20px;
            }
        }
    </style>
</head>

<body>

<header>
    <strong>CampusConnect</strong>
    <nav>
        <a href="${pageContext.request.contextPath}/events">Browse Events</a>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </nav>
</header>

<main class="edit-container">
    <h1>Edit Event</h1>
    <p>Update the event details below and save your changes.</p>

    <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <div class="error-message"><%= safe(errorMessage) %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/edit-event"
          method="post">

        <input type="hidden" name="id"
               value="<%= event.getEventId() %>">

        <div class="edit-grid">

            <div class="edit-field full">
                <label for="title">Event Title *</label>
                <input id="title" name="title" type="text"
                       required maxlength="150"
                       value="<%= safe(event.getTitle()) %>">
            </div>

            <div class="edit-field full">
                <label for="description">Description *</label>
                <textarea id="description" name="description"
                          required><%= safe(event.getDescription()) %></textarea>
            </div>

            <div class="edit-field">
                <label for="department">Department *</label>
                <select id="department" name="department" required>
                    <option value="">Select department</option>
                    <option value="CSE" <%= "CSE".equals(event.getDepartment()) ? "selected" : "" %>>CSE</option>
                    <option value="ISE" <%= "ISE".equals(event.getDepartment()) ? "selected" : "" %>>ISE</option>
                    <option value="ECE" <%= "ECE".equals(event.getDepartment()) ? "selected" : "" %>>ECE</option>
                    <option value="EEE" <%= "EEE".equals(event.getDepartment()) ? "selected" : "" %>>EEE</option>
                    <option value="ME" <%= "ME".equals(event.getDepartment()) ? "selected" : "" %>>Mechanical</option>
                    <option value="CIVIL" <%= "CIVIL".equals(event.getDepartment()) ? "selected" : "" %>>Civil</option>
                    <option value="ALL" <%= "ALL".equals(event.getDepartment()) ? "selected" : "" %>>All Departments</option>
                </select>
            </div>

            <div class="edit-field">
                <label for="category">Category *</label>
                <select id="category" name="category" required>
                    <option value="">Select category</option>
                    <option value="Technical" <%= "Technical".equalsIgnoreCase(event.getCategory() == null ? "" : event.getCategory()) ? "selected" : "" %>>Technical</option>
                    <option value="Workshop" <%= "Workshop".equalsIgnoreCase(event.getCategory() == null ? "" : event.getCategory()) ? "selected" : "" %>>Workshop</option>
                    <option value="Cultural" <%= "Cultural".equalsIgnoreCase(event.getCategory() == null ? "" : event.getCategory()) ? "selected" : "" %>>Cultural</option>
                    <option value="Sports" <%= "Sports".equalsIgnoreCase(event.getCategory() == null ? "" : event.getCategory()) ? "selected" : "" %>>Sports</option>
                    <option value="Seminar" <%= "Seminar".equalsIgnoreCase(event.getCategory() == null ? "" : event.getCategory()) ? "selected" : "" %>>Seminar</option>
                    <option value="Other" <%= "Other".equalsIgnoreCase(event.getCategory() == null ? "" : event.getCategory()) ? "selected" : "" %>>Other</option>
                </select>
            </div>

            <div class="edit-field">
                <label for="venue">Venue *</label>
                <input id="venue" name="venue" type="text"
                       required maxlength="200"
                       value="<%= safe(event.getVenue()) %>">
            </div>

            <div class="edit-field">
                <label for="location">Location</label>
                <input id="location" name="location" type="text"
                       value="<%= safe(event.getLocation()) %>">
            </div>

            <div class="edit-field">
                <label for="startTime">Start Date and Time *</label>
                <input id="startTime" name="startTime"
                       type="datetime-local" required
                       value="<%= safe(startValue) %>">
            </div>

            <div class="edit-field">
                <label for="endTime">End Date and Time *</label>
                <input id="endTime" name="endTime"
                       type="datetime-local" required
                       value="<%= safe(endValue) %>">
            </div>

            <div class="edit-field">
                <label for="registrationDeadline">Registration Deadline *</label>
                <input id="registrationDeadline"
                       name="registrationDeadline"
                       type="datetime-local" required
                       value="<%= safe(deadlineValue) %>">
            </div>

            <div class="edit-field">
                <label for="capacity">Capacity (0 = unlimited)</label>
                <input id="capacity" name="capacity" type="number"
                       min="0" required
                       value="<%= event.getCapacity() %>">
            </div>

        </div>

        <div class="form-actions">
            <button type="submit">Update Event</button>

            <a href="${pageContext.request.contextPath}/event-details?id=<%= event.getEventId() %>">
                Cancel
            </a>
        </div>
    </form>
</main>

</body>
</html>
