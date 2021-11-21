package com.dbms.dbms.dao;

import com.dbms.dbms.dto.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class PatientDao {
    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(PatientDao.class);

    @Autowired
    public PatientDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int updatePatient(String email, Patient patient) {
        String sql = "update patient p,users u " +
                "set p.first_name=? " +
                ",p.middle_name=? " +
                ",p.last_name=? " +
                ",p.address=? " +
                ",p.age=? " +
                ",p.gender=? " +
                ",p.dob=? " +
                "where p.id=u.id and u.email=?";

        int res = jdbcTemplate.update(sql, patient.getFirstName(), patient.getMiddleName(),
                patient.getLastName(), patient.getAddress(), patient.getAge(), patient.getGender(), patient.getDob(), email);
        logger.debug("Update patient " + email + ".Result: " + res);
        return res;
    }

    public int createPatient(String email, Patient patient) {
        String sql = "insert into patient(id,first_name,middle_name,last_name,address,age,gender,dob) "
                + "values ((select id from users where email=?),?,?,?,?,?,?,?);";

        int res = jdbcTemplate.update(sql, email, patient.getFirstName(), patient.getMiddleName(),
                patient.getLastName(), patient.getAddress(), patient.getAge(), patient.getGender(), patient.getDob());

        logger.debug("Create patient " + email + ".Result: " + res);
        return res;
    }

    public List<Patient> selectAllPatients() {
        String sql = "select p.* from patient p,users u where u.id=p.id";

        return jdbcTemplate.query(sql, mapPatientFromDb());
    }

    public Patient findPatientByEmail(String email) {
        String sql = "select p.* from patient p,users u where u.id=p.id and u.email=?";

        try {
            return jdbcTemplate.queryForObject(sql, mapPatientFromDb(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    private RowMapper<Patient> mapPatientFromDb() {
        return (resultSet, i) -> {

            String firstName = resultSet.getString("first_name");
            String middleName = resultSet.getString("middle_name");
            String lastName = resultSet.getString("last_name");
            String address = resultSet.getString("address");
            Date dob = resultSet.getDate("dob");
            int age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");

            Patient user = new Patient();

            user.setFirstName(firstName);
            user.setMiddleName(middleName);
            user.setLastName(lastName);
            user.setAddress(address);
            user.setDob(dob);
            user.setAge(age);
            user.setGender(gender);

            return user;
        };
    }
}
