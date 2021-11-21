package com.dbms.dbms.dao;

import com.dbms.dbms.dto.Doctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DoctorDao {
    private static final Logger logger = LoggerFactory.getLogger(DoctorDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DoctorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createDoctor(String email, Doctor doctor) {
        String sql = "insert into doctor(id,first_name,middle_name,last_name,address,phone_no,specializations,qualifications) "
                + "values ((select id from users where email=?),?,?,?,?,?,?,?);";

        int res = jdbcTemplate.update(sql, email, doctor.getFirstName(), doctor.getMiddleName(), doctor.getLastName(),
                doctor.getAddress(), doctor.getPhoneNo(), doctor.getSpecializations(), doctor.getQualifications());

        logger.debug("Create doctor " + email + ".Result: " + res);
        return res;
    }

    public Doctor findDoctorByEmail(String email) {
        String sql = "select p.* from doctor p,users u where u.id=p.id and u.email=?";

        try {
            return jdbcTemplate.queryForObject(sql, mapDoctorFromDb(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Doctor findDoctorById(int id) {
        String sql = "select p.* from doctor p where p.id=?";

        try {
            return jdbcTemplate.queryForObject(sql, mapDoctorFromDb(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Doctor> getAllDoctors() {
        String sql = "select * from doctor";
        return jdbcTemplate.query(sql, mapDoctorFromDb());
    }

    public int updateDoctor(String email, Doctor doctor) {
        String sql = "update doctor p,users u " +
                "set p.first_name=? " +
                ",p.middle_name=? " +
                ",p.last_name=? " +
                ",p.address=? " +
                ",p.phone_no=? " +
                ",p.specializations=? " +
                ",p.qualifications=? " +
                "where p.id=u.id and u.email=?";

        int res = jdbcTemplate.update(sql, doctor.getFirstName(), doctor.getMiddleName(),
                doctor.getLastName(), doctor.getAddress(), doctor.getPhoneNo(), doctor.getSpecializations(), doctor.getQualifications(), email);
        logger.debug("Update " + doctor.getDesignation() + " " + email + ".Result: " + res);
        return res;
    }

    private RowMapper<Doctor> mapDoctorFromDb() {
        return (resultSet, i) -> {
            String firstName = resultSet.getString("first_name");
            String middleName = resultSet.getString("middle_name");
            String lastName = resultSet.getString("last_name");
            String address = resultSet.getString("address");
            String phoneNo = resultSet.getString("phone_no");
            String qualifications = resultSet.getString("qualifications");
            String specializations = resultSet.getString("specializations");
            int id = resultSet.getInt("id");

            Doctor user = new Doctor();

            user.setSpecializations(specializations);
            user.setQualifications(qualifications);
            user.setFirstName(firstName);
            user.setMiddleName(middleName);
            user.setLastName(lastName);
            user.setAddress(address);
            user.setId(id);
            user.setPhoneNo(phoneNo);

            return user;
        };
    }
}
