package com.example.cvm_mobile_application.data.db.model;

public class Organization {
    private String id;
    private String name;
    private String province;
    private String district;
    private String town;
    private String street;

    public Organization() {
    }

    public Organization(String id, String name, String province, String district, String town, String street) {
        this.id = id;
        this.name = name;
        this.province = province;
        this.district = district;
        this.town = town;
        this.street = street;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
