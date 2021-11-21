package com.dbms.dbms.service;

import com.dbms.dbms.dao.DoctorDao;
import com.dbms.dbms.dao.UserDao;
import com.dbms.dbms.dto.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorDao doctorDao;
    private final UserDao userDao;

    public DoctorService(DoctorDao doctorDao, UserDao userDao) {
        this.doctorDao = doctorDao;
        this.userDao = userDao;
    }

    public void upsertDoctor(String email, Doctor doctor) {
        if (doctorDao.updateDoctor(email, doctor) == 0) {
            doctorDao.createDoctor(email, doctor);
            userDao.setUserInit(email);
        }
    }

    public List<Doctor> getAllDoctors() {
        return doctorDao.getAllDoctors();
    }

    public Doctor findDoctorByEmail(String email) {
        return doctorDao.findDoctorByEmail(email);
    }

    public Doctor findDoctorById(int id) {
        return doctorDao.findDoctorById(id);
    }
}
