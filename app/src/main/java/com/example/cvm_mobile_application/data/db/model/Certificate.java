package com.example.cvm_mobile_application.data.db.model;

public class Certificate {
    private int numDose;
    private int certType;
    private Citizen citizen;

    public Certificate() {
    }

    public Certificate(int numDose, int certType, Citizen citizen) {
        this.numDose = numDose;
        this.certType = certType;
        this.citizen = citizen;
    }

    public int getNumDose() {
        return numDose;
    }

    public void setNumDose(int numDose) {
        this.numDose = numDose;
    }

    public int getCertType() {
        return certType;
    }

    public void setCertType(int certType) {
        this.certType = certType;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }
}
