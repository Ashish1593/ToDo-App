package com.example.manisharana.todoapp.Models;

import java.io.Serializable;

public class Task implements Serializable{
    private String title;
    private String id;
    private long date;
    private boolean status;
    private String assignByName;
    private String assignByPhone;
    private String assignToName;
    private String assignToPhone;


    public Task(String title, String id, long date, boolean status, String assignByName, String assignByPhone, String assignToName, String assignToPhone) {
        this.title = title;
        this.id = id;
        this.date = date;
        this.status = status;
        this.assignByName = assignByName;
        this.assignByPhone = assignByPhone;
        this.assignToName = assignToName;
        this.assignToPhone = assignToPhone;
    }

    public Task(String title, long date, boolean status, String assignByName, String assignByPhone, String assignToName, String assignToPhone) {
        this.title = title;
        this.date = date;
        this.status = status;
        this.assignByName = assignByName;
        this.assignByPhone = assignByPhone;
        this.assignToName = assignToName;
        this.assignToPhone = assignToPhone;
    }

    public Task() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAssignByName() {
        return assignByName;
    }

    public void setAssignByName(String assignByName) {
        this.assignByName = assignByName;
    }

    public String getAssignByPhone() {
        return assignByPhone;
    }

    public void setAssignByPhone(String assignByPhone) {
        this.assignByPhone = assignByPhone;
    }

    public String getAssignToName() {
        return assignToName;
    }

    public void setAssignToName(String assignToName) {
        this.assignToName = assignToName;
    }

    public String getAssignToPhone() {
        return assignToPhone;
    }

    public void setAssignToPhone(String assignToPhone) {
        this.assignToPhone = assignToPhone;
    }

    @Override
    public String toString() {
        return "Task name: "+getTitle();
    }
}
