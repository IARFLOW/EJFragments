package com.example.ejfragments.modelo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ejfragments.modelo.entidades.ActorEntity;

import java.util.List;

@Dao
public interface ActorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ActorEntity actor);

    @Update
    void update(ActorEntity actor);

    @Query("SELECT * FROM actores WHERE id = :actorId")
    ActorEntity getActorById(int actorId);

    @Query("SELECT * FROM actores")
    List<ActorEntity> getAllActors();
}