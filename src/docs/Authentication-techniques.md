# Authentication Mechanisms: JWT vs Session-Based Authentication and Other Common Approaches

## Table of Contents

1. Introduction
2. Session-Based Authentication
3. JWT (JSON Web Token) Authentication
4. Session vs JWT Comparison
5. OAuth 2.0
6. OpenID Connect (OIDC)
7. Single Sign-On (SSO)
8. Multi-Factor Authentication (MFA)
9. API Keys
10. Authentication Mechanisms For Different Purpose?
11. Summary
---

# 1. Introduction

Authentication is the process of verifying a user's identity before granting access to an application or system.

Modern applications commonly use:

* Session-Based Authentication
* JWT Authentication
* OAuth 2.0
* OpenID Connect (OIDC)
* Single Sign-On (SSO)
* Multi-Factor Authentication (MFA)
* API Keys (for service-to-service communication)

---

# 2. Session-Based Authentication

## How It Works

### Step 1: User Logs In

```text
POST /login
username=danish
password=secret
```

### Step 2: Server Validates Credentials

The server checks the username and password against the database.

### Step 3: Session Is Created

```text
Session ID: ABC123XYZ
```

The server stores session information:

```java
Session {
    id: "ABC123XYZ",
    userId: 1,
    role: "ADMIN"
}
```

### Step 4: Session ID Sent to Browser

```http
Set-Cookie: JSESSIONID=ABC123XYZ
```

### Step 5: Browser Sends Cookie

```http
Cookie: JSESSIONID=ABC123XYZ
```

### Step 6: Server Looks Up Session

The server retrieves user information using the session ID.

---

## Advantages

* Easy logout
* Easy session invalidation
* Good security when configured properly
* Default approach in Spring Security

## Disadvantages

* Server must store sessions
* Difficult to scale across multiple servers
* Requires shared session storage in distributed systems

---

# 3. JWT (JSON Web Token) Authentication

JWT is a stateless authentication mechanism.

## How It Works

### Step 1: User Logs In

```text
POST /login
```

### Step 2: Server Generates JWT

Example payload:

```json
{
  "sub": "danish",
  "role": "ADMIN",
  "exp": 1767225600
}
```

JWT structure:

```text
Header.Payload.Signature
```

Example:

```text
eyJhbGciOiJIUzI1NiJ9...
```

### Step 3: Token Sent To Client

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Step 4: Client Stores Token

Usually:

* Local Storage
* Session Storage
* Secure HttpOnly Cookie

### Step 5: Client Sends Token

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Step 6: Server Verifies Signature

No database lookup is required for authentication itself.

---

## Advantages

* Stateless
* Easy scaling
* Good for APIs and microservices
* Mobile-app friendly

## Disadvantages

* Logout is harder
* Token revocation is difficult
* Larger request size
* Security issues if stored improperly

---

# 4. Session vs JWT Comparison

| Feature                 | Session-Based | JWT            |
| ----------------------- | ------------- | -------------- |
| Server State            | Stateful      | Stateless      |
| Storage                 | Server        | Client         |
| Scalability             | Moderate      | Excellent      |
| Logout                  | Easy          | More Difficult |
| Revocation              | Easy          | Difficult      |
| API Support             | Good          | Excellent      |
| Microservices           | Less Suitable | Very Suitable  |
| Default Spring Security | Yes           | No             |

---

# 5. OAuth 2.0

OAuth 2.0 is an authorization framework.

It allows users to log in using third-party providers:

* Google
* GitHub
* Facebook
* Microsoft
* LinkedIn

Example:

```text
Login with Google
```

Flow:

```text
User
  ↓
Google Login
  ↓
Google verifies identity
  ↓
Google returns access token
  ↓
Application receives token
```

OAuth answers:

> What can this application access on behalf of the user?

---

# 6. OpenID Connect (OIDC)

OpenID Connect is built on top of OAuth 2.0.

OAuth provides authorization.

OIDC provides authentication.

Example:

```text
Sign in with Google
```

The application receives:

```json
{
  "name": "Danish",
  "email": "danish@gmail.com"
}
```

Common providers:

* Google
* Microsoft
* GitHub
* Okta
* Auth0
* Keycloak

---

# 7. Single Sign-On (SSO)

SSO allows users to log in once and access multiple applications.

Example:

```text
Company Login
     ↓
Email
Calendar
Jira
Confluence
HR System
```

Popular enterprise solutions:

* Keycloak
* Okta
* Microsoft Entra ID (Azure AD)
* Ping Identity

Benefits:

* Better user experience
* Centralized authentication
* Easier access management

---

# 8. Multi-Factor Authentication (MFA)

MFA requires more than one authentication factor.

Example:

### Factor 1

```text
Password
```

### Factor 2

```text
OTP from phone
```

Or:

```text
Authenticator App
```

Or:

```text
Hardware Security Key
```

Examples:

* Google Authenticator
* Microsoft Authenticator
* YubiKey

Benefits:

* Strong protection against password theft
* Common in enterprise systems

---

# 9. API Keys

Used primarily for machine-to-machine communication.

Example:

```http
X-API-KEY: abc123xyz
```

Common use cases:

* External APIs
* Internal services
* CI/CD pipelines

Not recommended for user authentication.

---

# 10. Authentication Mechanisms For Different Purpose?

## Traditional Enterprise Applications

Commonly use:

* Session-Based Authentication
* SSO
* MFA

Examples:

* Banking systems
* Internal company portals

---

## Modern Web Applications

Commonly use:

* JWT
* OAuth 2.0
* OpenID Connect

Examples:

* SaaS products
* Cloud applications

---

## Microservices Architectures

Commonly use:

* JWT
* OAuth 2.0
* OIDC

Reason:

* Stateless
* Easy scaling
* Service-to-service communication

---

## Large Enterprises

Often combine:

```text
SSO
 + OAuth 2.0
 + OpenID Connect
 + MFA
```

Example:

```text
Employee
   ↓
Microsoft Entra ID
   ↓
MFA
   ↓
JWT/OIDC Token
   ↓
Applications
```

---

# 11. Summary

## Session-Based Authentication

Best for:

* Monolithic applications
* Server-rendered applications
* Spring MVC + Thymeleaf projects

---

## JWT Authentication

Best for:

* REST APIs
* Mobile applications
* Microservices

---

## OAuth 2.0 + OpenID Connect

Best for:

* Social login
* Enterprise authentication
* Modern cloud applications

---

## MFA

Should be added whenever security is important.

---

## Real-World Enterprise Stack

A common modern setup is:

```text
Frontend
    ↓
OAuth 2.0 / OpenID Connect
    ↓
Identity Provider (Keycloak, Okta, Azure AD)
    ↓
JWT Access Token
    ↓
Backend APIs
```

This architecture is widely used by modern enterprises and cloud-native applications.
