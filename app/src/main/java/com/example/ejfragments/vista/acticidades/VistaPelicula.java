package com.example.ejfragments.vista.acticidades;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ejfragments.R;
import com.example.ejfragments.vista.fragmentos.DatosPelicula;

public class VistaPelicula extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vista_pelicula);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detalle de la Película");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String nombre = getIntent().getStringExtra("nombre");
        String sinopsis = getIntent().getStringExtra("sinopsis");
        String genero = getIntent().getStringExtra("genero");
        String fecha = getIntent().getStringExtra("fecha");
        String imagen = getIntent().getStringExtra("imagen");
        int idPelicula = getIntent().getIntExtra("id", -1);

        DatosPelicula datosPeliculaFragment = DatosPelicula.newInstance(nombre, sinopsis, genero, fecha, imagen, idPelicula);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fcDetallePelicula, datosPeliculaFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_actores) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("mostrar", "actores");
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_peliculas) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("mostrar", "peliculas");
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_salir) {
            finishAffinity(); 
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acciones, menu);
        return true;
    }
}