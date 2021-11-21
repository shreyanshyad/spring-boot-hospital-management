package com.dbms.dbms.service;

import com.dbms.dbms.dao.StaffDao;
import com.dbms.dbms.dao.UserDao;
import com.dbms.dbms.dto.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffService {
    private final StaffDao staffDao;
    private final UserDao userDao;

    @Autowired
    public StaffService(StaffDao staffDao, UserDao userDao) {
        this.staffDao = staffDao;
        this.userDao = userDao;
    }

    public void upsertStaff(String email, String role, Staff staff) {
        staff.setDesignation(matchRoleToStaffDesignation(role));
        if (staffDao.updateStaff(email, staff) == 0) {
            staffDao.createStaff(email, staff);
            userDao.setUserInit(email);
        }
    }

    public Staff findStaffByEmail(String email, String role) {
        return staffDao.findStaffByEmail(email, matchRoleToStaffDesignation(role));
    }

    public String matchRoleToStaffDesignation(String role) {
        switch (role) {
            case "ROLE_NURSE":
                return "nurse";
            case "ROLE_LAB":
                return "lab_staff";
            case "ROLE_CHEMIST":
                return "chemist";
            case "ROLE_ADMIN":
                return "admin";
        }
        throw new RuntimeException("matchRoleToStaffDesignation: Unknown role");
    }
}
