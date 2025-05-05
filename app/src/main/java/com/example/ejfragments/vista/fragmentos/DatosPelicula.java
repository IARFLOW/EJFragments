package com.example.ejfragments.vista.fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ejfragments.R;
import com.example.ejfragments.mock.ObtencionDatos;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vistaFrag = inflater.inflate(R.layout.fragment_datos_pelicula, container, false);

        TextView tvNombre = vistaFrag.findViewById(R.id.tvnombre);
        TextView tvSinopsis = vistaFrag.findViewById(R.id.tvsinopsis);
        TextView tvGenero = vistaFrag.findViewById(R.id.tvgenero);
        TextView tvFecha = vistaFrag.findViewById(R.id.tvfecha);
        ImageView ivImagen = vistaFrag.findViewById(R.id.ivimagen);
        TextView tvComentarios = vistaFrag.findViewById(R.id.tvComentarios);
        String comentarioGuardado = sharedPreferences.getString(KEY_COMENTARIO_PREFIX + idPelicula, "");
        if (!comentarioGuardado.isEmpty()) {
            tvComentarios.setText(comentarioGuardado);
        }
        Button btEditarComentarios = vistaFrag.findViewById(R.id.btEditarComentarios);

        Button btIndicarFecha = vistaFrag.findViewById(R.id.btIndicarFecha);
        tvFechaElegida = vistaFrag.findViewById(R.id.tvFechaElegida);
        cargarPuntuacionYFecha();
        tvNombre.setText(nombre);
        tvSinopsis.setText(sinopsis);
        tvGenero.setText(genero);
        tvFecha.setText(fecha);

        btEditarComentarios.setOnClickListener(v -> {
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

                android.widget.Toast.makeText(requireContext(), "Comentario guardado", android.widget.Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());

            builder.create().show();
        });

        RatingBar ratingBar = vistaFrag.findViewById(R.id.ratingBarPelicula);
        Button btGuardarPelicula = vistaFrag.findViewById(R.id.btGuardarPelicula);

        btGuardarPelicula.setOnClickListener(v -> {
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
        });

        btIndicarFecha.setOnClickListener(v -> {
           Intent i = new Intent(requireContext(), SeleccionFechaActivity.class);
            startActivityForResult(i, REQUEST_CODE_FECHA);
        });

        ListView listaActores = vistaFrag.findViewById(R.id.lista_actores);
        listaActores.setOnItemClickListener((parent, view, position, id) -> {
            Actor actorSeleccionado = (Actor) parent.getItemAtPosition(position);
            Intent intent = new Intent(requireContext(), VistaActor.class);
            intent.putExtra("id_actor", actorSeleccionado.getId());
            startActivity(intent);
        });

        ArrayList<Actor> actores = new ObtencionDatos().obtenerListadoActores(idPelicula);
        ActorAdapter adapter = new ActorAdapter(requireContext(), actores);
        listaActores.setAdapter(adapter);

        return vistaFrag;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FECHA && resultCode == Activity.RESULT_OK) {
            String fechaEmision = data.getStringExtra("fecha_emision");
            tvFechaElegida.setText(fechaEmision);
        }
    }
    // Método para cargar puntuación y fecha desde Room
    // Método para cargar puntuación y fecha desde Room
    private void cargarPuntuacionYFecha() {
        // Importante: las operaciones de base de datos no deben hacerse en el hilo principal
        new Thread(() -> {
            try {
                // Intentar obtener datos de la base de datos
                final PeliculaRatingEntity rating = database.peliculaRatingDao().getRatingByPeliculaId(idPelicula);

                // Si encontramos datos, actualizar la UI en el hilo principal
                if (rating != null) {
                    requireActivity().runOnUiThread(() -> {
                        try {
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    } // Aquí debe terminar el método cargarPuntuacionYFecha

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
                com.example.ejfragments.modelo.entidades.PeliculaRatingEntity ratingEntity =
                        new com.example.ejfragments.modelo.entidades.PeliculaRatingEntity(idPelicula, rating, timestampFecha);

                // Guardar en la base de datos
                database.peliculaRatingDao().insert(ratingEntity);

                // Notificar en el hilo principal
                requireActivity().runOnUiThread(() ->
                        android.widget.Toast.makeText(requireContext(), "Puntuación y fecha guardadas", android.widget.Toast.LENGTH_SHORT).show()
                );
            } catch (java.text.ParseException e) {
                e.printStackTrace();
                // Notificar error en el hilo principal
                requireActivity().runOnUiThread(() ->
                        android.widget.Toast.makeText(requireContext(), "Error al guardar fecha", android.widget.Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}