package com.dbms.dbms.service;

import com.dbms.dbms.dao.TreatmentDao;
import com.dbms.dbms.dto.Treatment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentService {

    private final TreatmentDao treatmentDao;

    @Autowired
    public TreatmentService(TreatmentDao treatmentDao) {
        this.treatmentDao = treatmentDao;
    }

    public int createTreatment(Treatment treatment) {
        return treatmentDao.createTreatment(treatment);
    }

    public List<Treatment> getAllTreatmentsByDoctor(int doctorId) {
        return treatmentDao.getAllTreatmentsByDoctor(doctorId);
    }

    public List<Treatment> getAllOngoingTreatmentsByDoctor(int doctorId) {
        return treatmentDao.getAllOngoingTreatmentsByDoctor(doctorId);
    }

    public List<Treatment> getAllTreatmentsByPatient(int patientId) {
        return treatmentDao.getAllTreatmentsByPatient(patientId);
    }

    public List<Treatment> getAllOngoingTreatmentsByPatient(int patientId) {
        return treatmentDao.getAllOngoingTreatmentsByPatient(patientId);
    }

    public int endTreatment(int treatmentId) {
        return treatmentDao.endTreatment(treatmentId);
    }

    public List<Treatment> filterTreatments(String pFname, String pLname, String dFname, String dLname) {
        return treatmentDao.filteredTreatmentDetails(pFname, pLname, dFname, dLname);
    }
}
