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

        String mostrar = getIntent().getStringExtra("mostrar");
        if ("actores".equals(mostrar)) {
            mostrarListaActores();
            setToolbarTitle("Listado de Actores");
        } else if ("peliculas".equals(mostrar)) {
            mostrarListaPeliculas();
            setToolbarTitle("Listado de Películas");
        } else {
            mostrarListaPeliculas();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String mostrar = intent.getStringExtra("mostrar");
        if ("actores".equals(mostrar)) {
            mostrarListaActores();
            setToolbarTitle("Listado de Actores");
        } else if ("peliculas".equals(mostrar)) {
            mostrarListaPeliculas();
            setToolbarTitle("Listado de Películas");
        }
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportFragmentManager().findFragmentById(R.id.main) instanceof ListaPeliculas) {
            setToolbarTitle("Listado de Películas");
        } else if (getSupportFragmentManager().findFragmentById(R.id.main) instanceof ListaActores) {
            setToolbarTitle("Listado de Actores");
        }
    }

    @Override
    public void OnPeliculasSelecionadasListener(Pelicula pelicula) {
        boolean esTablet = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        try {
            if (esTablet) {
                DatosPelicula datosPeliculaFragment = DatosPelicula.newInstance(
                        pelicula.getNombre(),
                        pelicula.getSinopsis(),
                        pelicula.getGenero(),
                        pelicula.getFecha() != null ? pelicula.getFecha().toString() : "",
                        pelicula.getImagen(),
                        pelicula.getId()
                );
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fcDetallePelicula, datosPeliculaFragment)
                        .commit();

            } else {
                setToolbarTitle("Detalle de la Película");
                Intent i = new Intent(MainActivity.this, VistaPelicula.class);
                i.putExtra("nombre", pelicula.getNombre());
                i.putExtra("fecha", pelicula.getFecha() != null ? pelicula.getFecha().toString() : "");
                i.putExtra("sinopsis", pelicula.getSinopsis());
                i.putExtra("genero", pelicula.getGenero());
                i.putExtra("imagen", pelicula.getImagen());
                i.putExtra("id", pelicula.getId());
                startActivity(i);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al mostrar detalle: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
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
            finishAffinity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void realizarSincronizacion() {
        Toast.makeText(this, "Sincronización realizada", Toast.LENGTH_SHORT).show();
    }

    private void mostrarListaPeliculas() {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, new ListaPeliculas())
                    .commit();
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar lista de películas: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void mostrarListaActores() {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, new ListaActores())
                    .commit();
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar lista de actores: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
