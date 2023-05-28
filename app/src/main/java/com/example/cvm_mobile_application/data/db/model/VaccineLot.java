package com.example.cvm_mobile_application.data.db.model;

public class VaccineLot {
    String orgId;
    String lot;
    String vaccineId;
    String importDate;
    int quantity;
    String expirationDate;

    public VaccineLot() {
    }

    public VaccineLot(String orgId, String lot, String vaccineId, String importDate, int quantity, String expirationDate) {
        this.orgId = orgId;
        this.lot = lot;
        this.vaccineId = vaccineId;
        this.importDate = importDate;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(String vaccineId) {
        this.vaccineId = vaccineId;
    }

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
