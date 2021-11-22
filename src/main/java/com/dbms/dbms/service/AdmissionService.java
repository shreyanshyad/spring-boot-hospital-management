package com.dbms.dbms.service;

import com.dbms.dbms.dao.AdmissionDao;
import com.dbms.dbms.dto.Admission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmissionService {
    private final AdmissionDao admissionDao;

    public AdmissionService(AdmissionDao admissionDao) {
        this.admissionDao = admissionDao;
    }

    public int createAdmission(Admission admission) {
        return admissionDao.createAdmission(admission);
    }

    public int dischargePatient(int treatmentId) {
        return admissionDao.dischargePatient(treatmentId);
    }

    public List<Admission> getAdmissions(int treatmentId) {
        return admissionDao.viewAdmissions(treatmentId);
    }

    public Boolean isAdmitted(int treatmentId) {
        return admissionDao.isAdmitted(treatmentId);
    }
}
