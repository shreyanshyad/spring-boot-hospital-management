package com.dbms.dbms.service;

import com.dbms.dbms.dao.LabDao;
import com.dbms.dbms.dto.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabService {

    private final LabDao labDao;

    @Autowired
    public LabService(LabDao labDao) {
        this.labDao = labDao;
    }

    public int createTest(Test test) {
        return labDao.createTest(test);
    }

    public List<Test> getTestsForPatient(int patientId) {
        return labDao.getReportsForPatient(patientId);
    }

    public List<Test> getAllTests() {
        return labDao.getAllReports();
    }
}
