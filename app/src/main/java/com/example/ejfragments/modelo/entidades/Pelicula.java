package com.example.ejfragments.modelo.entidades;

import java.util.Date;
import java.util.List;

public class Pelicula {

    private int id;
    private String nombre;
    private Date fecha;
    private String sinopsis;
    private String genero;
    private String  imagen;
    private List<Actor> actores;

    public Pelicula(int id, String nombre, Date fecha, String sinopsis, String genero, String imagen, List<Actor> actores) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.sinopsis = sinopsis;
        this.genero = genero;
        this.imagen = imagen;
        this.actores = actores;
    }

    public Pelicula(int id, String nombre, Date fecha, String sinopsis, String genero, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.sinopsis = sinopsis;
        this.genero = genero;
        this.imagen = imagen;
    }

    public Pelicula(String nombre, Date fecha, String sinopsis, String genero, String imagen) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.sinopsis = sinopsis;
        this.genero=genero;
        this.imagen=imagen;
    }

    public Pelicula() {
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getSinopsis() {
        return sinopsis;
    }


    public String getGenero() {
        return genero;
    }


    public String getImagen() {
        return imagen;
    }


    public List<Actor> getActores() {
        return actores;
    }

    public void setActores(List<Actor> actores) {
        this.actores = actores;
    }
}
