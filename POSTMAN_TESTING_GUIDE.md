# Postman Testing Guide - Auth & Onboarding Flow

## Prerequisites
1. Start your Spring Boot application (`mvn spring-boot:run` or run from IDE)
2. Ensure PostgreSQL is running and database is accessible
3. Application should be running on `http://localhost:8080`

## Base URL
```
http://localhost:8080
```

---

## 🔐 AUTHENTICATION ENDPOINTS

### 1. User Registration
**POST** `/api/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePassword123!"
}
```

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 1,
      "email": "john.doe@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "verified": false,
      "role": "STUDENT"
    }
  },
  "statusCode": 201,
  "timestamp": "2025-09-05T10:34:35"
}
```

### 2. Email Verification
**POST** `/api/auth/verify-email`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "token": "verification-token-from-email"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Email verified successfully",
  "statusCode": 200,
  "timestamp": "2025-09-05T10:34:35"
}
```

### 3. User Login
**POST** `/api/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePassword123!"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 1,
      "email": "john.doe@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "verified": true,
      "role": "STUDENT"
    }
  },
  "statusCode": 200,
  "timestamp": "2025-09-05T10:34:35"
}
```

### 4. Google OAuth Login
**POST** `/api/auth/oauth2/google`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "idToken": "google-id-token-from-frontend"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Google login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 2,
      "email": "user@gmail.com",
      "firstName": "Google",
      "lastName": "User",
      "verified": true,
      "role": "STUDENT",
      "googleId": "google-user-id"
    }
  },
  "statusCode": 200,
  "timestamp": "2025-09-05T10:34:35"
}
```

### 5. Refresh Token
**POST** `/api/auth/refresh-token`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "refreshToken": "your-refresh-token-here"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "new-access-token",
    "refreshToken": "new-refresh-token",
    "tokenType": "Bearer",
    "expiresIn": 3600
  },
  "statusCode": 200,
  "timestamp": "2025-09-05T10:34:35"
}
```

### 6. Logout
**POST** `/api/auth/logout`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "refreshToken": "your-refresh-token-here"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Logout successful",
  "statusCode": 200,
  "timestamp": "2025-09-05T10:34:35"
}
```

---

## 👤 ONBOARDING ENDPOINTS

### 1. Get Onboarding Data
**GET** `/api/onboarding`

**Headers:**
```
Authorization: Bearer your-access-token-here
Content-Type: application/json
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Onboarding data retrieved successfully",
  "data": {
    "id": 1,
    "collegeName": "ABC University",
    "degree": "Bachelor of Technology",
    "branch": "Computer Science",
    "currentYear": 3,
    "currentCgpa": 8.5,
    "careerGoal": "Software Developer",
    "targetCompanies": "Google, Microsoft, Amazon",
    "preferredRoles": "Full Stack Developer, Backend Developer",
    "targetSalary": 1200000.0,
    "preferredLocation": "Bangalore, Pune, Hyderabad",
    "currentSkills": "Java, Spring Boot, React",
    "skillLevels": "Java: Advanced, Spring Boot: Intermediate, React: Beginner",
    "learningPreferences": "Video tutorials, Hands-on projects",
    "dailyStudyHours": 4,
    "preferredStudyTime": "Evening",
    "weekendAvailability": true,
    "internshipExperience": "2 months at XYZ Company",
    "projectExperience": "E-commerce website, Chat application",
    "certifications": "Oracle Java Certification",
    "isCompleted": true,
    "completedAt": "2025-09-05T10:00:00"
  },
  "statusCode": 200,
  "timestamp": "2025-09-05T10:34:35"
}
```

### 2. Create/Update Onboarding Data
**POST** `/api/onboarding`

**Headers:**
```
Authorization: Bearer your-access-token-here
Content-Type: application/json
```

**Request Body:**
```json
{
  "collegeName": "ABC University",
  "degree": "Bachelor of Technology",
  "branch": "Computer Science and Engineering",
  "currentYear": 3,
  "currentCgpa": 8.5,
  "careerGoal": "Full Stack Software Developer",
  "targetCompanies": "Google, Microsoft, Amazon, Netflix",
  "preferredRoles": "Full Stack Developer, Backend Developer, Software Engineer",
  "targetSalary": 1200000.0,
  "preferredLocation": "Bangalore, Pune, Hyderabad, Mumbai",
  "currentSkills": "Java, Spring Boot, React, JavaScript, MySQL",
  "skillLevels": "Java: Advanced, Spring Boot: Intermediate, React: Beginner, JavaScript: Intermediate, MySQL: Intermediate",
  "learningPreferences": "Video tutorials, Hands-on projects, Documentation reading",
  "dailyStudyHours": 4,
  "preferredStudyTime": "Evening (6 PM - 10 PM)",
  "weekendAvailability": true,
  "internshipExperience": "2 months internship at XYZ Tech Company as Backend Developer Intern",
  "projectExperience": "1. E-commerce website using Spring Boot and React, 2. Real-time chat application using WebSocket, 3. Task management system",
  "certifications": "Oracle Java SE 11 Certification, AWS Cloud Practitioner",
  "isCompleted": true
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Onboarding data saved successfully",
  "data": {
    "id": 1,
    "collegeName": "ABC University",
    "degree": "Bachelor of Technology",
    "branch": "Computer Science and Engineering",
    "currentYear": 3,
    "currentCgpa": 8.5,
    "careerGoal": "Full Stack Software Developer",
    "targetCompanies": "Google, Microsoft, Amazon, Netflix",
    "preferredRoles": "Full Stack Developer, Backend Developer, Software Engineer",
    "targetSalary": 1200000.0,
    "preferredLocation": "Bangalore, Pune, Hyderabad, Mumbai",
    "currentSkills": "Java, Spring Boot, React, JavaScript, MySQL",
    "skillLevels": "Java: Advanced, Spring Boot: Intermediate, React: Beginner, JavaScript: Intermediate, MySQL: Intermediate",
    "learningPreferences": "Video tutorials, Hands-on projects, Documentation reading",
    "dailyStudyHours": 4,
    "preferredStudyTime": "Evening (6 PM - 10 PM)",
    "weekendAvailability": true,
    "internshipExperience": "2 months internship at XYZ Tech Company as Backend Developer Intern",
    "projectExperience": "1. E-commerce website using Spring Boot and React, 2. Real-time chat application using WebSocket, 3. Task management system",
    "certifications": "Oracle Java SE 11 Certification, AWS Cloud Practitioner",
    "isCompleted": true,
    "completedAt": "2025-09-05T10:34:35"
  },
  "statusCode": 200,
  "timestamp": "2025-09-05T10:34:35"
}
```

---

## 🧪 TESTING FLOW SEQUENCE

### Complete Auth & Onboarding Flow:

1. **Register User** → Save `accessToken` from response
2. **Verify Email** (if email verification is implemented)
3. **Login User** → Update `accessToken` if needed
4. **Get Current User Info** → `GET /api/users/me` (with Authorization header)
5. **Create Onboarding Data** → Use saved `accessToken`
6. **Get Onboarding Data** → Verify data was saved correctly

### Postman Environment Variables:
Create these variables in Postman for easier testing:

```
baseUrl: http://localhost:8080
accessToken: (will be set from login response)
refreshToken: (will be set from login response)
userEmail: john.doe@example.com
```

### Authorization Header Format:
For protected endpoints, use:
```
Authorization: Bearer {{accessToken}}
```

---

## 🚨 Common Error Responses

### 400 Bad Request (Validation Error):
```json
{
  "success": false,
  "message": "Validation failed",
  "error": "Invalid input data",
  "data": {
    "email": "Email must be valid",
    "password": "Password must be at least 8 characters"
  },
  "statusCode": 400,
  "path": "/api/auth/register",
  "timestamp": "2025-09-05T10:34:35"
}
```

### 401 Unauthorized:
```json
{
  "success": false,
  "message": "Unauthorized access",
  "error": "Invalid username or password",
  "statusCode": 401,
  "path": "/api/auth/login",
  "timestamp": "2025-09-05T10:34:35"
}
```

### 404 Not Found:
```json
{
  "success": false,
  "message": "Resource not found",
  "error": "User not found with email: nonexistent@example.com",
  "statusCode": 404,
  "path": "/api/users/profile",
  "timestamp": "2025-09-05T10:34:35"
}
```

---

## 📝 Testing Checklist

- [ ] User registration with valid data
- [ ] User registration with invalid data (test validation)
- [ ] User login with correct credentials
- [ ] User login with incorrect credentials
- [ ] Access protected endpoint without token (should get 401)
- [ ] Access protected endpoint with valid token
- [ ] Create onboarding data
- [ ] Retrieve onboarding data
- [ ] Update onboarding data
- [ ] Token refresh functionality
- [ ] User logout

## 🔧 Troubleshooting

1. **500 Internal Server Error**: Check application logs and database connection
2. **404 Not Found**: Verify the endpoint URL and HTTP method
3. **401 Unauthorized**: Check if Authorization header is properly set
4. **400 Bad Request**: Verify request body format and required fields
