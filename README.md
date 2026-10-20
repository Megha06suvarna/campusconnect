# CampusConnect – Campus Event Management System

CampusConnect is a Java-based web application designed to help students discover campus events, register for activities, save interesting events, and manage their profiles through a centralized platform.

The application provides an organized way to access campus event information and simplifies event participation for students.

## Features

- User Registration and Login– Create an account and securely access the application.
- Event Discovery – Browse available campus events.
- Event Details – View detailed information about individual events.
- Event Registration – Register for events through the application.
- Save Events – Save interesting events for later reference.
- Dashboard– Access relevant event information and user activities.
- Event Management– Create, edit, and delete events according to application permissions.
- User Profile– View and manage profile information.
- Input Validation – Validate user input to help prevent invalid data.
- Database Integration – Store and retrieve application data using MySQL.

## Technology Stack

| Component | Technology |
| Programming Language | Java |
| Frontend | JSP, HTML, CSS |
| Backend | Java Servlets |
| Database | MySQL |
| Web Server | Apache Tomcat |
| Database Connectivity | JDBC |
| Development Environment | Eclipse IDE |
| Version Control | Git and GitHub |

## Project Structure


CampusConnectApp/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/campusconnect/
│       │       ├── dao/
│       │       ├── model/
│       │       ├── servlet/
│       │       └── util/
│       └── webapp/
│           ├── WEB-INF/
│           ├── css/
│           ├── login.jsp
│           ├── register.jsp
│           ├── dashboard.jsp
│           ├── events.jsp
│           ├── event-detail.jsp
│           ├── create-event.jsp
│           ├── edit-event.jsp
│           └── profile.jsp
├── .gitignore
└── README.md


## Prerequisites

Install the following software before running the project:

- Java Development Kit (JDK)
- Eclipse IDE with Enterprise Java and Web Development support
- Apache Tomcat compatible with the project's Servlet API
- MySQL Server
- MySQL Workbench (optional)

## Installation and Setup

### 1. Clone the Repository

Open a terminal and run:

git clone https://github.com/Megha06suvarna/campusconnect.git
cd campusconnect

### 2. Import the Project into Eclipse

1. Open Eclipse.
2. Select **File → Import**.
3. Choose **Existing Projects into Workspace** or the appropriate Maven project import option, depending on the project configuration.
4. Select the cloned project directory.
5. Finish the import.

### 3. Configure MySQL

1. Start your MySQL server.
2. Create the application database.
3. Execute the SQL schema required by the application.
4. Configure the database connection using your project's database configuration.

Security note:Keep database usernames, passwords, and other secrets outside version control. Never commit real credentials to GitHub.

The exact database name, schema, and configuration method should match the application's existing code.

### 4. Configure Apache Tomcat

1. Add your compatible Tomcat server in Eclipse.
2. Associate the project with the server.
3. Start the server using **Run As → Run on Server**.
4. Open the application URL shown by Eclipse.

### 5. Access the Application

Open the local URL provided by Tomcat. It will commonly resemble:

http://localhost:8080/campusconnect/

The actual context path may differ depending on your Eclipse and Tomcat configuration.

## Application Workflow

1. A user creates an account or logs in.
2. The application validates the submitted information.
3. Java Servlets process requests and coordinate application logic.
4. DAO classes interact with MySQL through JDBC.
5. JSP pages display the resulting information to the user.
6. Users can browse events, register, save events, and access their profiles.

## Database

MySQL is used to persist application information, such as user accounts, events, registrations, and saved events.

The database schema and table definitions must be configured before database-dependent features can work correctly.

## Security Considerations

- Store passwords using an appropriate secure password-hashing method.
- Use prepared statements for database operations.
- Validate and sanitize user input.
- Protect authenticated routes and user-specific actions.
- Keep credentials and secret configuration out of Git.
- Use HTTPS and secure configuration when deploying publicly.

## Deployment

The application can be packaged as a WAR file and deployed to a hosting environment that supports Java Servlets and JSP through a compatible Tomcat server.

Deployment requires:

- A compatible Java runtime and servlet container.
- A reachable hosted MySQL database.
- Secure environment-specific database configuration.
- Correct application context and server settings.

GitHub stores the source code; it does not execute JSP or Servlet applications by itself.

## Future Enhancements

- Event search and filtering.
- Event reminders and notifications.
- Improved mobile-responsive interface.
- Administrative analytics and reporting.
- Email notifications for event registrations.

## Author

Meghashree

GitHub: https://github.com/Megha06suvarna

## License

No license has been specified yet. Add a license if you intend to permit others to reuse, modify, or distribute this project.
