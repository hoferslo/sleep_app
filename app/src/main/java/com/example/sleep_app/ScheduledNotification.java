package com.example.sleep_app;

public class ScheduledNotification {
    private String title;
    private String description;
    private int hourOfDay;
    private int minute;
    private boolean active; // New field

    public ScheduledNotification(String title, String description, int hourOfDay, int minute) {
        this.title = title;
        this.description = description;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.active = true; // Default to active
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isActive() {
        return active;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
