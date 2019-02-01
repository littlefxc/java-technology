package com.littlefxc.examples.model;

import com.littlefxc.examples.validation.IsPhone;
import com.littlefxc.examples.validation.ValidatorAdd;
import com.littlefxc.examples.validation.ValidatorEdit;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author fengxuechao
 */
@Data
public class LoginVo {

    /**
     * 编辑时：不能为null
     */
    @NotNull(message = "{login.id.NotNull}", groups = {ValidatorEdit.class})
    private String id;

    /**
     * 添加时：不能为空，符合邮箱格式
     * 编辑时：符合邮箱格式
     */
    @NotBlank(message = "{login.username.NotBlank}", groups = {ValidatorAdd.class})
    @Email(message = "{login.username.Email}", groups = {ValidatorAdd.class, ValidatorEdit.class})
    private String username;

    /**
     * 添加时：不能为空，密码长度至少为6位
     * 编辑时：密码长度至少为6位
     */
    @NotBlank(message = "{login.password.NotBlank}", groups = {ValidatorAdd.class})
    @Length(min = 6, message = "{login.password.Length}", groups = {ValidatorAdd.class, ValidatorEdit.class})
    private String password;

    /**
     * 添加时：符合手机格式
     * 编辑时：符合手机格式
     */
    @IsPhone(groups = {ValidatorAdd.class, ValidatorEdit.class})
    private String phone;
}
