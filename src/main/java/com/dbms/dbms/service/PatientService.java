package com.dbms.dbms.service;

import com.dbms.dbms.dao.PatientDao;
import com.dbms.dbms.dao.UserDao;
import com.dbms.dbms.dto.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientDao patientDao;
    private final UserDao userDao;

    @Autowired
    public PatientService(PatientDao patientDao, UserDao userDao) {
        this.patientDao = patientDao;
        this.userDao = userDao;
    }

    public void upsertPatient(String email, Patient patient) {
        if (patientDao.updatePatient(email, patient) == 0) {
            patientDao.createPatient(email, patient);
            userDao.setUserInit(email);
        }
    }

    public Patient findPatientByEmail(String email) {
        return patientDao.findPatientByEmail(email);
    }

    public List<Patient> getAllPatient() {
        return patientDao.selectAllPatients();
    }
}
