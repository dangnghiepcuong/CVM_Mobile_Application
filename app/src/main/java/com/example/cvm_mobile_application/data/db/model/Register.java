package com.example.cvm_mobile_application.data.db.model;

public class Register {
    private int id;
    private String doseType;
    private int time;
    private int numOrder;
    private int status;
    private Schedule schedule;
    private Citizen citizen;

    public Register() {
    }

    public Register(int id, String doseType, int time, int numOrder, int status, Schedule schedule, Citizen citizen) {
        this.id = id;
        this.doseType = doseType;
        this.time = time;
        this.numOrder = numOrder;
        this.status = status;
        this.schedule = schedule;
        this.citizen = citizen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoseType() {
        return doseType;
    }

    public void setDoseType(String doseType) {
        this.doseType = doseType;
    }

    public String getTime() {
        switch (time){
            case 0:
                return "Sáng";
            case 1:
                return "Trưa";
            case 2:
                return "Tối";
        }
        return "";
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNumOrder() {
        return numOrder;
    }

    public void setNumOrder(int numOrder) {
        this.numOrder = numOrder;
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

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }
}
