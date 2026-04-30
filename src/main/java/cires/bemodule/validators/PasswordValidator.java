package cires.bemodule.validators;

import cires.bemodule.annotations.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASS_PATTERN =
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
    private final Pattern pattern = Pattern.compile(PASS_PATTERN);

    @Override
    public boolean isValid(String password,  ConstraintValidatorContext context){
            if (password == null) return false;
            return pattern.matcher(password).matches();
    }
}
