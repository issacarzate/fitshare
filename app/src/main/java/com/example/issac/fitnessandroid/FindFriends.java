package com.example.issac.fitnessandroid;


public class FindFriends {
    public String fullname, imagenPerfil, estado;

    public FindFriends(){}

    public FindFriends(String fullname, String imagenPerfil) {
        this.fullname = fullname;
        this.imagenPerfil = imagenPerfil;
        this.estado = estado;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public String getStatus() {
        return estado;
    }

    public void setStatus(String estado) {
        this.estado = estado;
    }

}
