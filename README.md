# URL Shortener Spring Boot api

A web application for shortening long URLs with JWT authentication, link management, and click tracking analytics.

## Features

- **User Authentication** - Registration and login with JWT token-based authentication
- **URL Shortening** - Generate unique 8-character short links from long URLs
- **Click Tracking** - Automatic registration of every click on shortened links with timestamp
- **Automatic Redirects** - Seamless redirection from short links to original URLs
- **User Isolation** - Each user can only view and manage their own shortened links

## Technology Stack

- **Java 17+** - Programming language
- **Spring Boot 3.x** - Web framework
- **Spring Security** - Authentication and authorization
- **JWT (JSON Web Tokens)** - Stateless authentication mechanism
- **JPA/Hibernate** - Object-relational mapping
- **Gradle** - Build and dependency management
- **REST API** - RESTful API design

## API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---|
| POST | `/auth/register` | Register new user account | No |
| POST | `/auth/login` | Login and receive JWT token | No |

### URL Management

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---|
| POST | `/api/urls/shorten` | Create a new shortened URL | Yes |
| GET | `/api/urls/my-urls` | Get all shortened URLs for current user | Yes |
| GET | `/api/urls/{id}` | Get details of a specific shortened URL | Yes |
| DELETE | `/api/urls/{id}` | Delete a shortened URL | Yes |

### Click Tracking & Analytics

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---|
| GET | `/api/analytics/clicks/{shortUrl}?start={date}&end={date}` | Get click statistics for a specific short URL by date range | Yes |
| GET | `/api/analytics/user-clicks?start={date}&end={date}` | Get all user's clicks across all URLs by date range | Yes |

### Redirect (Public)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---|
| GET | `/{shortUrl}` | Redirect to original URL and track the click | No |

## How It Works

### URL Shortening Process
1. **User Registration/Login** - User creates an account or logs in with credentials.
2. **JWT Token Acquisition** - Upon successful login, user receives a JWT token valid for API requests.
3. **Create Short URL** - User sends original URL with Bearer token; system generates unique 8-character code.
4. **Storage** - Mapping stored in database with user association and creation timestamp.
5. **Response** - User receives short URL and metadata.

### Click Tracking Flow
1. **User Access** - Someone clicks or accesses the short URL: `https://example.com/aBcD1234`
2. **Redirect Request** - GET request to `/{shortUrl}` endpoint.
3. **Click Event Logging** - System increments click counter and logs timestamp.
4. **Redirect Execution** - User automatically redirected to original URL (HTTP 302).
5. **Analytics Update** - Click event stored in database for reporting.

## Authentication & Security

### JWT Implementation
- **Token Generation** - Generated upon successful login with username claim.
- **Token Validation** - Every protected endpoint validates JWT signature and expiration.
- **Stateless** - No server-side session storage; tokens are self-contained.
- **Bearer Scheme** - Tokens passed in `Authorization: Bearer <token>` header.
- **Filter Chain** - Custom `JwtAuthenticationFilter` intercepts all requests.

### Protected Resources
All endpoints prefixed with `/api/` require a valid JWT token in the request header:
`Authorization: Bearer eyJhbGciOiJIUzI1NiIsIn...`

## Database Schema

### User Table
- `id` (PK) - Auto-generated user ID
- `username` (UNIQUE) - Username for login
- `email` (UNIQUE) - User email address
- `password` (ENCRYPTED) - BCrypt hashed password
- `created_date` - Account creation timestamp

### UrlMapping Table
- `id` (PK) - Auto-generated mapping ID
- `original_url` (TEXT) - Full original URL
- `short_url` (UNIQUE) - Generated 8-character short code
- `user_id` (FK) - Reference to User who created it
- `created_date` - When short URL was created
- `click_count` - Total number of clicks (counter)

### ClickEvent Table
- `id` (PK) - Auto-generated event ID
- `url_mapping_id` (FK) - Reference to UrlMapping
- `click_date` (TIMESTAMP) - Exact time of click

## Installation & Setup

### Prerequisites
- Java 17 or higher
- Gradle 7.x or higher
- MySQL/PostgreSQL (or H2 for development)

### Steps

1. **Clone Repository**
   ```bash
   git clone [https://github.com/yourusername/url-shortener-sb.git](https://github.com/yourusername/url-shortener-sb.git)
   cd url-shortener-sb
   ```

2. **Configure Database**
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=false
   ```

3. **Build Project**
   ```bash
   gradle clean build
   ```

4. **Run Application**
   ```bash
   gradle bootRun
   ```

5. **Access Application**
   Open `http://localhost:8080`

---

## Example Usage

### 1. Register User
**POST** `http://localhost:8080/auth/register`

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePass123"
}
```

### 2. Login
**POST** `http://localhost:8080/auth/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "securePass123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "john_doe"
}
```

### 3. Create Short URL
**POST** `http://localhost:8080/api/urls/shorten`

**Headers:**
- `Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
- `Content-Type: application/json`

**Request Body:**
```json
{
  "originalUrl": "[https://www.google.com/search?q=spring+boot+tutorial&hl=en](https://www.google.com/search?q=spring+boot+tutorial&hl=en)"
}
```

**Response:**
```json
{
  "id": 1,
  "originalUrl": "[https://www.google.com/search?q=spring+boot+tutorial&hl=en](https://www.google.com/search?q=spring+boot+tutorial&hl=en)",
  "shortUrl": "aBcD1234",
  "createdDate": "2024-01-15T10:30:00",
  "clickCount": 0,
  "username": "john_doe"
}
```

### 4. Access Short URL
**GET** `http://localhost:8080/aBcD1234`

**Result:**
The server responds with an HTTP 302 status code and redirects the user automatically to the original URL. The click is counted in the background.

### 5. Get Analytics
**GET** `http://localhost:8080/api/analytics/clicks/aBcD1234?start=2024-01-01&end=2024-01-31`

**Headers:**
- `Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

**Response:**
```json
[
  {
    "clickDate": "2024-01-15",
    "count": 42
  },
  {
    "clickDate": "2024-01-16",
    "count": 38
  }
]
```

