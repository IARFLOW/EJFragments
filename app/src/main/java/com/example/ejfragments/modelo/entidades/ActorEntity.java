package com.example.ejfragments.modelo.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "actores")
public class ActorEntity {
    @PrimaryKey
    private int id;
    private String nombre;
    private Long fechaNacimiento;
    private String foto;

    public ActorEntity() {
    }

    public ActorEntity(int id, String nombre, Long fechaNacimiento, String foto) {
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

    public Actor toActor() {
        Date fecha = null;
        if (fechaNacimiento != null) {
            fecha = new Date(fechaNacimiento);
        }

        return new Actor(id, nombre, fecha, foto);
    }

    public static ActorEntity fromActor(Actor actor) {
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