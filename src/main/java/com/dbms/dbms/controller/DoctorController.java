package com.dbms.dbms.controller;

import com.dbms.dbms.dto.Doctor;
import com.dbms.dbms.dto.Treatment;
import com.dbms.dbms.service.AppointmentService;
import com.dbms.dbms.service.DoctorService;
import com.dbms.dbms.service.TreatmentService;
import com.dbms.dbms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final TreatmentService treatmentService;

    @Autowired
    public DoctorController(DoctorService doctorService, UserService userService, AppointmentService appointmentService, TreatmentService treatmentService) {
        this.doctorService = doctorService;
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.treatmentService = treatmentService;
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isProfileComplete = userService.isProfileComplete(auth.getName());
        if (!isProfileComplete) return "redirect:profile";
        return "doctor/dashboard";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findAny().get().toString();

        Doctor storedDetails = doctorService.findDoctorByEmail(auth.getName());
        Doctor doctor = storedDetails == null ? new Doctor() : storedDetails;

        model.addAttribute("doctor", doctor);
        return "doctor/profile";
    }


    @PostMapping("/profile")
    public String postProfile(@ModelAttribute("doctor") Doctor doctor) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findAny().get().toString();
        doctorService.upsertDoctor(auth.getName(), doctor);
        return "redirect:dashboard?details_update";
    }

    @GetMapping("/appointments")
    public String geConfirmedAppointments(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int id = userService.findUserByEmail(auth.getName()).getId();
        model.addAttribute("appointments", appointmentService.getConfirmedAppointmentsForDoctor(id));
        return "doctor/appointments";
    }

    @GetMapping("/create_treatment")
    public String getCreateTreatment(Model model, @RequestParam(value = "pid", required = false, defaultValue = "-1") int patientId) {
        Treatment treatment = new Treatment();
        treatment.setStart(Date.valueOf(LocalDate.now()));
        if (patientId != -1) treatment.setPatientId(patientId);
        model.addAttribute("treatment", treatment);
        return "doctor/create_treatment";
    }

    @PostMapping("/create_treatment")
    public String postCreateTreatment(@ModelAttribute("treatment") Treatment treatment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int id = userService.findUserByEmail(auth.getName()).getId();
        treatment.setDoctorId(id);
        treatmentService.createTreatment(treatment);
        return "redirect:/doctor/dashboard";
    }

    @GetMapping("/ongoing_treatments")
    public String getOngoingTreatments(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int id = userService.findUserByEmail(auth.getName()).getId();

        model.addAttribute("treatments", treatmentService.getAllOngoingTreatmentsByDoctor(id));
        return "doctor/treatments";
    }

    @GetMapping("/all_treatments")
    public String getAllTreatments(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int id = userService.findUserByEmail(auth.getName()).getId();

        model.addAttribute("treatments", treatmentService.getAllTreatmentsByDoctor(id));
        return "doctor/treatments";
    }

    @GetMapping("/end_treatment/{tid}")
    public String endTreatment(Model model, @PathVariable(value = "tid", required = true) String tid) {
        treatmentService.endTreatment(Integer.parseInt(tid));
        return "redirect:/doctor/dashboard";
    }
}
