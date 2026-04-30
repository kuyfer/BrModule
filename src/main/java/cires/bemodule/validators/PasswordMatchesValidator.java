package cires.bemodule.validators;

import cires.bemodule.annotations.PasswordMatches;
import cires.bemodule.interfaces.PasswordConfirmable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordConfirmable> {

    @Override
    public boolean isValid(PasswordConfirmable pass, ConstraintValidatorContext context) {
        return pass.getPassword() != null &&
                pass.getPassword().equals(pass.getPasswordConfirm());

    }
}