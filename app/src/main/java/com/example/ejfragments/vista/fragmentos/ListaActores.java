package com.example.ejfragments.vista.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

            // Inicializamos un ArrayList vacío y el adaptador
            ArrayList<Actor> listaDeActores = new ArrayList<>();
            ActorAdapter adapter = new ActorAdapter(requireContext(), listaDeActores);
            listView.setAdapter(adapter);

            // Cargamos los datos desde el servicio REST
            try {
                ServicioREST.obtenerListadoActores(new ServicioREST.ActoresCallback() {
                    @Override
                    public void onSuccess(ArrayList<Actor> actores) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                try {
                                    listaDeActores.clear(); // Limpiamos la lista actual
                                    listaDeActores.addAll(actores); // Añadimos los nuevos datos
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
                                    "Error al cargar actores: " + mensaje, 
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
                Actor actorSeleccionado = (Actor) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), VistaActor.class);
                intent.putExtra("id_actor", actorSeleccionado.getId());
                startActivity(intent);
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