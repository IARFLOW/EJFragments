package com.example.ejfragments.vista.acticidades;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ejfragments.R;
import com.example.ejfragments.modelo.entidades.Pelicula;
import com.example.ejfragments.vista.fragmentos.DatosPelicula;
import com.example.ejfragments.vista.fragmentos.ListaPeliculas;

public class MainActivity extends AppCompatActivity implements ListaPeliculas.OnPeliculasSelecionadasListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void OnPeliculasSelecionadasListener(Pelicula pelicula) {
        boolean esTablet = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        if (esTablet) {
            DatosPelicula datosPeliculaFragment = DatosPelicula.newInstance(
                    pelicula.getNombre(),
                    pelicula.getSinopsis(),
                    pelicula.getFecha().toString(),
                    pelicula.getGenero(),
                    null
            );
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fcDetallePelicula, datosPeliculaFragment)
                    .commit();

        } else {
            Intent i = new Intent(MainActivity.this, VistaPelicula.class);
            i.putExtra("nombre", pelicula.getNombre());
            i.putExtra("fecha", pelicula.getFecha().toString());
            i.putExtra("sinopsis", pelicula.getSinopsis());
            i.putExtra("genero", pelicula.getGenero());
            i.putExtra("imagen", pelicula.getImagen());
            startActivity(i);
        }
    }
}