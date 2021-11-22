package com.dbms.dbms.dao;

import com.dbms.dbms.dto.Admission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AdmissionDao {
    private static final Logger logger = LoggerFactory.getLogger(AdmissionDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AdmissionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createAdmission(Admission admission) {
        String sql = "insert into admission(admission_time, treatment_id, room_id) " +
                "values(?,?,?)";
        Timestamp admissionTime = Timestamp.valueOf(admission.getAdmissionTime());
        return jdbcTemplate.update(sql, admissionTime, admission.getTreatmentId(), admission.getRoomId());
    }

    public int dischargePatient(int treatmentId) {
        String sql = "update admission set discharge_time=? where treatment_id=? and discharge_time is null";
        return jdbcTemplate.update(sql, Timestamp.valueOf(LocalDateTime.now()), treatmentId);
    }

    public boolean isAdmitted(int treatmentId) {
        String sql = "select * from admission where treatment_id=? and discharge_time is null";
        int count = jdbcTemplate.query(sql, mapAdmission(), treatmentId).size();
        return count > 0;
    }

    public List<Admission> viewAdmissions(int treatmentId) {
        String sql = "select * from admission where treatment_id=?";
        return jdbcTemplate.query(sql, mapAdmission(), treatmentId);
    }

    private RowMapper<Admission> mapAdmission() {
        return (resultSet, i) -> {
            Admission admission = new Admission();
            admission.setId(resultSet.getInt("admission_id"));
            admission.setRoomId(resultSet.getInt("room_id"));
            admission.setAdmissionTime(resultSet.getTimestamp("admission_time").toLocalDateTime());
            if (resultSet.getTimestamp("discharge_time") != null)
                admission.setDischargeTime(resultSet.getTimestamp("discharge_time").toLocalDateTime());
            admission.setTreatmentId(resultSet.getInt("treatment_id"));
            return admission;
        };
    }
}
