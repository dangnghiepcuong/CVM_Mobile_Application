package com.example.cvm_mobile_application.data.db.model;

import java.sql.Date;

public class Notification {
    private String userid;
    private String title;
    private String content;
    private Date onDate;
    private boolean read;

    public Notification() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String id) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
