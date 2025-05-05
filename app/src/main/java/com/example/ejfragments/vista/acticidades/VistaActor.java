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
import com.example.ejfragments.modelo.database.AppDatabase;
import com.example.ejfragments.modelo.entidades.ActorEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VistaActor extends AppCompatActivity {

    // Variables para persistencia
    private AppDatabase database;
    private Actor currentActor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_actor);

        // Inicializar base de datos
        database = AppDatabase.getInstance(this);

        // Referencias a los controles de la UI
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

        // Obtener ID del actor
        int actorId = getIntent().getIntExtra("id_actor", -1);

        // Cargar actor desde Room o mock
        cargarActor(actorId, etActorNombre, etActorFecha, ivFoto);

        // Botón editar
        btEditar.setOnClickListener(view -> {
            etActorNombre.setEnabled(true);
            etActorFecha.setEnabled(true);
            btGuardar.setEnabled(true);
        });

        // Botón guardar
        btGuardar.setOnClickListener(view -> {
            guardarActor(etActorNombre, etActorFecha);
        });
    }

    // Método para cargar actor desde Room o mock
    private void cargarActor(int actorId, EditText etNombre, EditText etFecha, ImageView ivFoto) {
        if (actorId != -1) {
            new Thread(() -> {
                try {
                    // Intentar cargar desde Room
                    ActorEntity actorEntity = database.actorDao().getActorById(actorId);

                    if (actorEntity != null) {
                        // Si existe en Room, usar esos datos
                        currentActor = actorEntity.toActor();
                        runOnUiThread(() -> mostrarDatosActor(currentActor, etNombre, etFecha, ivFoto));
                    } else {
                        // Si no existe, cargar del mock y guardar en Room
                        currentActor = new ObtencionDatos().obtenerActor(actorId);
                        if (currentActor != null) {
                            runOnUiThread(() -> mostrarDatosActor(currentActor, etNombre, etFecha, ivFoto));

                            // Guardar en Room para futuras consultas
                            ActorEntity nuevoActor = ActorEntity.fromActor(currentActor);
                            database.actorDao().insert(nuevoActor);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // Método para mostrar datos de actor en UI
    private void mostrarDatosActor(Actor actor, EditText etNombre, EditText etFecha, ImageView ivFoto) {
        etNombre.setText(actor.getNombre());
        if (actor.getFechaNacimiento() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaFormateada = sdf.format(actor.getFechaNacimiento());
            etFecha.setText(fechaFormateada);
        }
        // Puedes cargar la imagen aquí si tienes el recurso
        // ivFoto.setImageResource(R.drawable.actor_default);
    }

    // Método para guardar actor en Room
    private void guardarActor(EditText etNombre, EditText etFecha) {
        if (currentActor != null) {
            try {
                // Actualizar datos del actor
                currentActor.setNombre(etNombre.getText().toString());

                // Parsear fecha
                String fechaStr = etFecha.getText().toString();
                if (!fechaStr.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date fechaNacimiento = sdf.parse(fechaStr);
                    currentActor.setFechaNacimiento(fechaNacimiento);
                }

                // Guardar en Room en un hilo secundario
                new Thread(() -> {
                    try {
                        // Convertir Actor a ActorEntity
                        ActorEntity actorEntity = ActorEntity.fromActor(currentActor);

                        // Guardar en la base de datos
                        database.actorDao().update(actorEntity);

                        // Actualizar UI en hilo principal
                        runOnUiThread(() -> {
                            etNombre.setEnabled(false);
                            etFecha.setEnabled(false);
                            findViewById(R.id.btGuardar).setEnabled(false);

                            Toast.makeText(this, "Actor guardado en la base de datos", Toast.LENGTH_SHORT).show();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(this, "Error al guardar actor", Toast.LENGTH_SHORT).show()
                        );
                    }
                }).start();
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error en el formato de la fecha", Toast.LENGTH_SHORT).show();
            }
        }
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