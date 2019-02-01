package com.littlefxc.examples.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author fengxuechao
 */

@Constraint(validatedBy = IsPhoneConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsPhone {

    /**
     * 错误提示
     *
     * @return
     */
    String message() default "{login.phone.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
