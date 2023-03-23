package com.example.cvm_mobile_application.data.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DAO extends SQLiteOpenHelper {

    public static final String col_CITIZEN_ID = "CitizenID";
    public static final String col_LAST_NAME = "LastName";
    public static final String col_FIRST_NAME = "FirstName";
    public static final String col_BIRTHDAY = "Birthday";
    public static final String col_GENDER = "Gender";
    public static final String col_TOWN = "Town";
    public static final String col_HOME_TOWN = "Hometown";
    public static final String col_PROVINCE = "Province";
    public static final String col_DISTRICT = "District";
    public static final String col_STREET = "Street";
    public static final String col_PHONE = "Phone";
    public static final String col_EMAIL = "Email";
    public static final String col_ORG_ID = "OrgID";
    public static final String col_ORG_NAME = "OrgName";
    public static final String col_USERNAME = "Username";
    public static final String col_PASSWORD = "Password";
    public static final String col_ROLE = "Role";
    public static final String col_STATUS = "Status";
    public static final String col_VACCINE_ID = "VaccineID";
    public static final String col_VACCINE_NAME = "VaccineName";
    public static final String col_SUPLIER = "Suplier";
    public static final String col_TECHNOLOGY = "Technology";
    public static final String col_COUNTRY = "Country";
    public static final String col_FORM_ID = "FormID";
    public static final String col_NUM_DOSE = "NumDose";
    public static final String col_FORM_CHOICES = "FormChoices";
    public static final String col_FILLED_DATE = "FilledDate";
    public static final String col_CERT_TYPE = "CertType";
    public static final String col_NOTI_ID = "NewsID";
    public static final String col_NOTI_TITLE = "Title";
    public static final String col_NOTI_CONTENT = "Content";
    public static final String col_ON_DATE = "OnDate";
    public static final String col_SCHED_ID = "SchedID";
    public static final String col_VACCINE_SERIAL = "VaccineSerial";
    public static final String col_LIMIT_DAY = "LimitDay";
    public static final String col_LIMIT_NOON = "LimitNoon";
    public static final String col_LIMIT_NIGHT = "LimitNight";
    public static final String col_DAY_REGISTERED = "DayRegistered";
    public static final String col_NOON_REGISTERED = "NoonRegistered";
    public static final String col_NIGHT_REGISTERED = "NightRegistered";
    public static final String col_ID = "ID";
    public static final String col_DOSE_TYPE = "DoseType";
    public static final String col_TIME = "Time";
    public static final String col_NO = "NumOrder";
    public static final String col_IMAGE = "Image";
    public static final String col_GUARDIAN = "Guardian";
    public static final String col_INJ_NO = "InjNo";

    public DAOs(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "cvm.db", factory, 1);
    }

    public DAOs(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DAOs(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    //called the first time a database is accessed. DDL expressed here
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE CITIZEN(" +
                col_CITIZEN_ID + " text," +
                col_LAST_NAME + " text," +
                col_FIRST_NAME + " text," +
                col_BIRTHDAY + " text," +
                col_GENDER + " text," +
                col_HOME_TOWN + " text," +
                col_PROVINCE + " text," +
                col_DISTRICT + " text," +
                col_TOWN + " text," +
                col_STREET + " text," +
                col_PHONE + " text," +
                col_EMAIL + " text," +
                col_GUARDIAN + " text," +
                "primary key (" + col_CITIZEN_ID + ")" +
                ")";

        db.execSQL(createTableStatement);

        createTableStatement = "CREATE TABLE ORGANIZATION (" +
                col_ORG_ID + " text," +
                col_ORG_NAME + " text," +
                col_PROVINCE + " text," +
                col_DISTRICT + " text," +
                col_TOWN + " text," +
                col_STREET + " text," +
                "primary key (" + col_ORG_ID + ")" +
                ")";

        createTableStatement = "CREATE TABLE ACCOUNT (" +
                col_USERNAME + " text," +
                col_PASSWORD + " text," +
                col_ROLE + " integer," +
                col_STATUS + " integer," +
                "primary key (" + col_USERNAME + ")" +
                ")";

        createTableStatement = "CREATE TABLE VACCINE(" +
                col_VACCINE_ID + " text," +
                col_VACCINE_NAME + " text," +
                col_SUPLIER + " text," +
                col_TECHNOLOGY + " text," +
                col_COUNTRY + " text," +
                "primary key (" + col_VACCINE_ID + ")" +
                ")";

        createTableStatement = "CREATE TABLE STATISTIC(" +
                "StatID text," +
                col_NOTI_TITLE + " text," +
                "Date integer," +
                "UpdateOn text," +
                "primary key (StatID, UpdateOn)" +
                ")";

        createTableStatement = "CREATE TABLE FORM(" +
                col_FORM_ID + " integer," +
                col_CITIZEN_ID + " text," +
                col_FORM_CHOICES + " text," +
                col_FILLED_DATE + " text," +
                "primary key (" + col_FORM_ID + ", " + col_CITIZEN_ID + ")" +
                ")";

        createTableStatement = "CREATE TABLE CERTIFICATE (" +
                col_CITIZEN_ID + " text," +
                col_NUM_DOSE + " integer," +
                col_CERT_TYPE + " integer," +
                "primary key (" + col_CITIZEN_ID + ")" +
                ")";

        createTableStatement = "CREATE TABLE NOTIFICATION (" +
                col_NOTI_ID + " integer," +
                col_NOTI_TITLE + " text," +
                col_NOTI_CONTENT + " text," +
                col_ON_DATE + " text," +
                "primary key (" + col_NOTI_ID + ", " + col_ORG_ID + ")" +
                ")";

        createTableStatement = "CREATE TABLE SCHEDULE (" +
                col_SCHED_ID + " text," +
                col_ORG_ID + " text," +
                col_ON_DATE + " text," +
                col_VACCINE_ID+ " text," +
                col_VACCINE_SERIAL + " text," +
                col_LIMIT_DAY + " integer," +
                col_LIMIT_NOON + " integer," +
                col_LIMIT_NIGHT + " integer," +
                col_DAY_REGISTERED + " integer," +
                col_NOON_REGISTERED + " integer," +
                col_NIGHT_REGISTERED + " integer," +
                "primary key (" + col_SCHED_ID + ")" +
                ")";

        createTableStatement = "CREATE TABLE REGISTER (" +
                col_ID + " integer," +
                col_CITIZEN_ID + " text," +
                col_SCHED_ID + " text," +
                col_DOSE_TYPE + " text," +
                col_TIME + " integer," +
                col_NO + " integer," +
                col_STATUS + " integer," +
                col_IMAGE + " blob," +
                "primary key (" + col_ID + ", " + col_CITIZEN_ID + ", " + col_SCHED_ID + ")" +
                ")";

        createTableStatement = "CREATE TABLE INJECTION (" +
                col_INJ_NO + " integer," +
                col_CITIZEN_ID + " text," +
                col_SCHED_ID + " text," +
                col_DOSE_TYPE + " text," +
                "primary key (" + col_INJ_NO + ", " + col_CITIZEN_ID + ")" +
                ")";
    }

    //
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
