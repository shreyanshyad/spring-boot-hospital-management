package com.dbms.dbms.dto;

import java.sql.Date;

public class Treatment {
    int id, patientId, doctorId;
    Date start, end;
    String problem, treatment;
    String pFname, pLname, dFname, dLname;

    public String getpFname() {
        return pFname;
    }

    public void setpFname(String pFname) {
        this.pFname = pFname;
    }

    public String getpLname() {
        return pLname;
    }

    public void setpLname(String pLname) {
        this.pLname = pLname;
    }

    public String getdFname() {
        return dFname;
    }

    public void setdFname(String dFname) {
        this.dFname = dFname;
    }

    public String getdLname() {
        return dLname;
    }

    public void setdLname(String dLname) {
        this.dLname = dLname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
}
