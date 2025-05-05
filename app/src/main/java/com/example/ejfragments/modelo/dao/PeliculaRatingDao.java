package com.example.ejfragments.modelo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ejfragments.modelo.entidades.PeliculaRatingEntity;

@Dao
public interface PeliculaRatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PeliculaRatingEntity peliculaRating);

    @Update
    void update(PeliculaRatingEntity peliculaRating);

    @Query("SELECT * FROM pelicula_ratings WHERE peliculaId = :peliculaId")
    PeliculaRatingEntity getRatingByPeliculaId(int peliculaId);
}