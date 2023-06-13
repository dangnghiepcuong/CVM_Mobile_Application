package com.example.cvm_mobile_application.data.db.model;

public enum Shift {
    SHIFT_1("Sáng (S)", 0),
    SHIFT_2("Chiều (C)", 1),
    SHIFT_3("Tối (T)", 2);
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
