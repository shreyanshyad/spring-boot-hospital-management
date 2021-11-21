package com.dbms.dbms.service;

import com.dbms.dbms.dao.AdmissionDao;
import com.dbms.dbms.dto.Admission;
import org.springframework.stereotype.Service;

@Service
public class AdmissionService {
    private final AdmissionDao admissionDao;

    public AdmissionService(AdmissionDao admissionDao) {
        this.admissionDao = admissionDao;
    }

    public int createAdmission(Admission admission) {
        return admissionDao.createAdmission(admission);
    }
}
