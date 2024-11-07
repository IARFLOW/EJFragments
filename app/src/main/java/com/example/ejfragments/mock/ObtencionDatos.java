package com.example.ejfragments.mock;

import com.example.ejfragments.modelo.entidades.Actor;
import com.example.ejfragments.modelo.entidades.Pelicula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ObtencionDatos {

    // HashMap para almacenar las imágenes en Base64
    private static HashMap<Integer, String> actorImages = new HashMap<>();

    private static ArrayList<Pelicula> listaPeliculas;

    public ObtencionDatos() {
        // Cargar las imágenes de los actores en el HashMap
        cargarImagenesActores();
    }

    public ArrayList<Pelicula> obtenerListadoPeliculas() {
        if(listaPeliculas==null){
            listaPeliculas=new ArrayList<>();
            // Crear instancias de la clase Pelicula con actores y agregarlas a la lista
            listaPeliculas.add(new Pelicula(1, "Inception", crearFecha(2010, 7, 16),
                    "Un ladrón que roba secretos corporativos usando tecnología de sueños.",
                    "Ciencia ficción", "inception.jpg", crearActores(
                    new Actor(1, "Leonardo DiCaprio", crearFecha(1974, 11, 11), getImagenActor(1)),
                    new Actor(2, "Joseph Gordon-Levitt", crearFecha(1981, 2, 17), getImagenActor(2))
            )));

            listaPeliculas.add(new Pelicula(2, "The Dark Knight", crearFecha(2008, 7, 18),
                    "Batman enfrenta al Joker, un criminal caótico en Gotham.",
                    "Acción", "dark_knight.jpg", crearActores(
                    new Actor(3, "Christian Bale", crearFecha(1974, 1, 30), getImagenActor(3)),
                    new Actor(4, "Heath Ledger", crearFecha(1979, 4, 4), getImagenActor(4))
            )));

            listaPeliculas.add(new Pelicula(3, "Interstellar", crearFecha(2014, 11, 7),
                    "Un grupo de astronautas busca un nuevo hogar para la humanidad.",
                    "Ciencia ficción", "interstellar.jpg", crearActores(
                    new Actor(5, "Matthew McConaughey", crearFecha(1969, 11, 4), getImagenActor(5)),
                    new Actor(6, "Anne Hathaway", crearFecha(1982, 11, 12), getImagenActor(6))
            )));

            listaPeliculas.add(new Pelicula(4, "The Godfather", crearFecha(1972, 3, 24),
                    "La historia de una poderosa familia de la mafia.",
                    "Crimen", "godfather.jpg", crearActores(
                    new Actor(7, "Marlon Brando", crearFecha(1924, 4, 3), getImagenActor(7)),
                    new Actor(8, "Al Pacino", crearFecha(1940, 4, 25), getImagenActor(8))
            )));

            listaPeliculas.add(new Pelicula(5, "Pulp Fiction", crearFecha(1994, 10, 14),
                    "Varias historias entrelazadas de crimen y redención.",
                    "Crimen", "pulp_fiction.jpg", crearActores(
                    new Actor(9, "John Travolta", crearFecha(1954, 2, 18), getImagenActor(9)),
                    new Actor(10, "Samuel L. Jackson", crearFecha(1948, 12, 21), getImagenActor(10))
            )));

            listaPeliculas.add(new Pelicula(6, "Schindler's List", crearFecha(1993, 12, 15),
                    "La historia real de Oskar Schindler y su intento de salvar a judíos durante el Holocausto.",
                    "Drama", "schindler_list.jpg", crearActores(
                    new Actor(11, "Liam Neeson", crearFecha(1952, 6, 7), getImagenActor(11)),
                    new Actor(12, "Ben Kingsley", crearFecha(1943, 12, 31), getImagenActor(12))
            )));

            listaPeliculas.add(new Pelicula(7, "The Matrix", crearFecha(1999, 3, 31),
                    "Un programador descubre la verdad sobre su realidad virtual.",
                    "Ciencia ficción", "matrix.jpg", crearActores(
                    new Actor(13, "Keanu Reeves", crearFecha(1964, 9, 2), getImagenActor(13)),
                    new Actor(14, "Laurence Fishburne", crearFecha(1961, 7, 30), getImagenActor(14))
            )));

            listaPeliculas.add(new Pelicula(8, "Forrest Gump", crearFecha(1994, 7, 6),
                    "La vida de un hombre con un coeficiente intelectual bajo que cambia el mundo de maneras inesperadas.",
                    "Drama", "forrest_gump.jpg", crearActores(
                    new Actor(15, "Tom Hanks", crearFecha(1956, 7, 9), getImagenActor(15)),
                    new Actor(16, "Robin Wright", crearFecha(1966, 4, 8), getImagenActor(16))
            )));

            listaPeliculas.add(new Pelicula(9, "Gladiator", crearFecha(2000, 5, 5),
                    "Un general romano convertido en gladiador busca venganza.",
                    "Acción", "gladiator.jpg", crearActores(
                    new Actor(17, "Russell Crowe", crearFecha(1964, 4, 7), getImagenActor(17)),
                    new Actor(18, "Joaquin Phoenix", crearFecha(1974, 10, 28), getImagenActor(18))
            )));

            listaPeliculas.add(new Pelicula(10, "Titanic", crearFecha(1997, 12, 19),
                    "Un romance trágico a bordo del Titanic.",
                    "Romance", "titanic.jpg", crearActores(
                    new Actor(1, "Leonardo DiCaprio", crearFecha(1974, 11, 11), getImagenActor(1)),
                    new Actor(19, "Kate Winslet", crearFecha(1975, 10, 5), getImagenActor(19))
            )));

        }



        return listaPeliculas;
    }

    // Método auxiliar para crear fechas
    private Date crearFecha(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day); // Los meses en Calendar empiezan desde 0
        return calendar.getTime();
    }

    private List<Actor> crearActores(Actor... actores) {
        return new ArrayList<>(Arrays.asList(actores));
    }

    // Método para obtener la imagen del actor desde el HashMap
    private String getImagenActor(int idActor) {
        return actorImages.get(idActor); // Devuelve la imagen en Base64
    }

    // Método para cargar las imágenes de los actores en el HashMap
    private void cargarImagenesActores() {
        actorImages.put(1, encodeImageToBase64("leo.jpg"));
        actorImages.put(2, encodeImageToBase64("jgl.jpg"));
        actorImages.put(3, encodeImageToBase64("bale.jpg"));
        actorImages.put(4, encodeImageToBase64("ledger.jpg"));
        actorImages.put(5, encodeImageToBase64("mcconaughey.jpg"));
        actorImages.put(6, encodeImageToBase64("hathaway.jpg"));
        actorImages.put(7, encodeImageToBase64("brando.jpg"));
        actorImages.put(8, encodeImageToBase64("pacino.jpg"));
        actorImages.put(9, encodeImageToBase64("travolta.jpg"));
        actorImages.put(10, encodeImageToBase64("jackson.jpg"));
        actorImages.put(11, encodeImageToBase64("neeson.jpg"));
        actorImages.put(12, encodeImageToBase64("kingsley.jpg"));
        actorImages.put(13, encodeImageToBase64("reeves.jpg"));
        actorImages.put(14, encodeImageToBase64("fishburne.jpg"));
        actorImages.put(15, encodeImageToBase64("hanks.jpg"));
        actorImages.put(16, encodeImageToBase64("wright.jpg"));
        actorImages.put(17, encodeImageToBase64("crowe.jpg"));
        actorImages.put(18, encodeImageToBase64("phoenix.jpg"));
        actorImages.put(19, encodeImageToBase64("winslet.jpg"));
    }

    // Método auxiliar para codificar imágenes en Base64 (simulado, debe ajustarse para cargar las imágenes reales)
    private String encodeImageToBase64(String fileName) {
        // Aquí se debería cargar la imagen desde el archivo y convertirla en Base64
        // De momento, retornamos una cadena simulada para ejemplo.
        return "base64StringOf(" + fileName + ")";
    }

    public ArrayList<Actor> obtenerListadoActores(int idPelicula) {
        ArrayList<Actor> listadoActores=new ArrayList<>();

        if(listaPeliculas==null)
            obtenerListadoPeliculas();

        if(idPelicula!=0){

            listaPeliculas.stream().filter(pelicula -> pelicula.getId()==idPelicula).forEach(pelicula -> listadoActores.addAll(pelicula.getActores()));

        }
        else{
            //Cambiar para obtener todos los actores
            listaPeliculas.stream().forEach(pelicula -> listadoActores.addAll(pelicula.getActores()));

        }

        return listadoActores;

    }

    public Actor obtenerActor(int idActorSeleccionado) {
        return obtenerListadoActores(0).stream().filter(actor -> actor.getId()==idActorSeleccionado).findFirst().orElse(null);
    }
}