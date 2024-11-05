package com.example.ejfragments.mock;



import com.example.ejfragments.modelo.entidades.Pelicula;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ObtencionDatos {

    public ArrayList<Pelicula> obtenerListadoPeliculas(){
        ArrayList<Pelicula> listaPeliculas = new ArrayList<>();

        listaPeliculas.add(new Pelicula("Inception", crearFecha(2010, 7, 16), "Un ladrón que roba secretos corporativos usando tecnología de sueños.", "Ciencia ficción", "inception.jpg"));
        listaPeliculas.add(new Pelicula("The Dark Knight", crearFecha(2008, 7, 18), "Batman se enfrenta al Joker, un criminal psicótico en Gotham.", "Acción", "dark_knight.jpg"));
        listaPeliculas.add(new Pelicula("Interstellar", crearFecha(2014, 11, 7), "Un grupo de astronautas busca un nuevo hogar para la humanidad.", "Ciencia ficción", "interstellar.jpg"));
        listaPeliculas.add(new Pelicula("The Godfather", crearFecha(1972, 3, 24), "La historia de una poderosa familia de la mafia.", "Crimen", "godfather.jpg"));
        listaPeliculas.add(new Pelicula("Pulp Fiction", crearFecha(1994, 10, 14), "Varias historias entrelazadas de crimen y redención.", "Crimen", "pulp_fiction.jpg"));
        listaPeliculas.add(new Pelicula("Schindler's List", crearFecha(1993, 12, 15), "La historia real de Oskar Schindler y su intento de salvar a judíos durante el Holocausto.", "Drama", "schindler_list.jpg"));
        listaPeliculas.add(new Pelicula("The Matrix", crearFecha(1999, 3, 31), "Un programador descubre la verdad sobre su realidad.", "Ciencia ficción", "matrix.jpg"));
        listaPeliculas.add(new Pelicula("Forrest Gump", crearFecha(1994, 7, 6), "La vida de un hombre con un coeficiente intelectual bajo que cambia el mundo de maneras inesperadas.", "Drama", "forrest_gump.jpg"));
        listaPeliculas.add(new Pelicula("Gladiator", crearFecha(2000, 5, 5), "Un general romano convertido en gladiador busca venganza.", "Acción", "gladiator.jpg"));
        listaPeliculas.add(new Pelicula("Titanic", crearFecha(1997, 12, 19), "Un romance trágico a bordo del Titanic.", "Romance", "titanic.jpg"));

        return listaPeliculas;
    }

    // Método auxiliar para crear fechas
    private Date crearFecha(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day); // Los meses en Calendar empiezan desde 0
        return calendar.getTime();
    }

}
