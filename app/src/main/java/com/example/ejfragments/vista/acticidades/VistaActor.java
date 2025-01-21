package com.example.ejfragments.vista.acticidades;

import android.os.Bundle;
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

    private EditText etActorNombre, etActorFecha;
    private Button btEditar, btGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_actor);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarActor);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Vistas
        ImageView ivFoto = findViewById(R.id.ivActorFoto);
        etActorNombre = findViewById(R.id.etActorNombre);
        etActorFecha = findViewById(R.id.etActorFecha);
        btEditar = findViewById(R.id.btEditar);
        btGuardar = findViewById(R.id.btGuardar);

        // Recogemos el ID de actor
        int actorId = getIntent().getIntExtra("id_actor", -1);
        Actor actor = new ObtencionDatos().obtenerActor(actorId);

        if (actor != null) {
            // Formatear la fecha
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaFormateada = sdf.format(actor.getFechaNacimiento());
            // Rellenamos los campos
            etActorNombre.setText(actor.getNombre());
            etActorFecha.setText(fechaFormateada);
        }

        // Botón Editar: habilita los campos y el botón Guardar
        btEditar.setOnClickListener(view -> {
            etActorNombre.setEnabled(true);
            etActorFecha.setEnabled(true);
            btGuardar.setEnabled(true);
        });

        // Botón Guardar: deshabilita de nuevo y simula guardado
        btGuardar.setOnClickListener(view -> {
            etActorNombre.setEnabled(false);
            etActorFecha.setEnabled(false);
            btGuardar.setEnabled(false);

            // Aquí podrías “actualizar” el mock o lo que quieras
            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
        });
    }

    // Flecha atrás
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
