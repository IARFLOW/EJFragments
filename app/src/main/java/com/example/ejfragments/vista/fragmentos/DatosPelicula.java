package com.example.ejfragments.vista.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ejfragments.R;

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

    // TODO: Rename and change types of parameters
    private String nombre;
    private String fecha;
    private String sinopsis;
    private String genero;
    private String imagen;

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
    public static DatosPelicula newInstance(String nombre, String sinopsis, String genero, String fecha, String imagen) {
        DatosPelicula fragment = new DatosPelicula();
        Bundle args = new Bundle();
        args.putString(ARG_NOMBRE, nombre);
        args.putString(ARG_SINOPSIS, sinopsis);
        args.putString(ARG_GENERO, genero);
        args.putString(ARG_FECHA, fecha);
        args.putString(ARG_IMAGEN, imagen);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vistaFrag=inflater.inflate(R.layout.fragment_datos_pelicula, container, false);

        TextView tvNombre = vistaFrag.findViewById(R.id.tvnombre);
        TextView tvSinopsis = vistaFrag.findViewById(R.id.tvsinopsis);
        TextView tvGenero = vistaFrag.findViewById(R.id.tvgenero);
        TextView tvFecha = vistaFrag.findViewById(R.id.tvfecha);
        ImageView ivImagen = vistaFrag.findViewById(R.id.ivimagen);

        tvNombre.setText(nombre);
        tvSinopsis.setText(sinopsis);
        tvGenero.setText(genero);
        tvFecha.setText(fecha);



        return vistaFrag;
    }
}