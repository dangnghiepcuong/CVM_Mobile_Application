package com.example.cvm_mobile_application.data.db.model;

public class Register {
    private int id;
    private String dose_type;
    private String shift;
    private int num_order;
    private int status;
    private String schedule_id;
    private String citizen_id;

    public Register() {
    }

    public Register(int id, String dose_type, String shift, int num_order, int status, String schedule_id, String citizen) {
        this.id = id;
        this.dose_type = dose_type;
        this.shift = shift;
        this.num_order = num_order;
        this.status = status;
        this.schedule_id = schedule_id;
        this.citizen_id = citizen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDose_type() {
        return dose_type;
    }

    public void setDose_type(String dose_type) {
        this.dose_type = dose_type;
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

    public int getNum_order() {
        return num_order;
    }

    public void setNum_order(int num_order) {
        this.num_order = num_order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getCitizen_id() {
        return citizen_id;
    }

    public void setCitizen_id(String citizen_id) {
        this.citizen_id = citizen_id;
    }
}
