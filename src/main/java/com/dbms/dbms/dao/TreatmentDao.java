package com.dbms.dbms.dao;

import com.dbms.dbms.dto.Treatment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TreatmentDao {
    private static final Logger logger = LoggerFactory.getLogger(TreatmentDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TreatmentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createTreatment(Treatment treatment) {
        String sql = "insert into treatment(patient_id, doctor_id, start_time, end_time, problem, treatment) " +
                "values(?,?,?,?,?,?)";

        return jdbcTemplate.update(sql, treatment.getPatientId(), treatment.getDoctorId(), treatment.getStart(),
                treatment.getEnd(), treatment.getProblem(), treatment.getTreatment());
    }

    public List<Treatment> getAllTreatmentsByDoctor(int doctorId) {
        String sql = "select * from treatment_details where doctor_id=?";

        return jdbcTemplate.query(sql, mapTreatmentToDb(), doctorId);
    }

    public List<Treatment> getAllOngoingTreatmentsByDoctor(int doctorId) {
        String sql = "select * from treatment_details where doctor_id=? and end_time is null";
        return jdbcTemplate.query(sql, mapTreatmentToDb(), doctorId);
    }

    public List<Treatment> getAllTreatmentsByPatient(int patientId) {
        String sql = "select * from treatment_details where patient_id=?";

        return jdbcTemplate.query(sql, mapTreatmentToDb(), patientId);
    }

    public List<Treatment> getAllOngoingTreatmentsByPatient(int patientId) {
        String sql = "select * from treatment_details where patient_id=? and end_time is null";
        return jdbcTemplate.query(sql, mapTreatmentToDb(), patientId);
    }

    public List<Treatment> filteredTreatmentDetails(String pFname, String pLname, String dFname, String dLname) {
        String sql = "select * from treatment_details where pfname like ? and dfname like ? and plname like ? and plname like ?";
        List<Treatment> treatments = jdbcTemplate.query(sql, mapTreatmentToDb(), "%" + pFname + "%", "%" + dFname + "%", "%" + pLname + "%", "%" + dLname + "%");
        logger.debug("Found " + treatments.size() + " treatments on filtering");
        return treatments;
    }

    private RowMapper<Treatment> mapTreatmentToDb() {
        return (resultSet, i) -> {

            Treatment treatment = new Treatment();
            treatment.setDoctorId(resultSet.getInt("doctor_id"));
            treatment.setPatientId(resultSet.getInt("patient_id"));
            treatment.setTreatment(resultSet.getString("treatment"));
            treatment.setProblem(resultSet.getString("problem"));
            treatment.setStart(resultSet.getDate("start_time"));
            treatment.setEnd(resultSet.getDate("end_time"));
            treatment.setId(resultSet.getInt("treatment_id"));
            treatment.setpFname(resultSet.getString("pfname"));
            treatment.setpLname(resultSet.getString("plname"));
            treatment.setdFname(resultSet.getString("dfname"));
            treatment.setdLname(resultSet.getString("dlname"));

            return treatment;
        };
    }
}
