package com.dbms.dbms.controller;

import com.dbms.dbms.dto.Staff;
import com.dbms.dbms.dto.User;
import com.dbms.dbms.service.StaffService;
import com.dbms.dbms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@Controller
@RolesAllowed("ADMIN")
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final StaffService staffService;

    @Autowired
    public AdminController(UserService userService, StaffService staffService) {
        this.userService = userService;
        this.staffService = staffService;
    }

    @GetMapping("/create")
    public String getAdminCreateUser(Model mode) {
        mode.addAttribute("user", new User());
        return "admin/create";
    }

    @PostMapping("/create")
    public String postAdminCreateUser(@ModelAttribute("user") User user) {
        userService.createUser(user);
        return "redirect:create?success";
    }

    @GetMapping("/toggleUser/{id}")
    public String toggleUser(@PathVariable("id") int id) {
        userService.toggleUserEnabled(id);
        return "redirect:/admin/view_users";
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isProfileComplete = userService.isProfileComplete(auth.getName());
        if (!isProfileComplete) return "redirect:profile";
        return "admin/dashboard";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findAny().get().toString();

        Staff storedDetails = staffService.findStaffByEmail(auth.getName(), role);
        Staff staff = storedDetails == null ? new Staff() : storedDetails;

        model.addAttribute("staff", staff);
        return "admin/profile";
    }


    @PostMapping("/profile")
    public String postProfile(@ModelAttribute("staff") Staff staff) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findAny().get().toString();
        staffService.upsertStaff(auth.getName(), role, staff);
        return "redirect:/admin/profile?success";
    }

    @GetMapping("/view_users")
    public String getViewUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/view_users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin/view_users";
    }
}
