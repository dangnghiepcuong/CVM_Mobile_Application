package com.example.cvm_mobile_application.data.db.model;

public enum Shift {
    SHIFT_1("Sáng", 0),
    SHIFT_2("Trưa", 1),
    SHIFT_3("Tối", 2);
    private String shift;
    private int value;
    Shift(String shift, int value) {
        this.shift = shift;
        this.value = value;
    }

    public String getShift() {
        return shift;
    }

    public int getValue() {
        return value;
    }
}
