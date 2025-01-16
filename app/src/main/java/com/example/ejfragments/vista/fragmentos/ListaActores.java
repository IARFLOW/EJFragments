package com.example.ejfragments.vista.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ejfragments.R;
import com.example.ejfragments.mock.ObtencionDatos;
import com.example.ejfragments.modelo.entidades.Actor;

import java.util.ArrayList;

public class ListaActores extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_actores, container, false);

        ListView listView = view.findViewById(R.id.lista_actores);

        ObtencionDatos datos = new ObtencionDatos();
        ArrayList<Actor> listaDeActores = datos.obtenerListadoActores(0); // Obtenemos todos los actores

        ArrayAdapter<Actor> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1, // Cambia esto si necesitas algo más personalizado
                listaDeActores
        );

        listView.setAdapter(adapter);

        return view;
    }

}
