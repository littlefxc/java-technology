package com.littlefxc.spring.security.constraint;

import com.littlefxc.spring.security.web.dto.PasswordResetDto;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class FieldMatchConstraintValidatorTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidPasswords() {
        PasswordResetDto passwordReset = new PasswordResetDto();
        passwordReset.setPassword("password");
        passwordReset.setConfirmPassword("password");

        Set<ConstraintViolation<PasswordResetDto>> constraintViolations = validator.validate(passwordReset);

        assertEquals(constraintViolations.size(), 0);
    }

    @Test
    public void testInvalidPassword() {
        PasswordResetDto passwordReset = new PasswordResetDto();
        passwordReset.setPassword("password");
        passwordReset.setConfirmPassword("invalid-password");

        Set<ConstraintViolation<PasswordResetDto>> constraintViolations = validator.validate(passwordReset);

        assertEquals(constraintViolations.size(), 1);
    }

}