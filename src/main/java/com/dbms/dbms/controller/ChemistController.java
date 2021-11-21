package com.dbms.dbms.controller;

import com.dbms.dbms.dto.Staff;
import com.dbms.dbms.service.ChemistService;
import com.dbms.dbms.service.StaffService;
import com.dbms.dbms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chemist")
public class ChemistController {

    private final ChemistService chemistService;
    private final UserService userService;
    private final StaffService staffService;

    @Autowired
    public ChemistController(ChemistService chemistService, UserService userService, StaffService staffService) {
        this.chemistService = chemistService;
        this.userService = userService;
        this.staffService = staffService;
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isProfileComplete = userService.isProfileComplete(auth.getName());
        if (!isProfileComplete) return "redirect:profile";
        return "chemist/dashboard";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findAny().get().toString();

        Staff storedDetails = staffService.findStaffByEmail(auth.getName(), role);
        Staff staff = storedDetails == null ? new Staff() : storedDetails;

        model.addAttribute("staff", staff);
        return "chemist/profile";
    }


    @PostMapping("/profile")
    public String postProfile(@ModelAttribute("staff") Staff staff) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findAny().get().toString();
        staffService.upsertStaff(auth.getName(), role, staff);
        return "redirect:dashboard?details_update";
    }
}
