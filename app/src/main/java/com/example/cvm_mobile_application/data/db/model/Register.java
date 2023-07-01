package com.example.cvm_mobile_application.data.db.model;

import android.annotation.SuppressLint;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register {
    private int id;
    private String schedule_id;
    private String shift;
    private int number_order;
    private int status;
    private Schedule schedule;
    private String citizen_id;
    private String citizen_name;
    private Timestamp on_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

//    public String getTime() {
//        switch (time){
//            case 0:
//                return "Sáng";
//            case 1:
//                return "Trưa";
//            case 2:
//                return "Tối";
//        }
//        return "";
//    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public int getNumber_order() {
        return number_order;
    }

    public void setNumber_order(int number_order) {
        this.number_order = number_order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getCitizen_id() {
        return citizen_id;
    }

    public void setCitizen_id(String citizen_id) {
        this.citizen_id = citizen_id;
    }

    public String getCitizen_name() {
        return citizen_name;
    }

    public void setCitizen_name(String citizen_name) {
        this.citizen_name = citizen_name;
    }

    public Timestamp getOn_date() {
        return on_date;
    }

    public void setOn_date(Timestamp on_date) {
        this.on_date = on_date;
    }


    public String getOnDateString() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = on_date.toDate();
        return df.format(date);
    }
}
