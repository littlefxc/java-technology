package com.littlefxc.examples.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author fengxuechao
 */
public class IsPhoneConstraintValidator implements ConstraintValidator<IsPhone, String> {

    private Pattern pattern = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");

    @Override
    public void initialize(IsPhone constraintAnnotation) {
        //启动时执行
    }

    /**
     * 自定义校验逻辑
     * @param value CharSequence为校验的类型
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) {
            return true;
        }
        return pattern.matcher(value).matches();
    }
}
