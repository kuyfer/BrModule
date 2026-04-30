package cires.bemodule.validators;

import cires.bemodule.annotations.PasswordMatches;
import cires.bemodule.dtos2.ResetPasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, ResetPasswordRequest> {

    @Override
    public boolean isValid(ResetPasswordRequest request, ConstraintValidatorContext context) {
        return request.getNewPassword() != null &&
                request.getNewPassword().equals(request.getConfirmPassword());

    }
}