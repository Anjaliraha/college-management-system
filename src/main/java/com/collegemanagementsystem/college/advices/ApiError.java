package com.collegemanagementsystem.college.advices;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ApiError {
  private HttpStatus httpStatus;
  private String error;
  private List<String> subError;
}
