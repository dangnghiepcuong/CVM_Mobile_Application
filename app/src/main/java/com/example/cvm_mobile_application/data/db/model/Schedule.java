package com.example.cvm_mobile_application.data.db.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Schedule implements Parcelable {
    private String id;
    private Timestamp on_date;
    private String lot;
    private int limit_day;
    private int limit_noon;
    private int limit_night;
    private int day_registered;
    private int noon_registered;
    private int night_registered;
    private Organization org;
    private String org_id;
    private String vaccine_id;

    public Schedule() {
        id = "";
        on_date = new Timestamp(new Date());
        lot = "";
        limit_day = 0;
        limit_noon = 0;
        limit_night = 0;
        day_registered = 0;
        noon_registered = 0;
        night_registered = 0;
        vaccine_id = "";
    }

    public Schedule(String id, String onDateString, String lot,
                    int limit_day, int limit_noon, int limit_night,
                    int day_registered, int noon_registered, int night_registered,
                    Organization org, String vaccine_id) {
        this.id = id;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date onDate = null;
        try {
            onDate = df.parse(onDateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.on_date = new Timestamp(Objects.requireNonNull(onDate));

        this.lot = lot;
        this.limit_day = limit_day;
        this.limit_noon = limit_noon;
        this.limit_night = limit_night;
        this.day_registered = day_registered;
        this.noon_registered = noon_registered;
        this.night_registered = night_registered;
        this.org = org;
        this.vaccine_id = vaccine_id;
    }

    protected Schedule(Parcel in) {
        id = in.readString();

        String dateString = in.readString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date onDate = null;
        try {
            onDate = df.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        on_date = new Timestamp(Objects.requireNonNull(onDate));

        lot = in.readString();
        limit_day = in.readInt();
        limit_noon = in.readInt();
        limit_night = in.readInt();
        day_registered = in.readInt();
        noon_registered = in.readInt();
        night_registered = in.readInt();
        org_id = in.readString();
        vaccine_id = in.readString();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getOn_date() {
        return on_date;
    }

    public void setOn_date(Timestamp on_date) {
        this.on_date = on_date;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public int getLimit_day() {
        return limit_day;
    }

    public void setLimit_day(int limit_day) {
        this.limit_day = limit_day;
    }

    public int getLimit_noon() {
        return limit_noon;
    }

    public void setLimit_noon(int limit_noon) {
        this.limit_noon = limit_noon;
    }

    public int getLimit_night() {
        return limit_night;
    }

    public void setLimit_night(int limit_night) {
        this.limit_night = limit_night;
    }

    public int getDay_registered() {
        return day_registered;
    }

    public void setDay_registered(int day_registered) {
        this.day_registered = day_registered;
    }

    public int getNoon_registered() {
        return noon_registered;
    }

    public void setNoon_registered(int noon_registered) {
        this.noon_registered = noon_registered;
    }

    public int getNight_registered() {
        return night_registered;
    }

    public void setNight_registered(int night_registered) {
        this.night_registered = night_registered;
    }

    public Organization getOrg() {
        return org;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public void setOrg(Organization org) {
        this.org = org;
    }

    public String getVaccine_id() {
        return vaccine_id;
    }

    public void setVaccine_id(String vaccine_id) {
        this.vaccine_id = vaccine_id;
    }

    public String getOnDateString() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = on_date.toDate();
        return df.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);

        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String onDate = df.format(on_date.toDate());
        parcel.writeString(onDate);
        parcel.writeString(lot);
        parcel.writeInt(limit_day);
        parcel.writeInt(limit_noon);
        parcel.writeInt(limit_night);
        parcel.writeInt(day_registered);
        parcel.writeInt(noon_registered);
        parcel.writeInt(night_registered);
        parcel.writeString(org_id);
        parcel.writeString(vaccine_id);
    }
}
