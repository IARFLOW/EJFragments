package com.example.ejfragments.modelo.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pelicula_ratings")
public class PeliculaRatingEntity {
    @PrimaryKey
    private int peliculaId;
    private float puntuacion;
    private Long fechaEmision; // Almacenamos la fecha como Long (timestamp)

    // Constructor vacío requerido por Room
    public PeliculaRatingEntity() {
    }

    // Constructor con todos los parámetros
    public PeliculaRatingEntity(int peliculaId, float puntuacion, Long fechaEmision) {
        this.peliculaId = peliculaId;
        this.puntuacion = puntuacion;
        this.fechaEmision = fechaEmision;
    }

    // Getters y setters
    public int getPeliculaId() {
        return peliculaId;
    }

    public void setPeliculaId(int peliculaId) {
        this.peliculaId = peliculaId;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Long getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Long fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
}