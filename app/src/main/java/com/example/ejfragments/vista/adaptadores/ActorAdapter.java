package com.example.ejfragments.vista.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
    private static final String TAG = "ActorAdapter";

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
        // ViewHolder pattern para optimizar el rendimiento del ListView
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
            // Establecer nombre
            holder.tvNombre.setText(actor.getNombre());
            Log.d(TAG, "Mostrando actor en lista: " + actor.getNombre() + " (ID: " + actor.getId() + ")");

            // Establecer fecha formateada
            if (actor.getFechaNacimiento() != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String fechaFormateada = sdf.format(actor.getFechaNacimiento());
                    holder.tvFecha.setText(fechaFormateada);
                } catch (Exception e) {
                    Log.e(TAG, "Error al formatear fecha para " + actor.getNombre() + ": " + e.getMessage());
                    holder.tvFecha.setText("Fecha desconocida");
                }
            } else {
                holder.tvFecha.setText("Fecha desconocida");
            }

            // Cargar imagen
            String fotoActorBase64 = actor.getFoto();

            // Logs para depurar la cadena Base64 ANTES de intentar convertirla
            Log.d(TAG, "Actor: " + actor.getNombre() + " - Foto String (primeros 60 chars): " +
                    (fotoActorBase64 != null && fotoActorBase64.length() > 60 ? fotoActorBase64.substring(0, 60) + "..." : fotoActorBase64));
            Log.d(TAG, "Actor: " + actor.getNombre() + " - Foto String es null? " + (fotoActorBase64 == null));
            if (fotoActorBase64 != null) {
                Log.d(TAG, "Actor: " + actor.getNombre() + " - Foto String está vacía? " + fotoActorBase64.isEmpty());
            }

            // Establecer una imagen predeterminada primero (en caso de error)
            holder.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery);
                
            if (fotoActorBase64 != null && !fotoActorBase64.isEmpty()) {
                try {
                    Bitmap bitmap = ServicioREST.base64ToBitmap(fotoActorBase64);
                    if (bitmap != null) {
                        holder.ivFoto.setImageBitmap(bitmap);
                    } else {
                        // Si la conversión Base64 falló (bitmap es null)
                        Log.w(TAG, "No se pudo convertir Base64 a Bitmap para el actor: " + actor.getNombre() + ". Usando imagen por defecto.");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Excepción al procesar imagen para " + actor.getNombre() + ": " + e.getMessage());
                }
            } else {
                // Si no hay cadena Base64 para la foto
                Log.w(TAG, "No hay cadena de foto (Base64) para el actor: " + actor.getNombre() + ". Usando imagen por defecto.");
            }
        } else {
            // Si el objeto actor es nulo (no debería pasar si la lista está bien)
            holder.tvNombre.setText("Actor desconocido");
            holder.tvFecha.setText("");
        }

        return convertView;
    }

    // ViewHolder class para optimización
    private static class ViewHolder {
        TextView tvNombre;
        TextView tvFecha;
        ImageView ivFoto;
    }
}