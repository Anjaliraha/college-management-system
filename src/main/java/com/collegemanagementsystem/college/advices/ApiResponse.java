package com.collegemanagementsystem.college.advices;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {

  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime timestamp;

  private T data;
  private ApiError apiError;

  public ApiResponse() {
    timestamp = LocalDateTime.now();
  }

  public ApiResponse(T data) {
    this();
    this.data = data;
  }

  public ApiResponse(ApiError error) {
    this();
    this.apiError = error;
  }
}
