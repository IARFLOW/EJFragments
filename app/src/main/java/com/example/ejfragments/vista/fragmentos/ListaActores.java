package com.example.ejfragments.vista.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ejfragments.R;
import com.example.ejfragments.api.ServicioREST;
import com.example.ejfragments.modelo.entidades.Actor;
import com.example.ejfragments.vista.acticidades.VistaActor;
import com.example.ejfragments.vista.adaptadores.ActorAdapter;

import java.util.ArrayList;

public class ListaActores extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_lista_actores, container, false);

            ListView listView = view.findViewById(R.id.lista_actores);
            ProgressBar progressBar = view.findViewById(R.id.progressBarActores);
            TextView tvEstado = view.findViewById(R.id.tvEstadoListaActores);

            // Inicializamos un ArrayList vacío y el adaptador
            ArrayList<Actor> listaDeActores = new ArrayList<>();
            ActorAdapter adapter = new ActorAdapter(requireContext(), listaDeActores);
            listView.setAdapter(adapter);

            // Cargamos los datos desde el servicio REST
            try {
                // Mostrar ProgressBar mientras carga
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                tvEstado.setVisibility(View.GONE);
                
                ServicioREST.obtenerListadoActores(new ServicioREST.ActoresCallback() {
                    @Override
                    public void onSuccess(ArrayList<Actor> actores) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                try {
                                    // Ocultar ProgressBar
                                    progressBar.setVisibility(View.GONE);
                                    
                                    if (actores != null && !actores.isEmpty()) {
                                        // Tenemos datos
                                        listaDeActores.clear(); // Limpiamos la lista actual
                                        listaDeActores.addAll(actores); // Añadimos los nuevos datos
                                        adapter.notifyDataSetChanged(); // Notificamos al adaptador
                                        listView.setVisibility(View.VISIBLE);
                                        tvEstado.setVisibility(View.GONE);
                                    } else {
                                        // No hay datos
                                        tvEstado.setText("No se encontraron actores");
                                        tvEstado.setVisibility(View.VISIBLE);
                                        listView.setVisibility(View.GONE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    tvEstado.setText("Error al actualizar la lista: " + e.getMessage());
                                    tvEstado.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(String mensaje) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                // Ocultar ProgressBar y mostrar mensaje de error
                                progressBar.setVisibility(View.GONE);
                                tvEstado.setText("Error al cargar actores: " + mensaje);
                                tvEstado.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            });
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
                tvEstado.setText("Error al conectar con el servicio: " + e.getMessage());
                tvEstado.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }

            listView.setOnItemClickListener((parent, view1, position, id) -> {
                Actor actorSeleccionado = (Actor) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), VistaActor.class);
                intent.putExtra("id_actor", actorSeleccionado.getId());
                startActivity(intent);
            });

            return view;
        } catch (Exception e) {
            e.printStackTrace();
            // Devolvemos una vista vacía en caso de error
            return new View(getContext());
        }
    }
}