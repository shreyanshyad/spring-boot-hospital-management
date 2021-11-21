package com.dbms.dbms.controller;

import com.dbms.dbms.dto.Appointment;
import com.dbms.dbms.dto.Patient;
import com.dbms.dbms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final TreatmentService treatmentService;
    private final LabService labService;
    private final BillService billService;

    @Autowired
    public PatientController(PatientService patientService, UserService userService, DoctorService doctorService,
                             AppointmentService appointmentService, TreatmentService treatmentService,
                             LabService labService, BillService billService) {
        this.patientService = patientService;
        this.userService = userService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.treatmentService = treatmentService;
        this.labService = labService;
        this.billService = billService;
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Patient storedDetails = patientService.findPatientByEmail(auth.getName());
        Patient patient = storedDetails == null ? new Patient() : storedDetails;

        model.addAttribute("patient", patient);
        return "patient/profile";
    }

    @PostMapping("/profile")
    public String postProfile(@ModelAttribute("patient") Patient patient) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        patientService.upsertPatient(auth.getName(), patient);
        return "redirect:dashboard?details_update";
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isProfileComplete = userService.isProfileComplete(auth.getName());
        if (!isProfileComplete) return "redirect:profile";
        return "patient/dashboard";
    }

    @GetMapping("/book_appointment")
    public String getBookAppointment(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "patient/book_appointment";
    }

    @GetMapping("/book_appointment/{id}")
    public String getBookAppointmentWithDoctor(@PathVariable("id") int id, Model model) {
        model.addAttribute("doctor", doctorService.findDoctorById(id));
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("id", id);
        return "patient/appointment_form";
    }

    @PostMapping("/book_appointment/{id}")
    public String postBookAppointment(@PathVariable("id") int doctorId, @ModelAttribute("appointment") Appointment appointment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        appointment.setDoctorId(doctorId);
        appointmentService.createAppointment(auth.getName(), appointment);
        return "redirect:/patient/dashboard";
    }

    @GetMapping("/show_appointments")
    public String showAppointments(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int userId = userService.findUserByEmail(auth.getName()).getId();
        model.addAttribute("appointments", appointmentService.getAppointmentsForPatient(userId));
        return "patient/view_appointments";
    }

    @GetMapping("/delete_appointment/{id}/")
    public String deleteAppointment(@PathVariable("id") int id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/patient/dashboard";
    }

    @GetMapping("/ongoing_treatments")
    public String getOngoingTreatments(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int id = userService.findUserByEmail(auth.getName()).getId();

        model.addAttribute("treatments", treatmentService.getAllOngoingTreatmentsByPatient(id));
        return "patient/treatments";
    }

    @GetMapping("/all_treatments")
    public String getAllTreatments(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int id = userService.findUserByEmail(auth.getName()).getId();

        model.addAttribute("treatments", treatmentService.getAllOngoingTreatmentsByPatient(id));
        return "patient/treatments";
    }

    @GetMapping("/reports")
    public String getReports(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int id = userService.findUserByEmail(auth.getName()).getId();

        model.addAttribute("tests", labService.getTestsForPatient(id));

        return "patient/view_reports";
    }

    @GetMapping("/bills")
    public String viewBills(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int id = userService.findUserByEmail(auth.getName()).getId();

        model.addAttribute("bills", billService.getBillsForPatient(id));

        return "patient/bills";
    }

    @GetMapping("/bill_detail/{id}")
    public String viewDetailedBill(Model model, @PathVariable("id") int id) {
        model.addAttribute("entries", billService.getDetailedBill(id));
        return "patient/detailed_bill";
    }
}
