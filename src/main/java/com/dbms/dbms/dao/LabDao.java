package com.dbms.dbms.dao;

import com.dbms.dbms.dto.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class LabDao {
    private static final Logger logger = LoggerFactory.getLogger(LabDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LabDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createTest(Test test) {
        String sql = "insert into test(patient_id, name, result, report_file,time) values (?,?,?,?,?)";
        return jdbcTemplate.update(sql, test.getPatientId(), test.getName(), test.getResult(), test.getFileName(), Timestamp.valueOf(test.getTime()));
    }

    public List<Test> getReportsForPatient(int patientId) {
        String sql = "select * from test where patient_id=?";
        return jdbcTemplate.query(sql, mapTestFromDb(), patientId);
    }

    public List<Test> getAllReports() {
        String sql = "select * from test";
        return jdbcTemplate.query(sql, mapTestFromDb());
    }

    private RowMapper<Test> mapTestFromDb() {
        return (resultSet, i) -> {
            Test test = new Test();
            test.setFileName(resultSet.getString("report_file"));
            test.setName(resultSet.getString("name"));
            test.setPatientId(resultSet.getInt("patient_id"));
            test.setTime(resultSet.getTimestamp("time").toLocalDateTime());
            test.setResult(resultSet.getString("result"));
            return test;
        };
    }
}
