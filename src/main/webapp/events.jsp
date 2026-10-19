
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.campusconnect.model.Event" %>
<%@ page import="com.campusconnect.model.util.AppConstants" %>
<%@ page import="com.campusconnect.model.util.HtmlUtil" %>

<%!
    private String safe(String value) {
        return HtmlUtil.escape(value == null ? "" : value);
    }
%>

<%
    List<Event> events = (List<Event>) request.getAttribute("events");

    String category = request.getParameter("category");
    String location = request.getParameter("location");
    String search = request.getParameter("search");

    if (category == null) category = "";
    if (location == null) location = "";
    if (search == null) search = "";

    String contextPath = request.getContextPath();
    String success = request.getParameter("success");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Campus Events | CampusConnect</title>

    <!-- Shared CSS connection -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <style>
        * {
            box-sizing: border-box;
        }

        header {
            background: #243b80;
            color: white;
            padding: 18px 7%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            flex-wrap: wrap;
        }

        header a {
            color: white;
            text-decoration: none;
            margin-left: 15px;
        }

        main {
            max-width: 1150px;
            margin: 30px auto;
            padding: 0 18px;
        }

        h1 {
            margin-bottom: 8px;
        }

        .muted {
            color: #666;
            line-height: 1.5;
        }

        .filters {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin: 22px 0;
            display: grid;
            grid-template-columns: 2fr 1fr 1fr auto auto;
            gap: 12px;
            align-items: end;
            box-shadow: 0 2px 10px rgba(0, 0, 0, .05);
        }

        label {
            display: block;
            font-size: 13px;
            font-weight: bold;
            margin-bottom: 7px;
        }

        input, select {
            width: 100%;
            padding: 11px;
            border: 1px solid #ccd1dc;
            border-radius: 6px;
            font: inherit;
            background: white;
        }

        button, .button {
            display: inline-block;
            padding: 11px 15px;
            border: 0;
            border-radius: 6px;
            background: #243b80;
            color: white;
            text-decoration: none;
            font-weight: bold;
            cursor: pointer;
            white-space: nowrap;
        }

        .secondary {
            background: #e8eaf0;
            color: #222;
        }

        .event-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 20px;
        }

        .event-card {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, .06);
            display: flex;
            flex-direction: column;
            min-width: 0;
        }

        .category {
            display: inline-block;
            align-self: flex-start;
            background: #e9edff;
            color: #243b80;
            padding: 5px 9px;
            border-radius: 20px;
            font-size: 12px;
            margin-bottom: 12px;
        }

        .event-card h2 {
            font-size: 19px;
            margin: 0 0 10px;
            overflow-wrap: anywhere;
        }

        .description {
            line-height: 1.5;
            color: #555;
            overflow-wrap: anywhere;
        }

        .event-info {
            font-size: 14px;
            line-height: 1.8;
            margin: 12px 0 18px;
        }

        .event-actions {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
            margin-top: auto;
        }

        .empty {
            background: white;
            border-radius: 10px;
            padding: 35px;
            text-align: center;
            grid-column: 1 / -1;
        }

        .notice {
            color: #176b35;
            background: #e9f8ed;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 18px;
        }

        @media (max-width: 900px) {
            .filters {
                grid-template-columns: 1fr 1fr;
            }

            .event-grid {
                grid-template-columns: repeat(2, minmax(0, 1fr));
            }
        }

        @media (max-width: 600px) {
            .filters, .event-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>

<body>

<header>
    <strong>CampusConnect</strong>
    <nav>
        <a href="${pageContext.request.contextPath}/events">Events</a>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
    </nav>
</header>

<main>
    <h1>Discover Campus Events</h1>

    <p class="muted">
        Find workshops, technical events, sports activities and more.
    </p>

    <form class="filters"
          action="${pageContext.request.contextPath}/events"
          method="get">

        <div>
            <label for="search">Search events</label>
            <input id="search"
                   name="search"
                   type="text"
                   placeholder="Search title or description"
                   value="<%= safe(search) %>">
        </div>

        <div>
            <label for="category">Category</label>
            <select id="category" name="category">
                <option value="">All categories</option>

                <% if (AppConstants.CATEGORIES != null) {
                    for (String item : AppConstants.CATEGORIES) { %>
                        <option value="<%= safe(item) %>"
                            <%= item.equals(category) ? "selected" : "" %>>
                            <%= safe(item) %>
                        </option>
                <%  }
                   } %>
            </select>
        </div>

        <div>
            <label for="location">Location</label>
            <input id="location"
                   name="location"
                   type="text"
                   placeholder="Enter location"
                   value="<%= safe(location) %>">
        </div>

        <button type="submit">Search</button>

        <a class="button secondary"
           href="${pageContext.request.contextPath}/events">Clear</a>
    </form>

    <% if ("created".equals(success)) { %>
        <div class="notice">Event created successfully!</div>
    <% } %>

    <div class="event-grid">

        <% if (events == null || events.isEmpty()) { %>

            <div class="empty">
                <h2>No events found</h2>
                <p class="muted">
                    Try another search term or clear the filters.
                </p>
            </div>

        <% } else {
            for (Event event : events) { %>

                <article class="event-card">

                    <span class="category">
                        <%= safe(event.getCategory() == null
                            || event.getCategory().isEmpty()
                            ? "Campus Event" : event.getCategory()) %>
                    </span>

                    <h2><%= safe(event.getTitle()) %></h2>

                    <p class="description">
                        <%= safe(event.getDescription()) %>
                    </p>

                    <div class="event-info">
                        <div>
                            <strong>Department:</strong>
                            <%= safe(event.getDepartment()) %>
                        </div>

                        <div>
                            <strong>Location:</strong>
                            <%= safe(event.getLocation()) %>
                        </div>

                        <div>
                            <strong>Date and time:</strong>
                            <%= safe(event.getStartTime()) %>
                        </div>
                    </div>

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
</main>

</body>
</html>
