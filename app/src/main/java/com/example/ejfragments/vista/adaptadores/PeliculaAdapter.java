package com.example.ejfragments.vista.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ejfragments.R;
import com.example.ejfragments.api.ServicioREST;
import com.example.ejfragments.modelo.entidades.Pelicula;

import java.util.ArrayList;

public class PeliculaAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Pelicula> listaPeliculas;

    public PeliculaAdapter(Context context, ArrayList<Pelicula> listaPeliculas) {
        this.context = context;
        this.listaPeliculas = listaPeliculas;
    }

    @Override
    public int getCount() {
        return listaPeliculas.size();
    }

    @Override
    public Pelicula getItem(int position) {
        return listaPeliculas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.pelicula_element, parent, false);
        }


        Pelicula pelicula = getItem(position);

        TextView tvNombre = convertView.findViewById(R.id.tvNombrePelicula);
        TextView tvGenero = convertView.findViewById(R.id.tvGeneroPelicula);
        ImageView ivImagen = convertView.findViewById(R.id.ivImagenPelicula);

        tvNombre.setText(pelicula.getNombre());
        tvGenero.setText(pelicula.getGenero());
        
        // Convertir imagen de Base64 a Bitmap si existe
        try {
            if (pelicula.getImagen() != null && !pelicula.getImagen().isEmpty()) {
                Bitmap bitmap = ServicioREST.base64ToBitmap(pelicula.getImagen());
                if (bitmap != null) {
                    ivImagen.setImageBitmap(bitmap);
                }
            }
        } catch (Exception e) {
            Log.e("PeliculaAdapter", "Error al cargar imagen", e);
            // No hacemos nada más, simplemente dejamos la imagen por defecto
        }

        return convertView;
    }
}
