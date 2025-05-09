package com.example.ejfragments.vista.fragmentos;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ejfragments.R;
import com.example.ejfragments.api.ServicioREST;
import com.example.ejfragments.modelo.entidades.Pelicula;
import com.example.ejfragments.vista.adaptadores.PeliculaAdapter;

import java.util.ArrayList;

public class ListaPeliculas extends Fragment {

    private static final String ARG_NOMBRE = "nombre";
    private static final String ARG_FECHA = "fecha";
    private static final String ARG_SINOPSIS = "sinopsis";
    private static final String ARG_GENERO = "genero";
    private static final String ARG_IMAGEN = "imagen";

    private String nombre;
    private String fecha;
    private String sinopsis;
    private String genero;
    private String imagen;
    private OnPeliculasSelecionadasListener listener;

    public interface OnPeliculasSelecionadasListener {
        void OnPeliculasSelecionadasListener(Pelicula pelicula);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnPeliculasSelecionadasListener) {
            listener = (OnPeliculasSelecionadasListener) context;
        } else {
            throw new ClassCastException(context.toString() + "debe implementar OnPeliculasSelecionadasListener");
        }
    }

    public ListaPeliculas() {
    }

    public static ListaPeliculas newInstance(String nombre, String sinopsis, String genero, String fecha, String imagen) {
        ListaPeliculas fragment = new ListaPeliculas();
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
        View view = inflater.inflate(R.layout.fragment_lista_peliculas, container, false);
        ListView listView = view.findViewById(R.id.lista_pelicula);

        ArrayList<Pelicula> peliculas = new ArrayList<>();
        PeliculaAdapter adapter = new PeliculaAdapter(getContext(), peliculas);
        listView.setAdapter(adapter);

        ServicioREST.obtenerListadoPeliculas(new ServicioREST.PeliculasCallback() {
            @Override
            public void onSuccess(ArrayList<Pelicula> listaPeliculas) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        peliculas.clear();
                        peliculas.addAll(listaPeliculas);
                        adapter.notifyDataSetChanged();
                    });
                }
            }

            @Override
            public void onError(String mensaje) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        android.widget.Toast.makeText(getContext(), 
                            "Error al cargar películas: " + mensaje, 
                            android.widget.Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Pelicula pelicula = (Pelicula) parent.getItemAtPosition(position);
            listener.OnPeliculasSelecionadasListener(pelicula);
        });

        return view;
    }
}