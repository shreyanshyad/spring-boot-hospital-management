package com.dbms.dbms.dao;

import com.dbms.dbms.dto.Staff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StaffDao {
    private static final Logger logger = LoggerFactory.getLogger(StaffDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StaffDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createStaff(String email, Staff staff) {
        String sql = "insert into " + staff.getDesignation() + "(id,first_name,middle_name,last_name,address,phone_no) "
                + "values ((select id from users where email=?),?,?,?,?,?);";

        int res = jdbcTemplate.update(sql, email, staff.getFirstName(), staff.getMiddleName(), staff.getLastName(),
                staff.getAddress(), staff.getPhoneNo());

        logger.debug("Create " + staff.getDesignation() + " " + email + ".Result: " + res);
        return res;
    }

    public Staff findStaffByEmail(String email, String designation) {
        String sql = "select p.* from " + designation + " p,users u where u.id=p.id and u.email=?";

        try {
            return jdbcTemplate.queryForObject(sql, mapStaffFromDb(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int updateStaff(String email, Staff staff) {
        String sql = "update " + staff.getDesignation() + " p,users u " +
                "set p.first_name=? " +
                ",p.middle_name=? " +
                ",p.last_name=? " +
                ",p.address=? " +
                ",p.phone_no=? " +
                "where p.id=u.id and u.email=?";

        int res = jdbcTemplate.update(sql, staff.getFirstName(), staff.getMiddleName(),
                staff.getLastName(), staff.getAddress(), staff.getPhoneNo(), email);
        logger.debug("Update " + staff.getDesignation() + " " + email + ".Result: " + res);
        return res;
    }

    private RowMapper<Staff> mapStaffFromDb() {
        return (resultSet, i) -> {
            String firstName = resultSet.getString("first_name");
            String middleName = resultSet.getString("middle_name");
            String lastName = resultSet.getString("last_name");
            String address = resultSet.getString("address");
            String phoneNo = resultSet.getString("phone_no");

            Staff user = new Staff();

            user.setFirstName(firstName);
            user.setMiddleName(middleName);
            user.setLastName(lastName);
            user.setAddress(address);
            user.setPhoneNo(phoneNo);

            return user;
        };
    }
}
