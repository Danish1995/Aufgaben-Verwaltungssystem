# Exception Flow Summary

## Why AppException?

Java already provides:

```java
NullPointerException
RuntimeException
IllegalArgumentException
```

 `AppException` is created because custom fields are needed:

```java
message
errorCode
statusCode
```

Example:

```java
throw new AppException(
    "User not found",
    "USER_NOT_FOUND",
    404
);
```

---

## Exception Flow

```text
Controller
    ↓
Service
    ↓
Repository
```

If something goes wrong:

```java
throw new AppException(...);
```

Spring automatically:

```text
Exception Thrown
       ↓
Looks for matching @ExceptionHandler(AppException.class)
whatever method is annotated with that, it will call it, for example: if we call .orElseThrow(() -> new NullPointerException(...) 
then it will look for @ExceptionHandler(NullPointerException.class) and call that method, if we call 
.orElseThrow(() -> new RuntimeException(...) then it will look for @ExceptionHandler(RuntimeException.class) and call that method,
and if it doesn't find it, it will look for @ExceptionHandler(Exception.class) and call that method, if it doesn't find that either, 
it will return a default 500 error response.
       ↓
Calls that method
       ↓
Returns ResponseEntity
```

Example:

```java
@ExceptionHandler(AppException.class)
```

handles:

```java
throw new AppException(...)
```

---

## GlobalExceptionHandler

```java
@ExceptionHandler(AppException.class)
```

Handles all AppExceptions.

```java
@ExceptionHandler(NullPointerException.class)
```

Handles all NullPointerExceptions.

```java
@ExceptionHandler(Exception.class)
```

Fallback for everything else.

---

// AppException (already in project)

```java
public class AppException extends RuntimeException {
    private String errorCode;
    private int statusCode;

    public AppException(String message, String errorCode, int statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
```

// Service throwing AppException

```java
public User findUser(Long id) {
    return userRepository.findById(id)
            .orElseThrow(() -> new AppException("User not found", "USER_NOT_FOUND", 404));
}
```

// ControllerAdvice (GlobalExceptionHandler)

```java

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleApp(AppException ex, HttpServletRequest req) {
        Map<String, Object> body = Map.of(
                "success", false,
                "message", ex.getMessage(),
                "errorCode", ex.getErrorCode(),
                "path", req.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex, HttpServletRequest req) {
        Map<String, Object> body = Map.of(
                "success", false,
                "message", "Internal Server Error",
                "path", req.getRequestURI()
        );
        return ResponseEntity.status(500).body(body);
    }
}
```

Result (client receives):
HTTP 404

```json
{
  "success": false,
  "message": "User not found",
  "errorCode": "USER_NOT_FOUND",
  "path": "/api/v1/users/123"
}
```

2) RuntimeException thrown (no domain info) — handled by generic handler

// Service throwing runtime exception

```java
public Task getTask(Long id) {
    return taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
}
```

// GlobalExceptionHandler's generic handler (same as above) will catch and return 500

Result (client receives):
HTTP 500

```json
{
  "success": false,
  "message": "Internal Server Error",
  "path": "/api/v1/tasks/99"
}
```

Key differences demonstrated

- AppException contains domain metadata (errorCode, status) and yields correct HTTP status and machine-readable error
  for clients.
- RuntimeException produces a generic 500; frontend cannot distinguish a missing resource from server failures.

Set correct HTTP status codes

- 400 = Bad request (validation, client error)
- 401 = Unauthorized (not logged in)
- 403 = Forbidden (no permission)
- 404 = Not found (resource doesn't exist)
- 409 = Conflict (e.g., duplicate email on registration)
- 500 = Server error (unexpected)

