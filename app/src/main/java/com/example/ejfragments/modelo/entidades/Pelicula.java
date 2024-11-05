package com.example.ejfragments.modelo.entidades;

import java.util.Date;

public class Pelicula {

    private String nombre;
    private Date fecha;
    private String sinopsis;
    private String genero;
    private String  imagen;

    public Pelicula(String nombre, Date fecha, String sinopsis, String genero, String imagen) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.sinopsis = sinopsis;
        this.genero=genero;
        this.imagen=imagen;
    }

    public Pelicula() {
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

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
