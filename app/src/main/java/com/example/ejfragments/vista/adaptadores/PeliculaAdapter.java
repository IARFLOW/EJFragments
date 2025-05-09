package com.example.ejfragments.vista.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ejfragments.R;
import com.example.ejfragments.api.ServicioREST;
import com.example.ejfragments.modelo.entidades.Pelicula;

import java.util.List;

public class PeliculaAdapter extends ArrayAdapter<Pelicula> {

    public PeliculaAdapter(Context context, List<Pelicula> peliculas) {
        super(context, 0, peliculas);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.pelicula_element, parent, false);
        }

        Pelicula pelicula = getItem(position);

        if (pelicula != null) {
            TextView tvNombrePelicula = listItemView.findViewById(R.id.tvNombrePelicula);
            TextView tvGeneroPelicula = listItemView.findViewById(R.id.tvGeneroPelicula);
            ImageView ivImagenPelicula = listItemView.findViewById(R.id.ivImagenPelicula);

            tvNombrePelicula.setText(pelicula.getNombre());
            tvGeneroPelicula.setText(pelicula.getGenero());

            String posterBase64 = pelicula.getImagen();
            if (posterBase64 != null && !posterBase64.isEmpty()) {
                try {
                    Bitmap bitmap = ServicioREST.base64ToBitmap(posterBase64);
                    if (bitmap != null) {
                        ivImagenPelicula.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return listItemView;
    }
}