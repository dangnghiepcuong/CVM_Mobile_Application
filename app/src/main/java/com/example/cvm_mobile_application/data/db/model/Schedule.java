package com.example.cvm_mobile_application.data.db.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Schedule {
    private String id;
    private Date on_date;
    private String lot;
    private int limit_day;
    private int limit_noon;
    private int limit_night;
    private int day_registered;
    private int noon_registered;
    private int night_registered;
    private String org_id;
    private String vaccine_id;

    public Schedule() {
    }

    public Schedule(String id, String onDateString, String lot,
                    int limit_day, int limit_noon, int limit_night,
                    int day_registered, int noon_registered, int night_registered,
                    String org_id, String vaccine_id) {
        this.id = id;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date onDate = null;
        try {
            onDate = df.parse(onDateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.on_date = onDate;

        this.lot = lot;
        this.limit_day = limit_day;
        this.limit_noon = limit_noon;
        this.limit_night = limit_night;
        this.day_registered = day_registered;
        this.noon_registered = noon_registered;
        this.night_registered = night_registered;
        this.org_id = org_id;
        this.vaccine_id = vaccine_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public java.util.Date getOn_date() {
        return on_date;
    }

    public void setOn_date(java.util.Date on_date) {
        this.on_date = on_date;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public int getLimit_day() {
        return limit_day;
    }

    public void setLimit_day(int limit_day) {
        this.limit_day = limit_day;
    }

    public int getLimit_noon() {
        return limit_noon;
    }

    public void setLimit_noon(int limit_noon) {
        this.limit_noon = limit_noon;
    }

    public int getLimit_night() {
        return limit_night;
    }

    public void setLimit_night(int limit_night) {
        this.limit_night = limit_night;
    }

    public int getDay_registered() {
        return day_registered;
    }

    public void setDay_registered(int day_registered) {
        this.day_registered = day_registered;
    }

    public int getNoon_registered() {
        return noon_registered;
    }

    public void setNoon_registered(int noon_registered) {
        this.noon_registered = noon_registered;
    }

    public int getNight_registered() {
        return night_registered;
    }

    public void setNight_registered(int night_registered) {
        this.night_registered = night_registered;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getVaccine_id() {
        return vaccine_id;
    }

    public void setVaccine_id(String vaccine_id) {
        this.vaccine_id = vaccine_id;
    }
}
