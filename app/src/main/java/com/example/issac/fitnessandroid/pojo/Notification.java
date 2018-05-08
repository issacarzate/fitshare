package com.example.issac.fitnessandroid.pojo;

/**
 * Created by issac on 15/04/18.
 */

public class Notification {
    public String id, Description, Date, RoomId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    @Override
    public String toString() {
        return "Description: " + Description + '\n' +  "Fecha: " + Date ;
    }
}
