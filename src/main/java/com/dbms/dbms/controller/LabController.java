package com.dbms.dbms.controller;

import com.dbms.dbms.dto.Staff;
import com.dbms.dbms.dto.Test;
import com.dbms.dbms.service.FileStorageService;
import com.dbms.dbms.service.LabService;
import com.dbms.dbms.service.StaffService;
import com.dbms.dbms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/lab")
public class LabController {

    private final LabService labService;
    private final UserService userService;
    private final StaffService staffService;
    private final FileStorageService fileStorageService;

    @Autowired
    public LabController(LabService labService, UserService userService, StaffService staffService, FileStorageService fileStorageService) {
        this.labService = labService;
        this.userService = userService;
        this.staffService = staffService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isProfileComplete = userService.isProfileComplete(auth.getName());
        if (!isProfileComplete) return "redirect:profile";
        return "lab/dashboard";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findAny().get().toString();

        Staff storedDetails = staffService.findStaffByEmail(auth.getName(), role);
        Staff staff = storedDetails == null ? new Staff() : storedDetails;

        model.addAttribute("staff", staff);
        return "lab/profile";
    }


    @PostMapping("/profile")
    public String postProfile(@ModelAttribute("staff") Staff staff) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findAny().get().toString();
        staffService.upsertStaff(auth.getName(), role, staff);
        return "redirect:dashboard?details_update";
    }

    @GetMapping("/create_report")
    public String getCreateReport(Model model) {
        model.addAttribute("test", new Test());
        return "lab/create_test";
    }

    @PostMapping("/create_report")
    public String postReport(@ModelAttribute("test") Test test, @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        test.setFileName(fileName);
        labService.createTest(test);
        return "redirect:/lab/dashboard";
    }
}
