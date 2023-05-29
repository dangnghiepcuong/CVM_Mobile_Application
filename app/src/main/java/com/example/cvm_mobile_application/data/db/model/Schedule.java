package com.example.cvm_mobile_application.data.db.model;

public class Schedule {
    private String id;
    private String onDate;
    private String serial;
    private int limitDay;
    private int limitNoon;
    private int limitNight;
    private int dayRegistered;
    private int noonRegistered;
    private int nightRegistered;
    private String orgId;
    private String vaccineId;

    public Schedule() {
    }

    public Schedule(String id, String onDate, String serial, int limitDay, int limitNoon, int limitNight, int dayRegistered, int noonRegistered, int nightRegistered, String orgId, String vaccineId) {
        this.id = id;
        this.onDate = onDate;
        this.serial = serial;
        this.limitDay = limitDay;
        this.limitNoon = limitNoon;
        this.limitNight = limitNight;
        this.dayRegistered = dayRegistered;
        this.noonRegistered = noonRegistered;
        this.nightRegistered = nightRegistered;
        this.orgId = orgId;
        this.vaccineId = vaccineId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOnDate() {
        return onDate;
    }

    public void setOnDate(String onDate) {
        this.onDate = onDate;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(int limitDay) {
        this.limitDay = limitDay;
    }

    public int getLimitNoon() {
        return limitNoon;
    }

    public void setLimitNoon(int limitNoon) {
        this.limitNoon = limitNoon;
    }

    public int getLimitNight() {
        return limitNight;
    }

    public void setLimitNight(int limitNight) {
        this.limitNight = limitNight;
    }

    public int getDayRegistered() {
        return dayRegistered;
    }

    public void setDayRegistered(int dayRegistered) {
        this.dayRegistered = dayRegistered;
    }

    public int getNoonRegistered() {
        return noonRegistered;
    }

    public void setNoonRegistered(int noonRegistered) {
        this.noonRegistered = noonRegistered;
    }

    public int getNightRegistered() {
        return nightRegistered;
    }

    public void setNightRegistered(int nightRegistered) {
        this.nightRegistered = nightRegistered;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId  ;
    }

    public String getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(String vaccineId) {
        this.vaccineId = vaccineId;
    }
}
