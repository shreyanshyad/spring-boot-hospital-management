package com.dbms.dbms.dao;

import com.dbms.dbms.dto.Appointment;
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
public class AppointmentDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AppointmentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createAppointment(String email, Appointment appointment) {
        String sql = "insert into appointments(patient_id,doctor_id,time,problem,confirmed) values ((select id from users where email=?),?,?,?,?)";

        Timestamp timestamp = Timestamp.valueOf(appointment.getTime());

        return jdbcTemplate.update(sql, email, appointment.getDoctorId(), timestamp, appointment.getProblem(), appointment.isConfirmed());
    }

    public List<Appointment> getAllAppointmentsForPatient(int patientId) {
        String sql = "select a.*,d.first_name as d_fname, d.last_name as d_lname from appointments a,doctor d where d.id=a.doctor_id and a.patient_id=? order by a.time";
        return jdbcTemplate.query(sql, mapAppointmentFromDb(), patientId);
    }

    public List<Appointment> getAllAppointments() {
        String sql = "select a.*,d.first_name as d_fname, d.last_name as d_lname from appointments a,doctor d where d.id=a.doctor_id order by a.time";
        return jdbcTemplate.query(sql, mapAppointmentFromDb());
    }

    public int deleteAppointmentById(int id) {
        String sql = "delete from appointments where appointment_id=?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Appointment> getUnconfirmedAppointments() {
        String sql = "select a.*,d.first_name as d_fname, d.last_name as d_lname from appointments a,doctor d where d.id=a.doctor_id and a.confirmed=false order by a.time";
        return jdbcTemplate.query(sql, mapAppointmentFromDb());
    }

    public List<Appointment> getConfirmedAppointmentsForDoctor(int doctorId) {
        String sql = "select a.*,d.first_name as d_fname, d.last_name as d_lname from appointments a,patient d where d.id=a.patient_id and a.confirmed=true and a.doctor_id=? order by a.time";
        return jdbcTemplate.query(sql, mapAppointmentFromDb(), doctorId);
    }

    public Appointment getAppointmentById(int id) {
        String sql = "select a.*,d.first_name as d_fname, d.last_name as d_lname from appointments a,doctor d where d.id=a.doctor_id and a.appointment_id=? order by a.time";
        return jdbcTemplate.queryForObject(sql, mapAppointmentFromDb(), id);
    }

    public int confirmAppointment(int id, LocalDateTime time) {
        Timestamp timestamp = Timestamp.valueOf(time);
        String sql = "update appointments set time=?,confirmed=true where appointment_id=?";
        return jdbcTemplate.update(sql, timestamp, id);
    }

    private RowMapper<Appointment> mapAppointmentFromDb() {
        return (resultSet, i) -> {
            Appointment apt = new Appointment();

            apt.setConfirmed(resultSet.getBoolean("confirmed"));
            apt.setId(resultSet.getInt("appointment_id"));
            apt.setPatientId(resultSet.getInt("patient_id"));
            apt.setDocFirstName(resultSet.getString("d_fname"));
            apt.setDocLastName(resultSet.getString("d_lname"));
            apt.setProblem(resultSet.getString("problem"));
            apt.setDoctorId(resultSet.getInt("doctor_id"));
            apt.setTime(resultSet.getTimestamp("time").toLocalDateTime());

            return apt;
        };
    }
}
