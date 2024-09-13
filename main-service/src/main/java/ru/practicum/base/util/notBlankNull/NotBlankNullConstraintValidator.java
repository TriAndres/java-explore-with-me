package ru.practicum.base.util.notBlankNull;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankNullConstraintValidator implements ConstraintValidator<NotBlankNull, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        } else {
            return !s.isBlank();
        }
    }
}
