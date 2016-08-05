package com.example.manisharana.todoapp.Models;

public class Section {

    private String dayString;

    public Section() {
    }

    public String getDayString() {
        return dayString;
    }

    public void setDayString(String dayString) {
        this.dayString = dayString;
    }

    @Override
    public boolean equals(Object object) {
        super.equals(object);
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            Section section = (Section) object;
            if (this.dayString.equals(section.getDayString())){
                result = true;
            }
        }
        return result;

    }

    @Override
    public int hashCode() {
        super.hashCode();
        int hash = 3;
        hash = 7 * hash + this.dayString.hashCode();
        return hash;
    }
}
