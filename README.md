# Personal Book Tracker API

## 1. Project Overview

**Application Purpose**: A secure RESTful API for a personal book collection tracker.

**Key Functionalities**:
-   User authentication (Register, Login, Logout).
-   Secure CRUD operations for books.
-   Data isolation (Users can only access their own books).
-   Tagging system and Reading Status tracking.

**Business Logic**: Ensures users interact only with their own data using JWT-based ownership validation.

---

## 2. Technology Stack

-   **Core**: Java 21 & Spring Boot 3.x
-   **Security**: Spring Security & JWT (JSON Web Tokens)
-   **Persistence**: Spring Data JPA with PostgreSQL/MySQL
-   **Encryption**: BCrypt for sensitive data (Passwords)
-   **Build Tool**: Maven

---

## 3. Database Design & Schema

**Schema Link**: [View Database Schema](./schema.sql)

**Design Rationale**:
-   **User-Book Relationship**: One-to-Many (`User` -> `Books`). A user can have many books, but a book belongs to a single user.
-   **Tags**: Many-to-Many (`Books` <-> `Tags`). A book can have multiple tags, and tags can be reused across books.

**Data Integrity**:
-   **Soft Deletes**: `deleted_at` column is used to soft-delete users and books, preserving history while hiding them from the API.
-   **Unique Constraints**: `username` in `users` table and `name` in `tags` table must be unique, `user_id` and `title` in `books` table for no duplication in book name for a single user

---

## 4. Database Migration (Flyway)

The project uses **Flyway** for database schema management and version control.

### How it Works
-   **Automatic Migration**: On application startup, Flyway checks the `src/main/resources/db/migration` folder.
-   **Versioning**: It compares the scripts with the `flyway_schema_history` table in the database.
-   **Execution**: New scripts (e.g., `V2__Add_Column.sql`) are executed automatically in order.

### Migration Scripts
-   **Location**: `src/main/resources/db/migration`
-   **Naming Convention**: `V<Version>__<Description>.sql` (e.g., `V1__Initial_Schema.sql`).
-   **Separator**: Two underscores (`__`) are required between version and description.

### Initial Setup
- In MySQL create the database with name `personal_book_tracker` and then start the application for the first time.
-   The first run will attempt to create the tables defined in [`V1__Initial_Schema.sql`](src/main/resources/db/migration/V1__Initial_Schema.sql).
-   If the database already contains these tables, `baseline-on-migrate=true` ensures Flyway baselines the specific version without failing.

---

## 5. Authentication Flow (JWT)

The system uses a stateless JWT authentication mechanism.

### Auth Flow Diagram

```
+--------+                                  +--------+
| Client |                                  | Server |
+--------+                                  +--------+
    |                                            |
    | 1. Login (Username + Password)             |
    |------------------------------------------->|
    |                                            |
    |        2. Validate Credentials & Generate  |
    |           JWT (with User ID/Role)          |
    |<-------------------------------------------|
    |                                            |
    | 3. Access Protected Resource               |
    |    (Authorization: Bearer <token>)         |
    |------------------------------------------->|
    |                                            |
    |        4. Validate Token Signature &       |
    |           Extract User Identity            |
    |                                            |
    |        5. Return Data for *Only*           |
    |           Attributes User Owns             |
    |<-------------------------------------------|
```

1.  **Registration**:
    -   User submits details.
    -   Password is hashed using **BCrypt** before saving to the database.

2.  **Login**:
    -   User submits credentials (`username`, `password`).
    -   Server validates credentials.
    -   On success, server generates a **signed JWT** containing the user's ID/Username.

3.  **Authorization**:
    -   Client includes the token in the `Authorization` header for all protected endpoints:
        ```
        Authorization: Bearer <your_jwt_token>
        ```

4.  **Security Context**:
    -   A `JwtAuthenticationFilter` intercepts requests.
    -   It validates the token signature and expiration.
    -   It extracts the `User` details and sets the **SecurityContext**, allowing the controller/service to identify the currently authenticated user.

---

## 6. Project Architecture & Folder Structure

The project follows a standard Layered Architecture:

-   **`controller/`**: Handles HTTP requests; utilizes DTOs to avoid exposing Entities directly.
-   **`service/`**: Contains core business logic, transaction management `@Transactional`, and validation.
-   **`DAO/` (DAO)**: Interfaces for Spring Data JPA to interact with the database.
-   **`entity/` (entity)**: JPA Entities representing database tables.
-   **`DTO/`**: Data Transfer Objects for Request/Response payloads.
-   **`config/`**: JWT filters, entry points, and Security Configuration.
-   **`utils/`**: Helper classes for Entity-DTO conversion.
-   **`exception/`**: Global Exception Handler (`@RestControllerAdvice`) for clean, consistent error responses.
-   **`handler/`**: Custom exception handlers.

---

## 7. Setup & Installation Guide

### Prerequisites
1.  **Java Development Kit (JDK) 21**: [Download JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
2.  **MySQL Server**: [Download MySQL](https://dev.mysql.com/downloads/mysql/)
3.  **Maven**:

    #### Windows
    1.  [Download Apache Maven](https://maven.apache.org/download.cgi) binary zip archive.
    2.  Extract it to a directory (e.g., `C:\Program Files\Maven`).
    3.  **Set Environment Variables**:
        -   Search for "Edit the system environment variables".
        -   Click **Environment Variables**.
        -   Under **System Variables**, click **New**. Variable name: `MAVEN_HOME`, Variable value: `C:\Program Files\Maven\apache-maven-x.x.x`.
        -   Find the `Path` variable, click **Edit**, then **New**, and add `%MAVEN_HOME%\bin`.
        -   Verify by opening CMD and running `mvn -version`.

    #### Linux (Ubuntu/Debian)
    ```bash
    sudo apt update
    sudo apt install maven
    mvn -version
    ```

    #### Mac (Homebrew)
    ```bash
    brew install maven
    mvn -version
    ```

### Installation Steps

1.  **Clone the Repository**:
    ```bash
    git clone <repository-url>
    cd tracker-backend
    ```

2.  **Configure Application Properties**:
    -   The project includes a template file `src/main/resources/application.properties.example`.
    -   **Create the actual configuration file** by copying the example:
        ```bash
        # Linux / Mac
        cp src/main/resources/application.properties.example src/main/resources/application.properties

        # Windows (Command Prompt)
        copy src\main\resources\application.properties.example src\main\resources\application.properties
        ```
    -   **Edit** `src/main/resources/application.properties` with your actual credentials:
        ```properties
        # Database Configuration
        spring.datasource.url=jdbc:mysql://localhost:3306/tracker_db
        spring.datasource.username=root
        spring.datasource.password=your_real_password

        # JWT Configuration
        jwt.secret=your_secure_random_secre_key
        ```

3.  **Build the Project**:
    ```bash
    mvn clean install
    ```

4.  **Run the Application**:
    ```bash
    mvn spring-boot:run
    ```
    The server will start on `http://localhost:8080`.

### Importing Postman Collection
1.  Open Postman.
2.  Click **Import** -> **File**.
3.  Select `api-collection.json` from the project root.
4.  The collection "Book Tracker API" will appear.

---

## 8. API Reference & Testing

### Swagger Documentation
The API documentation is available via Swagger UI when the application is running:
-   **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
-   **OpenAPI Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### API Endpoints

| Method | Endpoint | Auth Required | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | No | Register a new user |
| `POST` | `/api/auth/login` | No | Login and receive JWT |
| `GET` | `/api/books/` | **Yes** | Get all books for current user |
| `POST` | `/api/books/create` | **Yes** | Add a new book |
| `GET` | `/api/books/{id}` | **Yes** | Get book details |
| `PATCH` | `/api/books/update/{id}` | **Yes** | Update book details |
| `DELETE` | `/api/books/delete/{id}` | **Yes** | Soft delete a book |

---

## 9. Quality Standards

-   **Global Error Handling**: Responses follow a consistent JSON structure:
    ```json
    {
      "timestamp": "2024-02-17T12:00:00",
      "status": 404,
      "error": "Not Found",
      "message": "Book not found with id: 1",
      "path": "/api/books/1"
    }
    ```
-   **Pagination & Search**: List endpoints support `page` and `size` parameters for efficient data loading.
-   **Unit Tests**: Service layer logic logic is tested to ensure correctness of business rules (e.g., status history recording).
