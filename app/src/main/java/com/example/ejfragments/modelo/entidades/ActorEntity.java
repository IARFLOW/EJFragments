package com.example.ejfragments.modelo.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "actores")
public class ActorEntity {
    @PrimaryKey
    private int id;
    private String nombre;
    private Long fechaNacimiento; // Almacenamos la fecha como Long (timestamp)
    private String foto;

    // Constructor vacío requerido por Room
    public ActorEntity() {
    }

    // Constructor con todos los parámetros
    public ActorEntity(int id, String nombre, Long fechaNacimiento, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.foto = foto;
    }

    // Getters y setters para todos los campos
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

    public Long getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Long fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    // Metodo para convertir de ActorEntity a Actor (para la UI)
    public Actor toActor() {
        // Convertimos el timestamp Long a Date
        Date fecha = null;
        if (fechaNacimiento != null) {
            fecha = new Date(fechaNacimiento);
        }

        return new Actor(id, nombre, fecha, foto);
    }

    // Metodo estático para convertir de Actor a ActorEntity
    public static ActorEntity fromActor(Actor actor) {
        // Convertimos Date a timestamp Long
        Long timestamp = null;
        if (actor.getFechaNacimiento() != null) {
            timestamp = actor.getFechaNacimiento().getTime();
        }

        return new ActorEntity(
                actor.getId(),
                actor.getNombre(),
                timestamp,
                actor.getFoto()
        );
    }
}