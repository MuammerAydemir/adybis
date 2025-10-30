package com.muammer.adybis.base.exceptions;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
        private ErrorResponse buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request,
                        List<ValidationError> validationErrors) {
                return ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(status.value())
                                .error(status.getReasonPhrase())
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .validationErrors(validationErrors)
                                .build();
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex, HttpServletRequest req) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(buildErrorResponse(ex, HttpStatus.NOT_FOUND, req, null));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                List<ValidationError> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                                .map(err -> new ValidationError(err.getField(), err.getDefaultMessage()))
                                .collect(Collectors.toList());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request, validationErrors));
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraintViolation(
                        ConstraintViolationException ex,
                        HttpServletRequest request) {

                List<ValidationError> validationErrors = ex.getConstraintViolations().stream()
                                .map(violation -> new ValidationError(
                                                violation.getPropertyPath().toString(),
                                                violation.getMessage()))
                                .collect(Collectors.toList());

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Validation Error")
                                .message("One or more constraints violated while persisting entity")
                                .path(request.getRequestURI())
                                .validationErrors(validationErrors)
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
                        HttpServletRequest req) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(buildErrorResponse(ex, HttpStatus.BAD_REQUEST, req, null));
        }

        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleConflict(UserAlreadyExistsException ex, HttpServletRequest req) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(buildErrorResponse(ex, HttpStatus.CONFLICT, req, null));
        }

        @ExceptionHandler(InvalidResourceException.class)
        public ResponseEntity<ErrorResponse> handleInvalidResourceExcepsion(InvalidResourceException ex,
                        HttpServletRequest req) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(buildErrorResponse(ex, HttpStatus.CONFLICT, req, null));
        }

        @ExceptionHandler(NoAvailableResourceException.class)
        public ResponseEntity<ErrorResponse> handleNullPointerException(NoAvailableResourceException ex,
                        HttpServletRequest req) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(buildErrorResponse(ex, HttpStatus.CONFLICT, req, null));
        }

        @ExceptionHandler(UnsupportedOperationException.class)
        public ResponseEntity<ErrorResponse> handleMapperException(UnsupportedOperationException ex,
                        HttpServletRequest req) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(buildErrorResponse(ex, HttpStatus.CONFLICT, req, null));
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ErrorResponse> handleAuthenticationExceptions(AuthenticationException ex,
                        HttpServletRequest req) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, req, null));
        }

        @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleAuthenticationCredentialsNotFoundExceptions(
                        AuthenticationCredentialsNotFoundException ex, HttpServletRequest req) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, req, null));
        }

        @ExceptionHandler(MailSendException.class)
        public ResponseEntity<ErrorResponse> handleMailSendException(MailSendException ex, HttpServletRequest req) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorResponse(
                                ex, HttpStatus.SERVICE_UNAVAILABLE, req, null));
        }

        @ExceptionHandler(MessagingException.class)
        public ResponseEntity<ErrorResponse> handleMessagingException(MessagingException ex, HttpServletRequest req) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, req, null));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, null));
        }
}
