package com.example.cvm_mobile_application.data.db.model;

import java.sql.Date;

public class Notification {
    private String id;
    private String title;
    private String content;
    private Date onDate;

    public Notification() {
    }

    public Notification(String id, String title, String content, Date onDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.onDate = onDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
