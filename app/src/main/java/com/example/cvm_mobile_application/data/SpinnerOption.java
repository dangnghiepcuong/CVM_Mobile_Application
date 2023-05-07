package com.example.cvm_mobile_application.data;

public class SpinnerOption {
    private String option;
    private String value;

    public SpinnerOption() {
    }

    public SpinnerOption(String option, String value) {
        this.option = option;
        this.value = value;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
