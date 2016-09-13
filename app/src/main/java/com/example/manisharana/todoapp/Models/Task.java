package com.example.manisharana.todoapp.Models;
import com.example.manisharana.todoapp.Adapters.Utility;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable {
    private String title = "";
    private String id = "";
    private String date = "";
    private ArrayList<Comment> comments = null;
    private boolean status = true;
    private String assignByName = "";
    private String assignByPhone = "";
    private String assignToName = "";
    private String assignToPhone = "";

    //test by removing d fields
    public Task() {
        this.title = "";
        this.id = "";
        this.date = "";
        this.status = true;
        this.comments = null;
        this.assignByName = "";
        this.assignByPhone = "";
        this.assignToName = "";
        this.assignToPhone = "";
    }


    public Task(String title, String id, String date, boolean status, ArrayList<Comment> comments, String assignByName, String assignByPhone, String assignToName, String assignToPhone) {
        this.title = title;
        this.id = id;
        this.date = date;
        this.status = status;
        this.comments = comments;
        this.assignByName = assignByName;
        this.assignByPhone = assignByPhone;
        this.assignToName = assignToName;
        this.assignToPhone = assignToPhone;
    }


    public Task(String title, String date, boolean status, ArrayList<Comment> comments, String assignByName, String assignByPhone, String assignToName, String assignToPhone) {
        this.title = title;
        this.id = "";
        this.date = date;
        this.status = status;
        this.comments = comments;
        this.assignByName = assignByName;
        this.assignByPhone = assignByPhone;
        this.assignToName = assignToName;
        this.assignToPhone = assignToPhone;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
