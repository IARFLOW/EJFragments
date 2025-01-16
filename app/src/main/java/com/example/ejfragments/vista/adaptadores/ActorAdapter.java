package com.example.ejfragments.vista.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ejfragments.R;
import com.example.ejfragments.modelo.entidades.Actor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ActorAdapter extends android.widget.ArrayAdapter<Actor> {

    public ActorAdapter(@NonNull Context context, @NonNull ArrayList<Actor> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.actor_element, parent, false);
        }

        Actor actor = getItem(position);

        TextView tvNombre = convertView.findViewById(R.id.tvActorNombre);
        TextView tvFecha = convertView.findViewById(R.id.tvActorFecha);

        if (actor != null) {
            tvNombre.setText(actor.getNombre());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaFormateada = sdf.format(actor.getFechaNacimiento());

            tvFecha.setText(fechaFormateada);
        }

        return convertView;
    }


}
