package com.example.issac.fitnessandroid.pojo;

/**
 * Created by issac on 15/04/18.
 */

public class Groups {
   public String name, id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Nombre: " + name;
    }
}

