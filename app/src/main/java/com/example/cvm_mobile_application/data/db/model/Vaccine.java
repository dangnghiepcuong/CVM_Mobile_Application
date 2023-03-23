package com.example.cvm_mobile_application.data.db.model;

public class Vaccine {
    private String id;
    private String name;
    private String supplier;
    private String technology;
    private String country;

    public Vaccine() {
    }

    public Vaccine(String id, String name, String supplier, String technology, String country) {
        this.id = id;
        this.name = name;
        this.supplier = supplier;
        this.technology = technology;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
