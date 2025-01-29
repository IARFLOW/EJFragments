package com.example.ejfragments.vista.acticidades;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ejfragments.R;
import com.example.ejfragments.mock.ObtencionDatos;
import com.example.ejfragments.modelo.entidades.Actor;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class VistaActor extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_actor);

        ImageView ivFoto = findViewById(R.id.ivActorFoto);
        EditText etActorNombre = findViewById(R.id.etActorNombre);
        EditText etActorFecha = findViewById(R.id.etActorFecha);
        Button btEditar = findViewById(R.id.btEditar);
        Button btGuardar = findViewById(R.id.btGuardar);

        Toolbar toolbar = findViewById(R.id.toolbarActor);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int actorId = getIntent().getIntExtra("id_actor", -1);
        Actor actor = new ObtencionDatos().obtenerActor(actorId);

        if (actor != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaFormateada = sdf.format(actor.getFechaNacimiento());
            etActorNombre.setText(actor.getNombre());
            etActorFecha.setText(fechaFormateada);
        }

        btEditar.setOnClickListener(view -> {
            etActorNombre.setEnabled(true);
            etActorFecha.setEnabled(true);
            btGuardar.setEnabled(true);
        });

        btGuardar.setOnClickListener(view -> {
            etActorNombre.setEnabled(false);
            etActorFecha.setEnabled(false);
            btGuardar.setEnabled(false);

            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acciones, menu);
        return true;
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

}
