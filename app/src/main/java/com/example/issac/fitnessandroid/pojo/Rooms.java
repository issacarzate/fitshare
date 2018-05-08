package com.example.issac.fitnessandroid.pojo;

/**
 * Created by issac on 15/04/18.
 */

public class Rooms {
    public String roomName, roomDescription;
    public boolean glasses, helmet;

    public boolean isGlasses() {
        return glasses;
    }
    public boolean isHelmet() {
        return helmet;
    }
    public void setHelmet(boolean helmet) {
        this.helmet = helmet;
    }

    public void setGlasses(boolean glasses) {
        this.glasses = glasses;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    @Override
    public String toString() {
        if(glasses==true && helmet==true){
            return "En " + roomName +  " deben traer gafas y casco " +"👷‍ 😎";
        }
        
        if (glasses==true){
            return "En " + roomName + " deben traer gafas " + "😎";
        }

        if(helmet==true){
            return "En " + roomName + " deben traer casco " + "👷‍️";
        }else {
            return roomName + " Libre de accesorios de seguridad" + "😃";
        }
    }
}
