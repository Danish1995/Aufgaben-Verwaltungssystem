
## Exception Handler Notes
Purpose
- Short guide explaining why to use a domain-specific exception (`AppException`) together with a centralized `@ControllerAdvice` (`GlobalExceptionHandler`), the runtime flow of exception handling in Spring MVC, and simple examples comparing `AppException` vs `RuntimeException`.

## AppException + GlobalExceptionHandler — Summary

**AppException (the thrown object)**
- Purpose: represent a business/domain error (missing resource, validation failure, permission denied).
- Carries domain info: human message, machine `errorCode`, intended HTTP `status`.
- Used at the throw site (service/controller) to declare intent: "this is a handled business error, not a programming bug."

**GlobalExceptionHandler (`@ControllerAdvice`)**
- Purpose: central translator/formatter/policy point for all exceptions.
- Responsibilities:
    - Map exceptions to HTTP responses (status, body shape).
    - Log the event (warn for business errors, error+stack for unexpected ones).
    - Hide or enrich details (avoid leaking internals in production).
    - Provide consistent response schema for the frontend to parse.
    - Capture metrics / report to Sentry / add headers, etc.
- Declared once and applied across controllers (optionally scoped).

### Concrete flow (how they work together)
1. Service: `throw new AppException("User not found", "USER_NOT_FOUND", 404);`
2. DispatcherServlet sees the exception and asks Spring’s resolvers for a handler.
3. `ExceptionHandlerExceptionResolver` finds the `@ExceptionHandler(AppException.class)` method in your `@ControllerAdvice`.
4. That handler builds a `ResponseEntity` (structured body, correct status) and returns it.
5. Client receives consistent JSON and the system has logged/processed the error centrally.

10-step runtime flow (very important)
1. DispatcherServlet receives an HTTP request and finds a matching handler (controller method) via HandlerMapping.
2. HandlerAdapter invokes the controller method.
3. If the controller or service throws an exception, it bubbles back to DispatcherServlet.
4. DispatcherServlet delegates exception resolution to HandlerExceptionResolverComposite (a chain of resolvers).
5. ExceptionHandlerExceptionResolver (one resolver) checks for controller-local `@ExceptionHandler` methods.
6. If not found, it checks `@ControllerAdvice` beans for `@ExceptionHandler` methods (respecting `@Order`).
7. Resolver selects the most specific matching handler method by exception type (exact match → nearest superclass).
8. Spring resolves the handler method arguments (HttpServletRequest, Exception, WebRequest, BindingResult, etc.) and invokes the method.
9. The handler returns a result (ResponseEntity, POJO, ModelAndView); Spring serializes it (JSON via HttpMessageConverters) or renders a view.
10. If no resolver handles the exception, other resolvers (ResponseStatusExceptionResolver, DefaultHandlerExceptionResolver) try; if still unresolved, container-generated 500 is returned.

Code examples

1) AppException thrown in service + handled by GlobalExceptionHandler

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

    public String getErrorCode() { return errorCode; }
    public int getStatusCode() { return statusCode; }
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
    public ResponseEntity<Map<String,Object>> handleApp(AppException ex, HttpServletRequest req) {
        Map<String,Object> body = Map.of(
            "success", false,
            "message", ex.getMessage(),
            "errorCode", ex.getErrorCode(),
            "path", req.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGeneric(Exception ex, HttpServletRequest req) {
        Map<String,Object> body = Map.of(
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
- AppException contains domain metadata (errorCode, status) and yields correct HTTP status and machine-readable error for clients.
- RuntimeException produces a generic 500; frontend cannot distinguish a missing resource from server failures.

Practical best practices (summary)
- Throw `AppException` for expected business-level failures (not for programmer errors).
- Use distinct `errorCode` values for the frontend to react programmatically.
- Keep HTTP status codes accurate (404, 400, 403, 409, etc.).
- Centralize formatting/logging in `@ControllerAdvice` — do not build HTTP responses in services.
- Log unexpected exceptions at ERROR level in the handler; log business exceptions at WARN or INFO as appropriate.
- Optionally, use specialized handlers for validation (`MethodArgumentNotValidException`), access denied, and authentication failures.

Where to put this file
- Stored at `src/docs/ExceptionHanlder-notes.md` in your project for quick reference.

