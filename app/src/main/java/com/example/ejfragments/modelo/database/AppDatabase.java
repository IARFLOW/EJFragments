package com.example.ejfragments.modelo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ejfragments.modelo.dao.ActorDao;
import com.example.ejfragments.modelo.dao.PeliculaRatingDao;
import com.example.ejfragments.modelo.entidades.ActorEntity;
import com.example.ejfragments.modelo.entidades.PeliculaRatingEntity;

@Database(entities = {ActorEntity.class, PeliculaRatingEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ActorDao actorDao();
    public abstract PeliculaRatingDao peliculaRatingDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}