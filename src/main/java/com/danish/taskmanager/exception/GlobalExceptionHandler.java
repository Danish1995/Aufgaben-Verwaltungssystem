package com.danish.taskmanager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException ex) {

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(Map.of(
                        "success", false,
                        "message", ex.getMessage(),
                        "errorCode", ex.getErrorCode()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {

        return ResponseEntity
                .status(500)
                .body(Map.of(
                        "success", false,
                        "message", "Internal Server Error"

                ));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointer(NullPointerException ex) {
        return ResponseEntity.status(400).body(Map.of(
                "success", false,
                "message", "Invalid or missing required field",
                "errorCode", "INVALID_INPUT"
        ));
    }
}