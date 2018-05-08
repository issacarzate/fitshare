package com.example.issac.fitnessandroid.pojo;

/**
 * Created by issac on 15/04/18.
 */

public class Person {
    public String id, name, groupId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Nombre: " + name;
    }
}
