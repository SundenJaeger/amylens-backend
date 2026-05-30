// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.handler;

import io.github.sundenjaeger.amylensbackend.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                            HttpHeaders headers,
                                                                            HttpStatusCode status,
                                                                            WebRequest request) {
        Map<String, List<String>> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.computeIfAbsent(error.getField(), s -> new ArrayList<>())
                        .add(error.getDefaultMessage()));

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_CONTENT);
        problemDetail.setTitle("Validation Failed");
        problemDetail.setDetail("One or more fields failed validation");
        problemDetail.setInstance(URI.create(request.getContextPath()));
        problemDetail.setProperty("fieldErrors", fieldErrors);

        return ResponseEntity.unprocessableContent().body(problemDetail);
    }

    @ExceptionHandler(MissingReviewNoteException.class)
    public ResponseEntity<ProblemDetail> handleMissingReviewNote(MissingReviewNoteException ex,
                                                                 HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Missing Review Note");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleSessionNotFound(SessionNotFoundException ex,
                                                               HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Session Not Found");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(UnauthorizedDeviceException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorizedDevice(UnauthorizedDeviceException ex,
                                                                  HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setTitle("Unauthorized Device");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(DeviceAlreadyExistException.class)
    public ResponseEntity<ProblemDetail> handleDeviceAlreadyExist(DeviceAlreadyExistException ex,
                                                                  HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Device already exist");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(VarietyNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleVarietyNotFound(VarietyNotFoundException ex,
                                                               HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Variety not found");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(VarietyAlreadyExistException.class)
    public ResponseEntity<ProblemDetail> handleVarietyAlreadyExist(VarietyAlreadyExistException ex,
                                                                   HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Variety already exist");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ProblemDetail> handleUserAlreadyExist(UserAlreadyExistException ex,
                                                                HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("User already exist");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentials(BadCredentialsException ex,
                                                              HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problemDetail.setTitle("Authentication Failed");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.of(problemDetail).build();
    }
}
