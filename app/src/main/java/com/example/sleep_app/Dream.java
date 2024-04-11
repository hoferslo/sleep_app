package com.example.sleep_app;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.sleep_app.sqLiteHelpers.DatabaseHelper;
import com.example.sleep_app.sqLiteHelpers.DreamsHelper;

import java.time.LocalDateTime;

public class Dream implements Parcelable {
    private Long id;
    private String title;
    private int lucidity;
    private int clarity;
    private int happiness;
    private int recurringDream;
    private int nightmare;
    private String description;
    private LocalDateTime dateCreated;

    public Dream(Long id, String title, int lucidity, int clarity, int happiness, int recurringDream, int nightmare, String description, LocalDateTime dateCreated) {
        this.id = id;
        this.title = title;
        this.lucidity = lucidity;
        this.clarity = clarity;
        this.happiness = happiness;
        this.recurringDream = recurringDream;
        this.nightmare = nightmare;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public Dream() {

    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public int getRecurringDream() {
        return recurringDream;
    }

    public boolean isRecurringDream() {
        return recurringDream == 1;
    }

    public boolean isNightmare() {
        return nightmare == 1;
    }

    public void setRecurringDream(int recurringDream) {
        this.recurringDream = recurringDream;
    }

    public int getNightmare() {
        return nightmare;
    }

    public void setNightmare(int nightmare) {
        this.nightmare = nightmare;
    }

    public enum Attribute {
        _id, title, lucidity, clarity, happiness, recurringDream, nightmare, description, dateCreated
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getLucidity() {
        return lucidity;
    }

    public int getClarity() {
        return clarity;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLucidity(int lucidity) {
        this.lucidity = lucidity;
    }

    public void setClarity(int clarity) {
        this.clarity = clarity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    protected Dream(Parcel in) {
        id = in.readLong();
        title = in.readString();
        lucidity = in.readInt();
        clarity = in.readInt();
        happiness = in.readInt();
        recurringDream = in.readInt();
        nightmare = in.readInt();
        description = in.readString();
        // Read dateCreated as String and convert it back to LocalDateTime
        String dateString = in.readString();
        dateCreated = DatabaseHelper.dateTimeStringToDateTime(dateString);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeInt(lucidity);
        dest.writeInt(clarity);
        dest.writeInt(happiness);
        dest.writeInt(recurringDream);
        dest.writeInt(nightmare);
        dest.writeString(description);
        // Write dateCreated as String
        dest.writeString(DreamsHelper.DateTimeToDateTimeString(dateCreated));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dream> CREATOR = new Creator<Dream>() {
        @Override
        public Dream createFromParcel(Parcel in) {
            return new Dream(in);
        }

        @Override
        public Dream[] newArray(int size) {
            return new Dream[size];
        }
    };
}
