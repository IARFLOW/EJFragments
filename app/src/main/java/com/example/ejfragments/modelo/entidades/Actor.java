package com.example.ejfragments.modelo.entidades;

import java.util.Date;

public class Actor {
    private int id;
    private String nombre;
    private Date fechaNacimiento;
    private String foto;

    public Actor() {
    }

    public Actor(int id, String nombre, Date fechaNacimiento, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFoto() {
        return foto;
    }

}
