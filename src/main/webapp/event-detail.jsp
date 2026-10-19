```jsp
<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="com.campusconnect.model.Event" %>
<%@ page import="com.campusconnect.model.User" %>
<%@ page import="com.campusconnect.model.util.HtmlUtil" %>

<%!
    private String safe(String value) {
        return HtmlUtil.escape(value == null ? "" : value);
    }
%>

<%
    Event event = (Event) request.getAttribute("event");
    User user = (User) session.getAttribute("user");

    if (event == null) {
        response.sendError(
            jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND,
            "Event not found."
        );
        return;
    }

    String contextPath = request.getContextPath();

    boolean loggedIn = user != null;

    boolean student = loggedIn
            && "STUDENT".equalsIgnoreCase(user.getRole());

    boolean organizerOwnsEvent = loggedIn
            && "ORGANIZER".equalsIgnoreCase(user.getRole())
            && user.getUserId() == event.getOrganizerId();

    String registrationStatus = request.getParameter("registration");
    String bookmarkStatus = request.getParameter("bookmark");
    String updatedStatus = request.getParameter("updated");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><%= safe(event.getTitle()) %> | CampusConnect</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/css/style.css">

    <style>
        .details-card {
            max-width: 850px;
            margin: 30px auto;
            padding: 28px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 18px rgba(0, 0, 0, 0.07);
        }

        .details-card h1 {
            margin-top: 0;
            overflow-wrap: anywhere;
        }

        .details-info {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 16px;
            margin: 24px 0;
        }

        .info-item {
            padding: 14px;
            background: #f4f6fb;
            border-radius: 8px;
            overflow-wrap: anywhere;
        }

        .info-label {
            display: block;
            color: #666;
            font-size: 13px;
            margin-bottom: 5px;
        }

        .description {
            white-space: pre-wrap;
            overflow-wrap: anywhere;
        }

        .notice {
            padding: 12px;
            border-radius: 6px;
            margin: 16px 0;
        }

        .success {
            background: #e9f8ed;
            color: #176b35;
        }

        .warning {
            background: #fff5e5;
            color: #804900;
        }

        .actions {
            display: flex;
            align-items: center;
            flex-wrap: wrap;
            gap: 12px;
            margin-top: 24px;
        }

        .actions form {
            margin: 0;
        }

        .actions button,
        .actions a {
            display: inline-block;
            padding: 11px 16px;
            border: 0;
            border-radius: 6px;
            font: inherit;
            font-size: 14px;
            text-decoration: none;
            cursor: pointer;
        }

        .actions button {
            background: #243b80;
            color: white;
        }

        .actions a {
            background: #243b80;
            color: white;
        }

        .actions .secondary {
            background: #e8eaf0;
            color: #222;
        }

        .actions .danger-button {
            background: #a52222;
            color: white;
        }

        @media (max-width: 600px) {
            .details-card {
                margin: 18px;
                padding: 20px;
            }

            .details-info {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>

<body>

<header>
    <strong>CampusConnect</strong>

    <nav>
        <a href="<%= contextPath %>/events">Browse Events</a>

        <% if (loggedIn) { %>
            <a href="<%= contextPath %>/dashboard">Dashboard</a>
            <a href="<%= contextPath %>/logout">Logout</a>
        <% } else { %>
            <a href="<%= contextPath %>/login.jsp">Login</a>
            <a href="<%= contextPath %>/register.jsp">Register</a>
        <% } %>
    </nav>
</header>

<main class="details-card">

    <% if ("success".equals(updatedStatus)) { %>
        <div class="notice success">
            Event updated successfully.
        </div>
    <% } %>

    <% if ("success".equals(registrationStatus)) { %>
        <div class="notice success">
            Your event registration was successful.
        </div>
    <% } else if ("failed".equals(registrationStatus)) { %>
        <div class="notice warning">
            Registration could not be completed. You may already be
            registered, or the event may be full or unavailable.
        </div>
    <% } %>

    <% if ("success".equals(bookmarkStatus)) { %>
        <div class="notice success">
            Event saved successfully.
        </div>
    <% } else if ("failed".equals(bookmarkStatus)) { %>
        <div class="notice warning">
            This event could not be saved.
        </div>
    <% } %>

    <span class="category"><%= safe(event.getCategory()) %></span>

    <h1><%= safe(event.getTitle()) %></h1>

    <div class="details-info">

        <div class="info-item">
            <span class="info-label">Department</span>
            <strong><%= safe(event.getDepartment()) %></strong>
        </div>

        <div class="info-item">
            <span class="info-label">Venue</span>
            <strong><%= safe(event.getVenue()) %></strong>
        </div>

        <div class="info-item">
            <span class="info-label">Location</span>
            <strong><%= safe(event.getLocation()) %></strong>
        </div>

        <div class="info-item">
            <span class="info-label">Start Date and Time</span>
            <strong><%= safe(event.getStartTime()) %></strong>
        </div>

        <div class="info-item">
            <span class="info-label">End Date and Time</span>
            <strong><%= safe(event.getEndTime()) %></strong>
        </div>

        <div class="info-item">
            <span class="info-label">Registration Deadline</span>
            <strong><%= safe(event.getRegistrationDeadline()) %></strong>
        </div>

        <div class="info-item">
            <span class="info-label">Capacity</span>
            <strong>
                <%= event.getCapacity() == 0
                    ? "Unlimited"
                    : String.valueOf(event.getCapacity()) %>
            </strong>
        </div>

    </div>

    <h2>About This Event</h2>

    <p class="description"><%= safe(event.getDescription()) %></p>

    <div class="actions">

        <% if (student) { %>

            <form action="<%= contextPath %>/register-event" method="post">
                <input type="hidden" name="eventId"
                       value="<%= event.getEventId() %>">

                <button type="submit">Register for Event</button>
            </form>

            <form action="<%= contextPath %>/save-event" method="post">
                <input type="hidden" name="eventId"
                       value="<%= event.getEventId() %>">

                <button type="submit" class="secondary">Save Event</button>
            </form>

        <% } else if (!loggedIn) { %>

            <a href="<%= contextPath %>/login.jsp">
                Login to Register
            </a>

        <% } %>

        <% if (organizerOwnsEvent) { %>

            <a href="<%= contextPath %>/edit-event?id=<%= event.getEventId() %>">
                Edit Event
            </a>

            <form action="<%= contextPath %>/delete-event"
                  method="post"
                  onsubmit="return confirm('Are you sure you want to delete this event? This action cannot be undone.');">

                <input type="hidden" name="id"
                       value="<%= event.getEventId() %>">

                <button type="submit" class="danger-button">
                    Delete Event
                </button>
            </form>

        <% } %>

        <a href="<%= contextPath %>/events" class="secondary">
            Back to Events
        </a>

    </div>

</main>

</body>
</html>
```