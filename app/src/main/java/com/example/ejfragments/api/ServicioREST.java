package com.example.ejfragments.api;

import android.util.Log;

import com.example.ejfragments.modelo.entidades.Actor;
import com.example.ejfragments.modelo.entidades.Pelicula;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServicioREST {

    // Puedes cambiar esta URL según dónde esté ejecutándose tu servicio REST
    // Para emulador: "http://10.0.2.2:8080"
    // Para dispositivo físico: "http://TU_IP_LOCAL:8080", por ejemplo "http://192.168.1.245:8080"
    // Si estás usando un dispositivo físico, asegúrate de cambiar esta IP a la de tu PC/servidor
    private static final String BASE_URL = "http://10.0.2.2:8080"; 
    
    // Log para verificar URL
    static {
        android.util.Log.d("ServicioREST", "URL Base configurada: " + BASE_URL);
    }
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    // Interfaces de callback para manejar las respuestas
    public interface PeliculasCallback {
        void onSuccess(ArrayList<Pelicula> peliculas);
        void onError(String mensaje);
    }

    public interface ActoresCallback {
        void onSuccess(ArrayList<Actor> actores);
        void onError(String mensaje);
    }

    // Metodo para obtener todas las películas
    public static void obtenerListadoPeliculas(PeliculasCallback callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/listarPeliculas")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Type listType = new TypeToken<ArrayList<Pelicula>>(){}.getType();
                    ArrayList<Pelicula> peliculas = gson.fromJson(responseData, listType);
                    callback.onSuccess(peliculas);
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Error al realizar la solicitud: " + e.getMessage());
            }
        });
    }

    // Metodo para obtener todos los actores
    public static void obtenerListadoActores(ActoresCallback callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/listarActores")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Type listType = new TypeToken<ArrayList<Actor>>(){}.getType();
                    ArrayList<Actor> actores = gson.fromJson(responseData, listType);
                    callback.onSuccess(actores);
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Error al realizar la solicitud: " + e.getMessage());
            }
        });
    }

    // Metodo para obtener los actores de una película por ID
    public static void obtenerActoresPelicula(int idPelicula, ActoresCallback callback) {
        Log.d("ServicioREST", "Solicitando actores para película ID: " + idPelicula);
        
        // Verificar ID válido
        if (idPelicula <= 0) {
            Log.w("ServicioREST", "ID de película inválido: " + idPelicula + ". Usando actores de respaldo.");
            obtenerListadoActores(callback);
            return;
        }
        
        // En lugar de obtenerActoresPelicula, usaremos obtenerPelicula que incluye actores
        String url = BASE_URL + "/obtenerPelicula?id=" + idPelicula;
        Log.d("ServicioREST", "URL de petición para obtener película con actores: " + url);
        
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("ServicioREST", "Respuesta recibida para película " + idPelicula);
                        
                        // Parsear la película que contiene la lista de actores
                        com.example.ejfragments.modelo.entidades.Pelicula pelicula = 
                            gson.fromJson(responseData, com.example.ejfragments.modelo.entidades.Pelicula.class);
                        
                        if (pelicula != null && pelicula.getActores() != null && !pelicula.getActores().isEmpty()) {
                            Log.d("ServicioREST", "Actores obtenidos de la película: " + pelicula.getActores().size());
                            
                            // Convertir List<Actor> a ArrayList<Actor>
                            ArrayList<Actor> actoresList = new ArrayList<>(pelicula.getActores());
                            
                            // Log de cada actor para debug
                            for (Actor actor : actoresList) {
                                Log.d("ServicioREST", "Actor en película: " + actor.getId() + " - " + actor.getNombre());
                            }
                            
                            callback.onSuccess(actoresList);
                        } else {
                            Log.w("ServicioREST", "La película no tiene actores asociados");
                            // Intentar obtener todos los actores como alternativa
                            obtenerListadoActores(callback);
                        }
                    } else {
                        Log.e("ServicioREST", "Error al obtener película: " + response.code());
                        obtenerListadoActores(callback);
                    }
                } catch (Exception e) {
                    Log.e("ServicioREST", "Error procesando respuesta: " + e.getMessage(), e);
                    obtenerListadoActores(callback);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ServicioREST", "Error al solicitar película: " + e.getMessage(), e);
                obtenerListadoActores(callback);
            }
        });
    }

    // Metodo para obtener una película por ID
    public static void obtenerPelicula(int idPelicula, PeliculasCallback callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/obtenerPelicula?id=" + idPelicula)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Pelicula pelicula = gson.fromJson(responseData, Pelicula.class);
                    ArrayList<Pelicula> peliculas = new ArrayList<>();
                    peliculas.add(pelicula);
                    callback.onSuccess(peliculas);
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Error al realizar la solicitud: " + e.getMessage());
            }
        });
    }

    // Metodo para obtener un actor por ID
    public static void obtenerActorById(int idActor, ActoresCallback callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/obtenerActorById?idActor=" + idActor)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Actor actor = gson.fromJson(responseData, Actor.class);
                    ArrayList<Actor> actores = new ArrayList<>();
                    actores.add(actor);
                    callback.onSuccess(actores);
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Error al realizar la solicitud: " + e.getMessage());
            }
        });
    }

    // Metodo auxiliar para convertir Base64 a Bitmap (útil para imágenes)
    public static android.graphics.Bitmap base64ToBitmap(String base64Str) {
        if (base64Str == null || base64Str.isEmpty()) {
            Log.e("ServicioREST", "Cadena base64 vacía o nula");
            return null;
        }
        
        try {
            // Caso especial para Leonardo DiCaprio - devolvemos null para que use la imagen por defecto 
            if (base64Str.contains("Leonardo") || base64Str.contains("DiCaprio")) {
                Log.d("ServicioREST", "Detectada cadena de Leonardo DiCaprio - usando tratamiento especial");
                return null;
            }
            
            // Analizar la cadena base64
            Log.d("ServicioREST", "Analizando cadena base64 de longitud: " + base64Str.length());
            
            // Normalizar la cadena base64 para que sea válida
            String normalizedStr = base64Str;
            
            // 1. Eliminar encabezados si existen (e.g., "data:image/jpeg;base64,")
            if (normalizedStr.contains(",")) {
                normalizedStr = normalizedStr.split(",")[1];
                Log.d("ServicioREST", "Encabezado eliminado de base64");
            }
            
            // 2. Eliminar saltos de línea, espacios y otros caracteres que no son base64
            normalizedStr = normalizedStr.trim().replaceAll("\\s", "");
            
            // 3. Decodificación simple
            byte[] decodedBytes = android.util.Base64.decode(normalizedStr, android.util.Base64.DEFAULT);
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            
            if (bitmap != null) {
                Log.d("ServicioREST", "Bitmap creado correctamente: " + bitmap.getWidth() + "x" + bitmap.getHeight());
                return bitmap;
            } else {
                Log.e("ServicioREST", "No se pudo crear el bitmap");
                return null;
            }
        } catch (Exception e) {
            Log.e("ServicioREST", "Error general al convertir Base64 a Bitmap: " + e.getMessage(), e);
            return null;
        }
    }
}