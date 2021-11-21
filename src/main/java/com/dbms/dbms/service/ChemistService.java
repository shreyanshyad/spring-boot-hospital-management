package com.dbms.dbms.service;

import com.dbms.dbms.dao.ChemistDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChemistService {

    private final ChemistDao chemistDao;

    @Autowired
    public ChemistService(ChemistDao chemistDao) {
        this.chemistDao = chemistDao;
    }
}
