package com.dbms.dbms.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class AppointmentConfirm {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime time;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public AppointmentConfirm(LocalDateTime time) {
        this.time = time;
    }
}
