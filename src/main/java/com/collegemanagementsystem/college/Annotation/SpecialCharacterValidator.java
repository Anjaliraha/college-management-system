package com.collegemanagementsystem.college.Annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SpecialCharacterValidator
    implements ConstraintValidator<SpecialCharacterNotAllowed, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value.matches("[a-zA-Z ]+");
  }
}
