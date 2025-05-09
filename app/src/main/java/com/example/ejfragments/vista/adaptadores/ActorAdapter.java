package com.example.ejfragments.vista.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ejfragments.R;
import com.example.ejfragments.api.ServicioREST;
import com.example.ejfragments.modelo.entidades.Actor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ActorAdapter extends android.widget.ArrayAdapter<Actor> {

    public ActorAdapter(@NonNull Context context, @NonNull ArrayList<Actor> objects) {
        super(context, 0, objects);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.actor_element, parent, false);
            holder = new ViewHolder();
            holder.tvNombre = convertView.findViewById(R.id.tvActorNombre);
            holder.tvFecha = convertView.findViewById(R.id.tvActorFecha);
            holder.ivFoto = convertView.findViewById(R.id.ivActorFoto);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Actor actor = getItem(position);

        if (actor != null) {
            holder.tvNombre.setText(actor.getNombre());

            if (actor.getFechaNacimiento() != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String fechaFormateada = sdf.format(actor.getFechaNacimiento());
                    holder.tvFecha.setText(fechaFormateada);
                } catch (Exception e) {
                    holder.tvFecha.setText("Fecha desconocida");
                }
            } else {
                holder.tvFecha.setText("Fecha desconocida");
            }

            String fotoActorBase64 = actor.getFoto();
            holder.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery);
                
            if (fotoActorBase64 != null && !fotoActorBase64.isEmpty()) {
                try {
                    Bitmap bitmap = ServicioREST.base64ToBitmap(fotoActorBase64);
                    if (bitmap != null) {
                        holder.ivFoto.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            holder.tvNombre.setText("Actor desconocido");
            holder.tvFecha.setText("");
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvNombre;
        TextView tvFecha;
        ImageView ivFoto;
    }
}
