package com.example.cvm_mobile_application.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Organization implements Parcelable {
    private String id;
    private String name;
    private String province_name;
    private String district_name;
    private String ward_name;
    private String street;
    private String contact;
    private byte nSchedules;

    public Organization() {
        id = "";
        name = "";
        province_name = "";
        district_name = "";
        ward_name = "";
        street = "";
        contact = "";
        nSchedules = 0;
    }

    protected Organization(Parcel in) {
        id = in.readString();
        name = in.readString();
        province_name = in.readString();
        district_name = in.readString();
        ward_name = in.readString();
        street = in.readString();
        contact = in.readString();
        nSchedules = in.readByte();
    }

    public static final Creator<Organization> CREATOR = new Creator<Organization>() {
        @Override
        public Organization createFromParcel(Parcel in) {
            return new Organization(in);
        }

        @Override
        public Organization[] newArray(int size) {
            return new Organization[size];
        }
    };

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

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getWard_name() {
        return ward_name;
    }

    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    public byte getNSchedules() {
        return nSchedules;
    }

    public void setNSchedules(byte nSchedules) {
        this.nSchedules = nSchedules;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(province_name);
        parcel.writeString(district_name);
        parcel.writeString(ward_name);
        parcel.writeString(street);
        parcel.writeString(contact);
        parcel.writeByte(nSchedules);
    }
}
