package com.dbms.dbms.dao;

import com.dbms.dbms.dto.Admission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class AdmissionDao {
    private static final Logger logger = LoggerFactory.getLogger(AdmissionDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AdmissionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createAdmission(Admission admission) {
        String sql = "insert into admission(admission_time, discharge_time, treatment_id, room_id) " +
                "values(?,?,?,?)";
        Timestamp admissionTime = Timestamp.valueOf(admission.getAdmissionTime());
        Timestamp dischargeTime = null;
        if (admission.getDischargeTime() != null) dischargeTime = Timestamp.valueOf(admission.getDischargeTime());
        return jdbcTemplate.update(sql, admissionTime, dischargeTime, admission.getTreatmentId(), admission.getRoomId());
    }
}
