package com.example.issac.fitnessandroid;

/**
 * Created by issac on 04/04/18.
 */

public class Rutinas {
    public String uid, tiempo, date, imagenRutina, imagenPerfil, descripcion, fullname;

    public Rutinas(){

    }

    public Rutinas(String uid, String tiempo, String date, String imagenRutina, String imagenPerfil, String descripcion, String fullname) {
        this.uid = uid;
        this.tiempo = tiempo;
        this.date = date;
        this.imagenRutina = imagenRutina;
        this.imagenPerfil = imagenPerfil;
        this.descripcion = descripcion;
        this.fullname = fullname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImagenRutina() {
        return imagenRutina;
    }

    public void setImagenRutina(String imagenRutina) {
        this.imagenRutina = imagenRutina;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
