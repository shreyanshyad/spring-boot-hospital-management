package com.dbms.dbms.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NurseDao {
    private static final Logger logger = LoggerFactory.getLogger(NurseDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NurseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
