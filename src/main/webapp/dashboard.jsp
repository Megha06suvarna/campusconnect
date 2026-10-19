
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.campusconnect.model.User" %>
<%@ page import="com.campusconnect.model.Event" %>
<%@ page import="com.campusconnect.model.util.HtmlUtil" %>

<%!
    private String safe(String value) {
        return HtmlUtil.escape(value == null ? "" : value);
    }
%>

<%
    User currentUser = (User) session.getAttribute("user");

    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Event> registeredEvents =
        (List<Event>) request.getAttribute("registeredEvents");

    List<Event> savedEvents =
        (List<Event>) request.getAttribute("savedEvents");

    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Dashboard | CampusConnect</title>

    <!-- Connect shared CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <style>
        .welcome {
            background: white;
            padding: 24px;
            border-radius: 10px;
            margin-bottom: 25px;
        }

        .section-title {
            margin-top: 32px;
            margin-bottom: 16px;
        }

        .event-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 18px;
        }

        .event-card {
            display: flex;
            flex-direction: column;
            gap: 10px;
            min-width: 0;
        }

        .event-card h3 {
            margin: 0;
            overflow-wrap: anywhere;
        }

        .event-card p {
            margin: 0;
            color: #555;
            overflow-wrap: anywhere;
        }

        .event-actions {
            margin-top: auto;
            padding-top: 12px;
        }

        .notice {
            padding: 12px;
            background: #e9f8ed;
            color: #176b35;
            border-radius: 6px;
            margin-bottom: 16px;
        }

        .empty {
            background: white;
            border-radius: 10px;
            padding: 25px;
            color: #666;
        }

        @media (max-width: 850px) {
            .event-grid {
                grid-template-columns: repeat(2, minmax(0, 1fr));
            }
        }

        @media (max-width: 550px) {
            .event-grid {
                grid-template-columns: 1fr;
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

<main>

    <section class="welcome">
        <h1>Welcome, <%= safe(currentUser.getFullName()) %>!</h1>

        <p class="muted">
            Role: <%= safe(currentUser.getRole()) %>

            <% if (currentUser.getDepartment() != null
                    && !currentUser.getDepartment().trim().isEmpty()) { %>
                | Department: <%= safe(currentUser.getDepartment()) %>
            <% } %>
        </p>

        <% if ("success".equals(request.getParameter("registration"))) { %>
            <div class="notice">
                Your event registration was successful.
            </div>
        <% } %>

        <% if ("success".equals(request.getParameter("bookmark"))) { %>
            <div class="notice">
                Event bookmarked successfully.
            </div>
        <% } %>

        <% if ("ORGANIZER".equalsIgnoreCase(currentUser.getRole())) { %>
            <a class="button"
               href="${pageContext.request.contextPath}/create-event">
                + Create Event
            </a>
        <% } %>

        <a class="button secondary"
           href="${pageContext.request.contextPath}/events">
            Explore Events
        </a>
    </section>

    <% if ("STUDENT".equalsIgnoreCase(currentUser.getRole())) { %>

        <h2 class="section-title">My Registered Events</h2>

        <div class="event-grid">
            <% if (registeredEvents == null || registeredEvents.isEmpty()) { %>
                <div class="empty">
                    You haven't registered for any events yet.
                    <p>
                        <a href="${pageContext.request.contextPath}/events">
                            Browse events
                        </a>
                    </p>
                </div>
            <% } else {
                for (Event event : registeredEvents) { %>

                    <article class="event-card">
                        <span class="category">
                            <%= safe(event.getCategory()) %>
                        </span>

                        <h3><%= safe(event.getTitle()) %></h3>

                        <p>
                            <strong>Date:</strong>
                            <%= safe(event.getStartTime()) %>
                        </p>

                        <p>
                            <strong>Location:</strong>
                            <%= safe(event.getLocation()) %>
                        </p>

                        <div class="event-actions">
                            <a class="button"
                               href="${pageContext.request.contextPath}/event-details?id=<%= event.getEventId() %>">
                                View Details
                            </a>
                        </div>
                    </article>

            <%  }
                } %>
        </div>

        <h2 class="section-title">My Bookmarked Events</h2>

        <div class="event-grid">
            <% if (savedEvents == null || savedEvents.isEmpty()) { %>
                <div class="empty">
                    You haven't bookmarked any events yet.
                    <p>
                        <a href="${pageContext.request.contextPath}/events">
                            Discover events
                        </a>
                    </p>
                </div>
            <% } else {
                for (Event event : savedEvents) { %>

                    <article class="event-card">
                        <span class="category">
                            <%= safe(event.getCategory()) %>
                        </span>

                        <h3><%= safe(event.getTitle()) %></h3>

                        <p>
                            <strong>Date:</strong>
                            <%= safe(event.getStartTime()) %>
                        </p>

                        <p>
                            <strong>Location:</strong>
                            <%= safe(event.getLocation()) %>
                        </p>

                        <div class="event-actions">
                            <a class="button"
                               href="${pageContext.request.contextPath}/event-details?id=<%= event.getEventId() %>">
                                View Details
                            </a>
                        </div>
                    </article>

            <%  }
                } %>
        </div>

    <% } else if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) { %>

        <section class="empty">
            <h2>Administrator Dashboard</h2>
            <p>
                You are signed in as an administrator.
                Event management tools can be connected here.
            </p>
        </section>

    <% } else if ("ORGANIZER".equalsIgnoreCase(currentUser.getRole())) { %>

        <section class="empty">
            <h2>Organizer Dashboard</h2>
            <p>
                Use Create Event to publish a campus event.
                Your event-management list will be connected after
                the organizer features are updated.
            </p>
        </section>

    <% } %>

</main>

</body>
</html>
