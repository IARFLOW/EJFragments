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
import com.example.ejfragments.mock.ObtencionDatos;
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

        ObtencionDatos datos = new ObtencionDatos();
        ArrayList<Actor> listaDeActores = datos.obtenerListadoActores(0);
        ActorAdapter adapter = new ActorAdapter(requireContext(), listaDeActores);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Actor actorSeleccionado = (Actor) parent.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), VistaActor.class);
            intent.putExtra("id_actor", actorSeleccionado.getId());
            startActivity(intent);
        });

        return view;
    }
}