package com.dbms.dbms.service;

import com.dbms.dbms.dao.AppointmentDao;
import com.dbms.dbms.dto.Appointment;
import com.dbms.dbms.dto.AppointmentConfirm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentDao appointmentDao;

    @Autowired
    public AppointmentService(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    public int createAppointment(String email, Appointment appointment) {
        appointment.setConfirmed(false);
        return appointmentDao.createAppointment(email, appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDao.getAllAppointments();
    }

    public List<Appointment> getUnconfirmedAppointments() {
        return appointmentDao.getUnconfirmedAppointments();
    }

    public Appointment getAppointmentById(int id) {
        return appointmentDao.getAppointmentById(id);
    }

    public List<Appointment> getAppointmentsForPatient(int id) {
        return appointmentDao.getAllAppointmentsForPatient(id);
    }

    public List<Appointment> getConfirmedAppointmentsForDoctor(int id) {
        return appointmentDao.getConfirmedAppointmentsForDoctor(id);
    }

    public int confirmAppointment(int id, AppointmentConfirm confirm) {
        return appointmentDao.confirmAppointment(id, confirm.getTime());
    }

    public int deleteAppointment(int id) {
        return appointmentDao.deleteAppointmentById(id);
    }
}
