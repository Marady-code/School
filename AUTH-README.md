# Authentication Guide

## Overview

This School Management System uses JWT (JSON Web Token) for authentication. The system now returns the JWT token in both the response header and body for better security and flexibility.

## Authentication Flow

### 1. Registration

Users can register using the `/auth/register` endpoint with their email, username, and password.

```
POST /api/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "username": "johndoe",
  "password": "SecurePassword123",
  "role": "TEACHER" // Optional, defaults to STUDENT
}
```

### 2. Login

Users can authenticate using the `/auth/login` endpoint with their email and password.

```
POST /api/auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "SecurePassword123"
}
```

### 3. Token Handling

Upon successful authentication, the server returns:

- **JWT Token in Authorization Header**: `Authorization: Bearer [token]`
- **JWT Token in Response Body**: For backward compatibility

### 4. Using the Token

For subsequent requests, include the token in the Authorization header:

```
GET /api/users/profile
Authorization: Bearer [token]
```

### 5. Token Refresh

When the token is about to expire, use the refresh endpoint to get a new token:

```
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "[refresh-token]"
}
```

## Security Notes

1. **Token Storage**: Store the JWT token securely (such as in memory or HttpOnly cookies).
2. **Token Expiration**: Tokens are valid for 30 days by default.
3. **CORS Support**: The API supports cross-origin requests with Authorization headers.

## Testing Authentication

Use the provided script to test the authentication flow:

```powershell
./test-auth-header.ps1
```

## Integration in Client Applications

### Example JavaScript Client

```javascript
// Login
async function login(email, password) {
  const response = await fetch("/api/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });

  // Get token from header (preferred way)
  const token = response.headers.get("Authorization");

  // Store token securely
  sessionStorage.setItem("token", token);

  return await response.json();
}

// Using token for requests
async function fetchProtectedResource(url) {
  const token = sessionStorage.getItem("token");

  const response = await fetch(url, {
    headers: { Authorization: token },
  });

  return await response.json();
}
```
