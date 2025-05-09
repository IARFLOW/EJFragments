package com.example.ejfragments.api;

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

    private static final String BASE_URL = "http://10.0.2.2:8080"; 
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public interface PeliculasCallback {
        void onSuccess(ArrayList<Pelicula> peliculas);
        void onError(String mensaje);
    }

    public interface ActoresCallback {
        void onSuccess(ArrayList<Actor> actores);
        void onError(String mensaje);
    }

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

    public static void obtenerActoresPelicula(int idPelicula, ActoresCallback callback) {
        if (idPelicula <= 0) {
            obtenerListadoActores(callback);
            return;
        }
        
        String url = BASE_URL + "/obtenerPelicula?id=" + idPelicula;
        
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        
                        Pelicula pelicula = gson.fromJson(responseData, Pelicula.class);
                        
                        if (pelicula != null && pelicula.getActores() != null && !pelicula.getActores().isEmpty()) {
                            ArrayList<Actor> actoresList = new ArrayList<>(pelicula.getActores());
                            callback.onSuccess(actoresList);
                        } else {
                            obtenerListadoActores(callback);
                        }
                    } else {
                        obtenerListadoActores(callback);
                    }
                } catch (Exception e) {
                    obtenerListadoActores(callback);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                obtenerListadoActores(callback);
            }
        });
    }

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

    public static android.graphics.Bitmap base64ToBitmap(String base64Str) {
        if (base64Str == null || base64Str.isEmpty()) {
            return null;
        }
        
        try {
            byte[] decodedBytes = android.util.Base64.decode(base64Str, android.util.Base64.DEFAULT);
            return android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            return null;
        }
    }
}
