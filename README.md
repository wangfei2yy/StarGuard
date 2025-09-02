# StarGuard2 - StarRocks Permission Management System

StarGuard2 is a robust permission management system specifically designed for StarRocks databases. Developed with Spring Boot, it provides an intuitive and efficient interface for database administrators to manage StarRocks user permissions, enhancing system security and operational efficiency. The system enables administrators to easily grant, revoke, and monitor permissions across various StarRocks database objects.

## Key Features

### 1. User Management
- Display and manage user lists in the database system
- View detailed permission information for specific users
- Support user filtering by username and host

### 2. Permission Management
- Grant various types of permissions to users (database, table, view levels)
- Revoke existing permissions from users
- Support batch permission operations for multiple users
- Fine-grained permission scope control (single database/all databases, single table/all tables, single view/all views)

### 3. Permission Types Supported
- **Basic Permissions**: SELECT, INSERT, UPDATE, DELETE, ALTER, DROP, CREATE TABLE, etc.
- **View Permissions**: CREATE VIEW, SELECT VIEW, etc.
- **Materialized View Permissions**: CREATE MATERIALIZED VIEW, REFRESH MATERIALIZED VIEW, etc.
- **System Permissions**: CREATE RESOURCE GROUP, CREATE EXTERNAL CATALOG, etc.

### 4. Security Authentication
- User login authentication and session management
- Resource access control
- Comprehensive security logging

## Technology Stack

### Backend Technologies
- **Spring Boot 2.7.18** - Core application framework
- **Spring MVC** - Web request handling
- **Spring Data JPA** - Data access layer
- **Thymeleaf** - Server-side templating engine
- **MySQL/StarRocks** - Database storage

### Frontend Technologies
- **HTML5 + CSS3** - Page structure and styling
- **JavaScript (ES6+)** - Interactive logic implementation
- **jQuery 3.5.1** - DOM manipulation library
- **Bootstrap 4.6.2** - Responsive UI framework
- **Font Awesome** - Icon library

## Project Structure

```
StarGuard2/
├── src/
│   ├── main/
│   │   ├── java/org/authority/StarGuard2/  # Java source code
│   │   │   ├── config/                     # Configuration classes
│   │   │   ├── controller/                 # MVC controllers
│   │   │   ├── dto/                        # Data transfer objects
│   │   │   ├── exception/                  # Exception handling
│   │   │   ├── model/                      # Data models
│   │   │   ├── repository/                 # Data access layer
│   │   │   ├── service/                    # Business logic layer
│   │   │   └── Application.java            # Application entry point
│   │   └── resources/                      # Resource files
│   │       ├── static/                     # Static resources (CSS, JS, images)
│   │       ├── templates/                  # Thymeleaf templates
│   │       └── application.properties      # Application configuration
│   └── test/                               # Test code
├── pom.xml                                 # Maven project configuration
└── target/                                 # Build output directory
```

## Frontend Architecture

The frontend of StarGuard2 is organized in a modular JavaScript structure, interacting with backend APIs through AJAX requests to provide a responsive user interface.

### Frontend Modules

1. **app.js** - Main application module responsible for initializing the application, setting up event listeners, and coordinating module interactions
2. **user-manager.js** - User management module handling user list loading and rendering
3. **permission-manager.js** - Permission management module processing permission granting and revocation logic
4. **ui-utils.js** - UI utility module providing common UI functions like message notifications and modal operations

### Page Structure

The system frontend uses Bootstrap responsive design and includes:
- Login page (login.html)
- Main page (index.html) with:
  - Sidebar navigation menu
  - User list display area
  - Permission grant/revoke forms
  - Permission details modal

## Core API Endpoints

StarGuard2 follows RESTful API design principles, providing the following core API endpoints:

### User Management
- **GET /api/permission/users** - Get all users
- **GET /api/permission/users/{username}/{host}** - Get detailed permissions for a specific user

### Permission Management
- **POST /api/permission/grant** - Grant permissions to a user
- **POST /api/permission/revoke** - Revoke permissions from a user
- **POST /api/permission/batch-grant** - Batch grant permissions to multiple users
- **POST /api/permission/batch-revoke** - Batch revoke permissions from multiple users

## Deployment and Running

### Environment Requirements
- JDK 1.8 or higher
- Maven 3.6+ build tool
- MySQL/StarRocks database

### Build and Run Steps

1. **Configure Database Connection**
   Configure database connection information in the `application.properties` file

2. **Build the Project**
   ```bash
   mvn clean package
   ```

3. **Run the Application**
   ```bash
   java -jar target/StarGuard2-1.0-SNAPSHOT.jar
   ```

4. **Access the System**
   Open a browser and visit `http://localhost:8080/star-guard`

## System Highlights

1. **High Security** - Comprehensive user authentication and permission control mechanisms
2. **Intuitive Operation** - User-friendly interface simplifying permission management operations
3. **Rich Functionality** - Support for multiple permission types and fine-grained scope control
4. **Easy Extension** - Modular code structure facilitating function expansion and maintenance
5. **Responsive Design** - Adaptation to devices with different screen sizes

## Usage Guide

### Login to the System
1. Access the system login page
2. Enter username and password
3. Click the login button to enter the main page

### View User List
- After login, the user list page is displayed by default
- The page shows basic information of all users, including username, host, permission count, etc.

### View User Permission Details
1. In the user list, click the "View Permissions" button for the target user
2. The system pops up a permission details modal, showing all permission information of the user

### Grant Permissions
1. Click the "Grant Permission" option in the sidebar or tab
2. Fill in the form information: username, host, permission type, scope, etc.
3. Click the submit button to complete the authorization operation

### Revoke Permissions
1. Click the "Revoke Permission" option in the sidebar or tab
2. Fill in the form information: username, host, permission type to revoke, etc.
3. Click the submit button to complete the revocation operation

## Summary

StarGuard2 Permission Management System provides database administrators with a comprehensive and user-friendly solution for permission management. Through its intuitive user interface and rich permission control functions, it helps administrators easily manage database permissions, enhancing system security and operational efficiency. The system adopts a modular code structure, featuring good scalability and maintainability, and can be extended and customized according to business needs.