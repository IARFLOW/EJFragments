package com.example.ejfragments.vista.acticidades;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ejfragments.R;
import com.example.ejfragments.modelo.entidades.Pelicula;
import com.example.ejfragments.vista.fragmentos.DatosPelicula;
import com.example.ejfragments.vista.fragmentos.ListaActores;
import com.example.ejfragments.vista.fragmentos.ListaPeliculas;

public class MainActivity extends AppCompatActivity implements ListaPeliculas.OnPeliculasSelecionadasListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Listado de Películas");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
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
            setToolbarTitle("Detalle de la Película");
            Intent i = new Intent(MainActivity.this, VistaPelicula.class);
            i.putExtra("nombre", pelicula.getNombre());
            i.putExtra("fecha", pelicula.getFecha().toString());
            i.putExtra("sinopsis", pelicula.getSinopsis());
            i.putExtra("genero", pelicula.getGenero());
            i.putExtra("imagen", pelicula.getImagen());
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_peliculas) {
            mostrarListaPeliculas();
            setToolbarTitle("Listado de Películas");
            return true;
        } else if (id == R.id.menu_actores) {
            mostrarListaActores();
            setToolbarTitle("Listado de Actores");
            return true;
        } else if (id == R.id.menu_sincronizar) {
            realizarSincronizacion();
            return true;
        } else if (id == R.id.menu_salir) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void realizarSincronizacion() {
        Toast.makeText(this, "Sincronización realizada", Toast.LENGTH_SHORT).show();
    }

    private void mostrarListaPeliculas() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new ListaPeliculas())
                .commit();
    }

    private void mostrarListaActores() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new ListaActores())
                .commit();
    }
}