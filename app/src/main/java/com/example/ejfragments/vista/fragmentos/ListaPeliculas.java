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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaPeliculas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaPeliculas extends Fragment {

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
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nombre Parameter 1.
     * @param fecha  Parameter 2.
     * @return A new instance of fragment ListaPeliculas.
     */
    // TODO: Rename and change types and number of parameters
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
        try {
            View view = inflater.inflate(R.layout.fragment_lista_peliculas, container, false);
            ListView listView = view.findViewById(R.id.lista_pelicula);

            // Inicializamos un ArrayList vacío y el adaptador
            ArrayList<Pelicula> peliculas = new ArrayList<>();
            PeliculaAdapter adapter = new PeliculaAdapter(getContext(), peliculas);
            listView.setAdapter(adapter);

            // Cargamos los datos desde el servicio REST
            try {
                ServicioREST.obtenerListadoPeliculas(new ServicioREST.PeliculasCallback() {
                    @Override
                    public void onSuccess(ArrayList<Pelicula> listaPeliculas) {
                        // Actualizamos el UI en el hilo principal
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                try {
                                    peliculas.clear(); // Limpiamos la lista actual
                                    peliculas.addAll(listaPeliculas); // Añadimos los nuevos datos
                                    adapter.notifyDataSetChanged(); // Notificamos al adaptador
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    android.widget.Toast.makeText(getContext(), "Error al actualizar la lista: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(String mensaje) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                // Mostramos un mensaje de error
                                android.widget.Toast.makeText(getContext(), 
                                    "Error al cargar películas: " + mensaje, 
                                    android.widget.Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                android.widget.Toast.makeText(getContext(), 
                    "Error al conectar con el servicio: " + e.getMessage(), 
                    android.widget.Toast.LENGTH_SHORT).show();
            }

            listView.setOnItemClickListener((parent, view1, position, id) -> {
                Pelicula pelicula = (Pelicula) parent.getItemAtPosition(position);
                listener.OnPeliculasSelecionadasListener(pelicula);
            });

            return view;
        } catch (Exception e) {
            e.printStackTrace();
            android.widget.Toast.makeText(getContext(), 
                "Error general: " + e.getMessage(), 
                android.widget.Toast.LENGTH_SHORT).show();
            // Devolvemos una vista vacía en caso de error
            return new View(getContext());
        }
    }
}


