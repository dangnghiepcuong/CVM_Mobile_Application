package com.example.cvm_mobile_application.data.db.model;

import java.sql.Date;

public class Form {
    private int id;
    private String choices;
    private Date filledDate;
    private Citizen citizen;

    public Form() {
    }

    public Form(int id, String choices, Date filledDate, Citizen citizen) {
        this.id = id;
        this.choices = choices;
        this.filledDate = filledDate;
        this.citizen = citizen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public Date getFilledDate() {
        return filledDate;
    }

    public void setFilledDate(Date filledDate) {
        this.filledDate = filledDate;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }
}
