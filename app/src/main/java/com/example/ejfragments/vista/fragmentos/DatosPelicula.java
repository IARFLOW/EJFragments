package com.example.ejfragments.vista.fragmentos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ejfragments.R;
import com.example.ejfragments.mock.ObtencionDatos;
import com.example.ejfragments.modelo.entidades.Actor;
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



    // TODO: Rename and change types of parameters
    private String nombre;
    private String fecha;
    private String sinopsis;
    private String genero;
    private String imagen;
    private int idPelicula;

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
        Button btEditarComentarios = vistaFrag.findViewById(R.id.btEditarComentarios);

        ListView listaActores = vistaFrag.findViewById(R.id.lista_actores);
        listaActores.setOnItemClickListener((parent, view, position, id) -> {
            Actor actorSeleccionado = (Actor) parent.getItemAtPosition(position);

            Intent intent = new Intent(requireContext(), VistaActor.class);
            intent.putExtra("id_actor", actorSeleccionado.getId());
            startActivity(intent);
        });

        tvNombre.setText(nombre);
        tvSinopsis.setText(sinopsis);
        tvGenero.setText(genero);
        tvFecha.setText(fecha);

        // Configura el Listener del botón de comentarios
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
            });

            builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());

            builder.create().show();
        });

        ArrayList<Actor> actores = new ObtencionDatos().obtenerListadoActores(idPelicula);
        ActorAdapter adapter = new ActorAdapter(requireContext(), actores);
        listaActores.setAdapter(adapter);


        return vistaFrag;
    }
}