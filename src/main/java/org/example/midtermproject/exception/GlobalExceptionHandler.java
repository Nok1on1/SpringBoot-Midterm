package org.example.midtermproject.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleException(Exception exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"));
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ApiError> handleNotFound(NoSuchElementException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiError.of(HttpStatus.NOT_FOUND, exception.getMessage()));
  }

  @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class,
      MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
  public ResponseEntity<ApiError> handleBadRequest(Exception exception) {
    return ResponseEntity.badRequest()
        .body(ApiError.of(HttpStatus.BAD_REQUEST, exception.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
    Map<String, String> fieldErrors = new LinkedHashMap<>();
    exception.getBindingResult().getFieldErrors()
        .forEach(error -> fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage()));

    return ResponseEntity.badRequest()
        .body(ApiError.withFieldErrors(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors));
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<ApiError> handleMethodValidation(
      HandlerMethodValidationException exception) {
    return ResponseEntity.badRequest()
        .body(ApiError.of(HttpStatus.BAD_REQUEST, exception.getMessage()));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ApiError> handleFileTooLarge(MaxUploadSizeExceededException exception) {
    return ResponseEntity.status(HttpStatus.CONTENT_TOO_LARGE)
        .body(ApiError.of(HttpStatus.CONTENT_TOO_LARGE, "Uploaded file is too large"));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handleConflict(DataIntegrityViolationException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ApiError.of(HttpStatus.CONFLICT, "The request conflicts with existing data"));
  }


  @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
  public ResponseEntity<ApiError> handleInvalidDataAccess(
      InvalidDataAccessResourceUsageException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid data access"));
  }
}
