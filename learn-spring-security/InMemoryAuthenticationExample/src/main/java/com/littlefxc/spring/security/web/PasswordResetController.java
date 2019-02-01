package com.littlefxc.spring.security.web;

import com.littlefxc.spring.security.web.dto.PasswordResetDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/reset-password")
public class PasswordResetController {

    @ModelAttribute("passwordResetForm")
    public PasswordResetDto passwordReset() {
        return new PasswordResetDto();
    }

    @GetMapping
    public String showPasswordReset(Model model) {
        return "reset-password";
    }

    @PostMapping
    public String handlePasswordReset(
            @ModelAttribute("passwordResetForm") @Valid PasswordResetDto form, BindingResult result) {

        if (result.hasErrors()) {
            return "reset-password";
        }

        // save/updaate form here

        return "redirect:/login?resetSuccess";
    }

}