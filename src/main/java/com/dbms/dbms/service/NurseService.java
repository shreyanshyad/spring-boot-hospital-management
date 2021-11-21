package com.dbms.dbms.service;

import com.dbms.dbms.dao.NurseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NurseService {

    private final NurseDao nurseDao;

    @Autowired
    public NurseService(NurseDao nurseDao) {
        this.nurseDao = nurseDao;
    }
}
