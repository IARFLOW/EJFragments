package com.example.ejfragments.vista.fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejfragments.R;
import com.example.ejfragments.api.ServicioREST;
import com.example.ejfragments.modelo.database.AppDatabase;
import com.example.ejfragments.modelo.entidades.Actor;
import com.example.ejfragments.modelo.entidades.PeliculaRatingEntity;
import com.example.ejfragments.vista.acticidades.SeleccionFechaActivity;
import com.example.ejfragments.vista.acticidades.VistaActor;
import com.example.ejfragments.vista.adaptadores.ActorAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatosPelicula#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosPelicula extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NOMBRE = "nombre";
    private static final String ARG_FECHA = "fecha";
    private static final String ARG_SINOPSIS = "sinopsis";
    private static final String ARG_GENERO = "genero";
    private static final String ARG_IMAGEN = "imagen";
    private static final String ARG_ID_PELICULA = "id_pelicula";
    private static final int REQUEST_CODE_FECHA = 1234;
    private static final String PREF_NAME = "PeliculaPrefs";
    private static final String KEY_COMENTARIO_PREFIX = "comentario_";

    // TODO: Rename and change types of parameters
    private String nombre;
    private String fecha;
    private String sinopsis;
    private String genero;
    private String imagen;
    private int idPelicula;
    private TextView tvFechaElegida;
    private SharedPreferences sharedPreferences;
    private AppDatabase database;

    public DatosPelicula() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nombre Parameter 1.
     * @param fecha Parameter 2.
     * @return A new instance of fragment DatosPelicula.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosPelicula newInstance(String nombre, String sinopsis, String genero, String fecha, String imagen,  int idPelicula) {
        DatosPelicula fragment = new DatosPelicula();
        Bundle args = new Bundle();
        args.putString(ARG_NOMBRE, nombre);
        args.putString(ARG_SINOPSIS, sinopsis);
        args.putString(ARG_GENERO, genero);
        args.putString(ARG_FECHA, fecha);
        args.putString(ARG_IMAGEN, imagen);
        args.putInt(ARG_ID_PELICULA, idPelicula);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            database = AppDatabase.getInstance(requireContext());
            if (getArguments() != null) {
                nombre = getArguments().getString(ARG_NOMBRE);
                sinopsis = getArguments().getString(ARG_SINOPSIS);
                genero = getArguments().getString(ARG_GENERO);
                fecha = getArguments().getString(ARG_FECHA);
                imagen = getArguments().getString(ARG_IMAGEN);
                idPelicula = getArguments().getInt(ARG_ID_PELICULA);
            }
        } catch (Exception e) {
            Log.e("DatosPelicula", "Error en onCreate", e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            View vistaFrag = inflater.inflate(R.layout.fragment_datos_pelicula, container, false);

            TextView tvNombre = vistaFrag.findViewById(R.id.tvnombre);
            TextView tvSinopsis = vistaFrag.findViewById(R.id.tvsinopsis);
            TextView tvGenero = vistaFrag.findViewById(R.id.tvgenero);
            TextView tvFecha = vistaFrag.findViewById(R.id.tvfecha);
            ImageView ivImagen = vistaFrag.findViewById(R.id.ivimagen);
            TextView tvComentarios = vistaFrag.findViewById(R.id.tvComentarios);
            
            // Cargar comentario de SharedPreferences si existe
            try {
                String comentarioGuardado = sharedPreferences.getString(KEY_COMENTARIO_PREFIX + idPelicula, "");
                if (!comentarioGuardado.isEmpty()) {
                    tvComentarios.setText(comentarioGuardado);
                }
            } catch (Exception e) {
                Log.e("DatosPelicula", "Error al cargar comentario", e);
            }
            
            Button btEditarComentarios = vistaFrag.findViewById(R.id.btEditarComentarios);
            Button btIndicarFecha = vistaFrag.findViewById(R.id.btIndicarFecha);
            tvFechaElegida = vistaFrag.findViewById(R.id.tvFechaElegida);
            
            // Intentar cargar la imagen desde Base64
            try {
                if (imagen != null && !imagen.isEmpty()) {
                    Bitmap bitmap = ServicioREST.base64ToBitmap(imagen);
                    if (bitmap != null) {
                        ivImagen.setImageBitmap(bitmap);
                    }
                }
            } catch (Exception e) {
                Log.e("DatosPelicula", "Error al cargar imagen", e);
            }
            
            cargarPuntuacionYFecha();
            
            // Establecer texto en los campos
            tvNombre.setText(nombre != null ? nombre : "");
            tvSinopsis.setText(sinopsis != null ? sinopsis : "");
            tvGenero.setText(genero != null ? genero : "");
            tvFecha.setText(fecha != null ? fecha : "");

            btEditarComentarios.setOnClickListener(v -> {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    View dialogView = inflater.inflate(R.layout.dialog_comentarios, null);
                    EditText etComentariosDialog = dialogView.findViewById(R.id.etComentariosDialog);
                    etComentariosDialog.setText(tvComentarios.getText().toString());

                    builder.setView(dialogView);
                    builder.setTitle("Comentarios de la película");

                    builder.setPositiveButton("Aceptar", (dialog, which) -> {
                        String comentariosNuevos = etComentariosDialog.getText().toString();
                        tvComentarios.setText(comentariosNuevos);

                        // Guardar comentario en SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_COMENTARIO_PREFIX + idPelicula, comentariosNuevos);
                        editor.apply();

                        Toast.makeText(requireContext(), "Comentario guardado", Toast.LENGTH_SHORT).show();
                    });

                    builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());

                    builder.create().show();
                } catch (Exception e) {
                    Log.e("DatosPelicula", "Error al editar comentarios", e);
                    Toast.makeText(requireContext(), "Error al editar comentarios", Toast.LENGTH_SHORT).show();
                }
            });

            RatingBar ratingBar = vistaFrag.findViewById(R.id.ratingBarPelicula);
            Button btGuardarPelicula = vistaFrag.findViewById(R.id.btGuardarPelicula);

            btGuardarPelicula.setOnClickListener(v -> {
                try {
                    String fechaEmision = tvFechaElegida.getText().toString();
                    String comentarios = tvComentarios.getText().toString();
                    float rating = ratingBar.getRating();

                    // Guardar comentario en SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_COMENTARIO_PREFIX + idPelicula, comentarios);
                    editor.apply();

                    // Guardar puntuación y fecha en Room
                    guardarPuntuacionYFecha(rating, fechaEmision);

                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Datos a Guardar");

                    String mensaje = "Fecha de emision: " + fechaEmision + "\n"
                            + "Comentarios: " + comentarios + "\n"
                            + "Rating: " + rating;
                    builder.setMessage(mensaje);

                    builder.setPositiveButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } catch (Exception e) {
                    Log.e("DatosPelicula", "Error al guardar película", e);
                    Toast.makeText(requireContext(), "Error al guardar datos", Toast.LENGTH_SHORT).show();
                }
            });

            btIndicarFecha.setOnClickListener(v -> {
                try {
                    Intent i = new Intent(requireContext(), SeleccionFechaActivity.class);
                    startActivityForResult(i, REQUEST_CODE_FECHA);
                } catch (Exception e) {
                    Log.e("DatosPelicula", "Error al abrir selección de fecha", e);
                    Toast.makeText(requireContext(), "Error al abrir selección de fecha", Toast.LENGTH_SHORT).show();
                }
            });

            ListView listaActores = vistaFrag.findViewById(R.id.lista_actores);
            listaActores.setOnItemClickListener((parent, view, position, id) -> {
                try {
                    Actor actorSeleccionado = (Actor) parent.getItemAtPosition(position);
                    if (actorSeleccionado != null) {
                        Intent intent = new Intent(requireContext(), VistaActor.class);
                        intent.putExtra("id_actor", actorSeleccionado.getId());
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e("DatosPelicula", "Error al seleccionar actor", e);
                    Toast.makeText(requireContext(), "Error al seleccionar actor", Toast.LENGTH_SHORT).show();
                }
            });

            // Inicializamos un ArrayList vacío y el adaptador
            ArrayList<Actor> actores = new ArrayList<>();
            ActorAdapter adapter = new ActorAdapter(requireContext(), actores);
            listaActores.setAdapter(adapter);

            // Cargamos los actores de la película desde el servicio REST
            try {
                Log.d("DatosPelicula", "Solicitando actores para película ID: " + idPelicula);
                
                // Limpiar y reiniciar el adaptador
                adapter.clear();
                adapter.notifyDataSetChanged();
                
                // Actualizar UI para indicar carga
                Toast.makeText(requireContext(), "Cargando actores para " + 
                    (nombre != null ? nombre : "esta película") + "...", Toast.LENGTH_SHORT).show();
                
                // Intentar cargar actores de la película seleccionada
                ServicioREST.obtenerListadoActores(new ServicioREST.ActoresCallback() {
                    @Override
                    public void onSuccess(ArrayList<Actor> todosLosActores) {
                        // Una vez tenemos todos los actores, filtrar por ID de película
                        ServicioREST.obtenerActoresPelicula(idPelicula, new ServicioREST.ActoresCallback() {
                            @Override
                            public void onSuccess(ArrayList<Actor> actoresDePelicula) {
                                if (getActivity() != null && isAdded()) {
                                    requireActivity().runOnUiThread(() -> {
                                        try {
                                            // Log de verificación
                                            Log.d("DatosPelicula", "Actores recibidos para película " + nombre + " (ID: " + idPelicula + "): " + actoresDePelicula.size());
                                            
                                            if (actoresDePelicula.isEmpty()) {
                                                Log.w("DatosPelicula", "No se recibieron actores específicos para esta película, usando una selección aleatoria");
                                                // Si no tenemos actores para esta película, seleccionar algunos aleatoriamente
                                                ArrayList<Actor> actoresAleatorios = new ArrayList<>();
                                                if (!todosLosActores.isEmpty()) {
                                                    int maxActores = Math.min(3, todosLosActores.size());
                                                    for (int i = 0; i < maxActores; i++) {
                                                        actoresAleatorios.add(todosLosActores.get(i));
                                                    }
                                                    
                                                    // Actualizar la UI con estos actores aleatorios
                                                    adapter.clear();
                                                    adapter.addAll(actoresAleatorios);
                                                    adapter.notifyDataSetChanged();
                                                    
                                                    Toast.makeText(requireContext(), "Mostrando actores de ejemplo", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(requireContext(), "No hay actores disponibles", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                // Tenemos actores específicos para esta película
                                                adapter.clear();
                                                adapter.addAll(actoresDePelicula);
                                                adapter.notifyDataSetChanged();
                                            }
                                        } catch (Exception e) {
                                            Log.e("DatosPelicula", "Error al actualizar lista de actores", e);
                                            Toast.makeText(requireContext(), "Error al mostrar actores: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(String mensaje) {
                                if (getActivity() != null && isAdded()) {
                                    requireActivity().runOnUiThread(() -> {
                                        Log.e("DatosPelicula", "Error al obtener actores de película: " + mensaje);
                                        // Mostrar algunos actores aleatorios en caso de error
                                        if (!todosLosActores.isEmpty()) {
                                            ArrayList<Actor> actoresAleatorios = new ArrayList<>();
                                            int maxActores = Math.min(3, todosLosActores.size());
                                            for (int i = 0; i < maxActores; i++) {
                                                actoresAleatorios.add(todosLosActores.get(i));
                                            }
                                            adapter.clear();
                                            adapter.addAll(actoresAleatorios);
                                            adapter.notifyDataSetChanged();
                                            
                                            Toast.makeText(requireContext(), "Mostrando actores de ejemplo", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(requireContext(), "No se pudieron cargar actores", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(String mensaje) {
                        if (getActivity() != null && isAdded()) {
                            requireActivity().runOnUiThread(() -> {
                                Log.e("DatosPelicula", "Error al obtener listado de actores: " + mensaje);
                                Toast.makeText(requireContext(), "Error al cargar actores: " + mensaje, Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("DatosPelicula", "Error al obtener actores", e);
                Toast.makeText(requireContext(), "Error general al cargar actores: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            return vistaFrag;
        } catch (Exception e) {
            Log.e("DatosPelicula", "Error general en onCreateView", e);
            Toast.makeText(requireContext(), "Error al cargar vista", Toast.LENGTH_SHORT).show();
            // Devolvemos una vista vacía en caso de error
            return new View(requireContext());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE_FECHA && resultCode == Activity.RESULT_OK && data != null) {
                String fechaEmision = data.getStringExtra("fecha_emision");
                if (fechaEmision != null) {
                    tvFechaElegida.setText(fechaEmision);
                }
            }
        } catch (Exception e) {
            Log.e("DatosPelicula", "Error en onActivityResult", e);
        }
    }
    
    // Método para cargar puntuación y fecha desde Room
    private void cargarPuntuacionYFecha() {
        // Importante: las operaciones de base de datos no deben hacerse en el hilo principal
        new Thread(() -> {
            try {
                // Intentar obtener datos de la base de datos
                final PeliculaRatingEntity rating = database.peliculaRatingDao().getRatingByPeliculaId(idPelicula);

                // Si encontramos datos, actualizar la UI en el hilo principal
                if (rating != null && getActivity() != null && isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        try {
                            if (getView() != null) {
                                RatingBar ratingBar = getView().findViewById(R.id.ratingBarPelicula);

                                // Actualizar puntuación
                                if (rating.getPuntuacion() > 0) {
                                    ratingBar.setRating(rating.getPuntuacion());
                                }

                                // Actualizar fecha de emisión si existe
                                if (rating.getFechaEmision() != null) {
                                    java.util.Date fechaEmision = new java.util.Date(rating.getFechaEmision());
                                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
                                    tvFechaElegida.setText(sdf.format(fechaEmision));
                                }
                            }
                        } catch (Exception e) {
                            Log.e("DatosPelicula", "Error al actualizar UI con rating", e);
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("DatosPelicula", "Error al cargar puntuación y fecha", e);
            }
        }).start();
    }

    // Método para guardar puntuación y fecha en Room
    private void guardarPuntuacionYFecha(float rating, String fechaEmisionStr) {
        new Thread(() -> {
            try {
                // Convertir texto de fecha a objeto Date si hay una fecha válida
                Long timestampFecha = null;
                if (fechaEmisionStr != null && !fechaEmisionStr.equals("Ninguna indicada") && !fechaEmisionStr.isEmpty()) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
                    java.util.Date fechaEmision = sdf.parse(fechaEmisionStr);
                    if (fechaEmision != null) {
                        timestampFecha = fechaEmision.getTime();
                    }
                }

                // Crear la entidad para guardar
                PeliculaRatingEntity ratingEntity = new PeliculaRatingEntity(idPelicula, rating, timestampFecha);

                // Guardar en la base de datos
                database.peliculaRatingDao().insert(ratingEntity);

                // Notificar en el hilo principal
                if (getActivity() != null && isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Puntuación y fecha guardadas", Toast.LENGTH_SHORT).show()
                    );
                }
            } catch (java.text.ParseException e) {
                Log.e("DatosPelicula", "Error al parsear fecha", e);
                // Notificar error en el hilo principal
                if (getActivity() != null && isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Error al guardar fecha", Toast.LENGTH_SHORT).show()
                    );
                }
            } catch (Exception e) {
                Log.e("DatosPelicula", "Error general al guardar rating", e);
            }
        }).start();
    }
}