package com.dbms.dbms.controller;

import com.dbms.dbms.dto.User;
import com.dbms.dbms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping(path = "/dashboard")
    public String getDashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findAny().get().toString();
        boolean isProfileComplete = userService.isProfileComplete(auth.getName());

        switch (role) {
            case "ROLE_ADMIN":
                return "redirect:admin/dashboard";
            case "ROLE_PATIENT":
                return "redirect:patient/dashboard";
            case "ROLE_DOCTOR":
                return "redirect:doctor/dashboard";
            case "ROLE_NURSE":
                return "redirect:nurse/dashboard";
            case "ROLE_CHEMIST":
                return "redirect:chemist/dashboard";
            case "ROLE_LAB":
                return "redirect:lab/dashboard";
        }
        return "redirect:login";
    }

    @GetMapping(path = "/login")
    public String showLogin() {
        if (isAuthenticated()) return "redirect:dashboard";
        return "login";
    }

    @GetMapping(path = "/register")
    public String showRegister(Model model) {
        if (isAuthenticated()) return "redirect:dashboard";
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(path = "/register")
    public String registerUserAccount(@ModelAttribute("user") User user) {
        int res = userService.createPatient(user);
        if (res == 0) return "redirect:/register?error";
        return "redirect:/register?success";
    }
}