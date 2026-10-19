```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpServletResponse" %>
<%@ page import="com.campusconnect.model.User" %>
<%@ page import="com.campusconnect.model.util.AppConstants" %>
<%@ page import="com.campusconnect.model.util.HtmlUtil" %>

<%
    User currentUser = (User) session.getAttribute("user");

    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    if (!"ORGANIZER".equalsIgnoreCase(currentUser.getRole())) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN,
                "Only organizers can create events.");
        return;
    }

    String errorMessage = (String) request.getAttribute("errorMessage");

    String title = request.getParameter("title");
    String description = request.getParameter("description");
    String department = request.getParameter("department");
    String category = request.getParameter("category");
    String venue = request.getParameter("venue");
    String location = request.getParameter("location");
    String startTime = request.getParameter("startTime");
    String endTime = request.getParameter("endTime");
    String registrationDeadline =
            request.getParameter("registrationDeadline");
    String capacity = request.getParameter("capacity");

    if (title == null) title = "";
    if (description == null) description = "";
    if (department == null) department = "";
    if (category == null) category = "";
    if (venue == null) venue = "";
    if (location == null) location = "";
    if (startTime == null) startTime = "";
    if (endTime == null) endTime = "";
    if (registrationDeadline == null) registrationDeadline = "";
    if (capacity == null) capacity = "";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Event | CampusConnect</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f6fb;
            color: #222;
        }

        header {
            background: #243b80;
            color: white;
            padding: 18px 7%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            flex-wrap: wrap;
        }

        header a {
            color: white;
            text-decoration: none;
            margin-left: 16px;
        }

        main {
            max-width: 850px;
            margin: 35px auto;
            padding: 0 18px;
        }

        .panel {
            background: white;
            padding: 28px;
            border-radius: 12px;
            box-shadow: 0 3px 14px rgba(0, 0, 0, .08);
        }

        h1 {
            margin-top: 0;
        }

        .intro {
            color: #555;
            line-height: 1.5;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 18px;
        }

        .field {
            display: flex;
            flex-direction: column;
            gap: 7px;
        }

        .full {
            grid-column: 1 / -1;
        }

        label {
            font-weight: bold;
            font-size: 14px;
        }

        input, select, textarea {
            width: 100%;
            padding: 11px;
            border: 1px solid #cbd0dc;
            border-radius: 6px;
            font: inherit;
            background: white;
        }

        textarea {
            min-height: 110px;
            resize: vertical;
        }

        .hint {
            color: #666;
            font-size: 12px;
            line-height: 1.4;
        }

        .error {
            background: #fff0f0;
            border: 1px solid #e7aaaa;
            color: #9c2020;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 18px;
        }

        .actions {
            display: flex;
            gap: 12px;
            margin-top: 24px;
            flex-wrap: wrap;
        }

        button, .cancel {
            padding: 12px 18px;
            border-radius: 6px;
            border: 0;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            font-size: 14px;
        }

        button {
            background: #243b80;
            color: white;
        }

        .cancel {
            background: #e8eaf0;
            color: #222;
        }

        @media (max-width: 600px) {
            .grid {
                grid-template-columns: 1fr;
            }

            .full {
                grid-column: auto;
            }

            .panel {
                padding: 20px;
            }

            header a {
                margin-left: 0;
                margin-right: 12px;
            }
        }
    </style>
</head>

<body>
<header>
    <strong>CampusConnect</strong>
    <nav>
        <a href="<%= request.getContextPath() %>/events">Browse Events</a>
        <a href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </nav>
</header>

<main>
    <section class="panel">
        <h1>Create a Campus Event</h1>

        <p class="intro">
            Enter the event information below. Check the date and time
            carefully before publishing.
        </p>

        <% if (errorMessage != null) { %>
            <div class="error">
                <%= HtmlUtil.escape(errorMessage) %>
            </div>
        <% } %>

        <form action="<%= request.getContextPath() %>/create-event"
              method="post">

            <div class="grid">
                <div class="field full">
                    <label for="title">Event title *</label>
                    <input id="title" name="title" type="text"
                           maxlength="200" required
                           value="<%= HtmlUtil.escape(title) %>">
                </div>

                <div class="field full">
                    <label for="description">Description *</label>
                    <textarea id="description" name="description"
                              maxlength="5000" required><%= HtmlUtil.escape(description) %></textarea>
                </div>

                <div class="field">
                    <label for="department">Department *</label>
                    <select id="department" name="department" required>
                        <option value="">Choose department</option>
                        <% for (String item : AppConstants.DEPARTMENTS) { %>
                            <option value="<%= HtmlUtil.escape(item) %>"
                                <%= item.equals(department) ? "selected" : "" %>>
                                <%= HtmlUtil.escape(item) %>
                            </option>
                        <% } %>
                    </select>
                </div>

                <div class="field">
                    <label for="category">Category *</label>
                    <select id="category" name="category" required>
                        <option value="">Choose category</option>
                        <% for (String item : AppConstants.CATEGORIES) { %>
                            <option value="<%= HtmlUtil.escape(item) %>"
                                <%= item.equals(category) ? "selected" : "" %>>
                                <%= HtmlUtil.escape(item) %>
                            </option>
                        <% } %>
                    </select>
                </div>

                <div class="field">
                    <label for="venue">Venue *</label>
                    <input id="venue" name="venue" type="text"
                           maxlength="255" required
                           value="<%= HtmlUtil.escape(venue) %>"
                           placeholder="e.g. Seminar Hall">
                </div>

                <div class="field">
                    <label for="location">Location</label>
                    <input id="location" name="location" type="text"
                           maxlength="255"
                           value="<%= HtmlUtil.escape(location) %>"
                           placeholder="e.g. Main Block">
                </div>

                <div class="field">
                    <label for="startTime">Start date and time *</label>
                    <input id="startTime" name="startTime"
                           type="datetime-local" required
                           value="<%= HtmlUtil.escape(startTime) %>">
                </div>

                <div class="field">
                    <label for="endTime">End date and time *</label>
                    <input id="endTime" name="endTime"
                           type="datetime-local" required
                           value="<%= HtmlUtil.escape(endTime) %>">
                </div>

                <div class="field">
                    <label for="registrationDeadline">
                        Registration deadline *
                    </label>
                    <input id="registrationDeadline"
                           name="registrationDeadline"
                           type="datetime-local" required
                           value="<%= HtmlUtil.escape(registrationDeadline) %>">
                    <span class="hint">
                        Must be before or at the event start time.
                    </span>
                </div>

                <div class="field">
                    <label for="capacity">Capacity</label>
                    <input id="capacity" name="capacity"
                           type="number" min="0"
                           value="<%= HtmlUtil.escape(capacity) %>"
                           placeholder="0 means unlimited">
                    <span class="hint">
                        Enter 0 or leave blank for unlimited capacity.
                    </span>
                </div>
            </div>

            <div class="actions">
                <button type="submit">Publish Event</button>
                <a class="cancel"
                   href="<%= request.getContextPath() %>/dashboard">
                    Cancel
                </a>
            </div>
        </form>
    </section>
</main>
</body>
</html>
