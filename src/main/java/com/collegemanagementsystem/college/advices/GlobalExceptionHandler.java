package com.collegemanagementsystem.college.advices;

import com.collegemanagementsystem.college.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    ApiError apiError =
        ApiError.builder()
            .httpStatus(HttpStatus.NOT_FOUND)
            .error(ex.getMessage())
            .subError(List.of())
            .build();
    return buildApiResponse(apiError);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotFoundException(
      MethodArgumentNotValidException ex) {
    List<String> subErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + " : " + e.getDefaultMessage())
            .toList();
    ApiError apiError =
        ApiError.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .error("Input Validation failed")
            .subError(subErrors)
            .build();
    return buildApiResponse(apiError);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException ex) {
    ApiError apiError =
        ApiError.builder()
            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
            .error("Some Internal Server Failure Happened")
            .subError(List.of())
            .build();
    return buildApiResponse(apiError);
  }

  public ResponseEntity<ApiResponse<?>> buildApiResponse(ApiError error) {
    return ResponseEntity.status(error.getHttpStatus()).body(new ApiResponse<>(error));
  }
}
