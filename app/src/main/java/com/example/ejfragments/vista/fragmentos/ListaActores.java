package com.example.ejfragments.vista.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
        View view = inflater.inflate(R.layout.fragment_lista_actores, container, false);

        ListView listView = view.findViewById(R.id.lista_actores);
        TextView tvEstado = view.findViewById(R.id.tvEstadoListaActores);

        ArrayList<Actor> listaDeActores = new ArrayList<>();
        ActorAdapter adapter = new ActorAdapter(requireContext(), listaDeActores);
        listView.setAdapter(adapter);

        tvEstado.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        
        ServicioREST.obtenerListadoActores(new ServicioREST.ActoresCallback() {
            @Override
            public void onSuccess(ArrayList<Actor> actores) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (actores != null && !actores.isEmpty()) {
                            listaDeActores.clear();
                            listaDeActores.addAll(actores);
                            adapter.notifyDataSetChanged();
                            listView.setVisibility(View.VISIBLE);
                            tvEstado.setVisibility(View.GONE);
                        } else {
                            tvEstado.setText("No se encontraron actores");
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
                        tvEstado.setText("Error al cargar actores: " + mensaje);
                        tvEstado.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    });
                }
            }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Actor actorSeleccionado = (Actor) parent.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), VistaActor.class);
            intent.putExtra("id_actor", actorSeleccionado.getId());
            startActivity(intent);
        });

        return view;
    }
}