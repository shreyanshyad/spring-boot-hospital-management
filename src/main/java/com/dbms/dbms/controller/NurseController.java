package com.dbms.dbms.controller;

import com.dbms.dbms.dto.*;
import com.dbms.dbms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/nurse")
public class NurseController {

    private final NurseService nurseService;
    private final UserService userService;
    private final StaffService staffService;
    private final AppointmentService appointmentService;
    private final TreatmentService treatmentService;
    private final AdmissionService admissionService;
    private Bill bill;
    private final BillService billService;
    private final RoomService roomService;
    private Admission admission;

    @Autowired
    public NurseController(NurseService nurseService, UserService userService, StaffService staffService,
                           AppointmentService appointmentService, TreatmentService treatmentService,
                           AdmissionService admissionService, BillService billService, RoomService roomService) {
        this.nurseService = nurseService;
        this.userService = userService;
        this.staffService = staffService;
        this.appointmentService = appointmentService;
        this.treatmentService = treatmentService;
        this.admissionService = admissionService;
        this.billService = billService;
        this.roomService = roomService;
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isProfileComplete = userService.isProfileComplete(auth.getName());
        if (!isProfileComplete) return "redirect:profile";
        return "nurse/dashboard";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findAny().get().toString();

        Staff storedDetails = staffService.findStaffByEmail(auth.getName(), role);
        Staff staff = storedDetails == null ? new Staff() : storedDetails;

        model.addAttribute("staff", staff);
        return "nurse/profile";
    }


    @PostMapping("/profile")
    public String postProfile(@ModelAttribute("staff") Staff staff) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findAny().get().toString();
        staffService.upsertStaff(auth.getName(), role, staff);
        return "redirect:dashboard?details_update";
    }

    @GetMapping("/pending_appointments")
    public String viewPendingAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getUnconfirmedAppointments());
        return "nurse/pending_appointments";
    }

    @GetMapping("/confirm_appointment/{id}")
    public String getConfirmAppointmnet(@PathVariable("id") int id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        model.addAttribute("a", appointment);
        model.addAttribute("confirm", new AppointmentConfirm(appointment.getTime()));
        return "nurse/confirm_appointment";
    }

    @PostMapping("/confirm_appointment/{id}")
    public String postConfirmAppointment(@PathVariable("id") int id, @ModelAttribute("confirm") AppointmentConfirm appointmentConfirm) {
        appointmentService.confirmAppointment(id, appointmentConfirm);
        return "redirect:/nurse/pending_appointments";
    }

    @GetMapping("/appointments")
    public String getTodayAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "nurse/appointments";
    }

    @GetMapping("/filter_treatments")
    public String searchTreatments(Model model) {
        model.addAttribute("treatments", treatmentService.filterTreatments("", "", "", ""));
        model.addAttribute("filter", new TreatmentsFilter());
        return "nurse/filter_treatments";
    }

    @PostMapping("/filter_treatments")
    public String postSearchTreatments(Model model, @ModelAttribute("filter") TreatmentsFilter filter) {
        model.addAttribute("treatments", treatmentService.filterTreatments(filter.getpFname(), filter.getpLname(), filter.getdFname(), filter.getdLname()));
        return "nurse/filter_treatments";
    }

    @GetMapping("/view_admissions/{tid}")
    public String viewAdmissions(Model model, @PathVariable(value = "tid", required = true) String tid) {
        model.addAttribute("admissions", admissionService.getAdmissions(Integer.parseInt(tid)));
        model.addAttribute("admit", admissionService.isAdmitted(Integer.parseInt(tid)));
        model.addAttribute("tid", tid);
        return "nurse/view_admissions";
    }

    @GetMapping("/admit/{tid}")
    public String getAdmitPatient(Model model, @PathVariable(value = "tid", required = true) String tid) {
        Admission admission = new Admission();
        admission.setTreatmentId(Integer.parseInt(tid));
        model.addAttribute("admission", admission);
        return "nurse/admission_form";
    }

    @PostMapping("/admit")
    public String postAdmission(@ModelAttribute("admission") Admission admission) {
        admission.setAdmissionTime(LocalDateTime.now());
        admissionService.createAdmission(admission);
        return "redirect:/nurse/dashboard";
    }

    @GetMapping("/discharge/{tid}")
    public String dischargePatient(@PathVariable(value = "tid", required = true) String tid) {
        admissionService.dischargePatient(Integer.parseInt(tid));
        return "redirect:/nurse/dashboard";
    }

    @GetMapping("/bill_pid")
    public String getBill() {
        return "nurse/pid";
    }

    @PostMapping("/bill_pid")
    public String getBill(Model model, @RequestParam("id") int id) {
        bill = new Bill();
        bill.setPatientId(id);
        return "redirect:/nurse/bill";
    }

    @GetMapping("/bill")
    public String getBill(Model model) {
        model.addAttribute("bill", bill);
        model.addAttribute("entry", new BillEntry());
        return "nurse/bill";
    }

    @PostMapping("/bill")
    public String postBill(@ModelAttribute("entry") BillEntry entry, Model model) {
        bill.addEntry(entry);
        model.addAttribute("bill", bill);
        return "nurse/bill";
    }

    @GetMapping("/bill_done")
    public String billDone() {
        double amt = 0;
        for (BillEntry e : bill.getEntries()) {
            amt += e.getCost() * e.getQuantity();
        }
        bill.setAmount(amt);
        billService.createBill(bill);
        return "redirect:/nurse/dashboard";
    }

    @GetMapping("/pay")
    public String getPay() {
        return "nurse/pay";
    }

    @PostMapping("/pay")
    public String postPay(@RequestParam("id") int id, @RequestParam("amt") int amt) {
        billService.pay(id, amt);
        return "redirect:/nurse/dashboard";
    }

    @GetMapping("/rooms")
    public String getRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "nurse/view_rooms";
    }
}
