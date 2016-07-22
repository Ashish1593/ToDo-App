package com.example.manisharana.todoapp.Models;

public class Task {
    private String title;
    private String description;
    private boolean remindMeFlag;
    private long date;
    private String attachment;
    private State state;
    private TaskTag tag;
    private User user;

    public Task() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRemindMeFlag() {
        return remindMeFlag;
    }

    public void setRemindMeFlag(boolean remindMeFlag) {
        this.remindMeFlag = remindMeFlag;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public TaskTag getTag() {
        return tag;
    }

    public void setTag(TaskTag tag) {
        this.tag = tag;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
