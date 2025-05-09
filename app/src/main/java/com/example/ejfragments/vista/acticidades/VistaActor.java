package com.example.ejfragments.vista.acticidades;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
import com.example.ejfragments.api.ServicioREST;
import com.example.ejfragments.modelo.entidades.Actor;
import com.example.ejfragments.modelo.database.AppDatabase;
import com.example.ejfragments.modelo.entidades.ActorEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VistaActor extends AppCompatActivity {

    private AppDatabase database;
    private Actor currentActor;
    private static final String TAG = "VistaActor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_vista_actor);

            database = AppDatabase.getInstance(this);

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

            cargarActor(actorId, etActorNombre, etActorFecha, ivFoto);

            btEditar.setOnClickListener(view -> {
                etActorNombre.setEnabled(true);
                etActorFecha.setEnabled(true);
                btGuardar.setEnabled(true);
            });

            btGuardar.setOnClickListener(view -> {
                guardarActor(etActorNombre, etActorFecha);
            });
        } catch (Exception e) {
            Log.e(TAG, "Error en onCreate", e);
            Toast.makeText(this, "Error al iniciar la vista del actor", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarActor(int actorId, EditText etNombre, EditText etFecha, ImageView ivFoto) {
        if (actorId != -1) {
            if (database == null) {
                database = AppDatabase.getInstance(getApplicationContext());
            }

            new Thread(() -> {
                ServicioREST.obtenerActorById(actorId, new ServicioREST.ActoresCallback() {
                    @Override
                    public void onSuccess(ArrayList<Actor> actores) {
                        if (actores != null && !actores.isEmpty()) {
                            currentActor = actores.get(0);
                            runOnUiThread(() -> mostrarDatosActor(currentActor, etNombre, etFecha, ivFoto));

                            try {
                                ActorEntity actorParaGuardar = ActorEntity.fromActor(currentActor);
                                database.actorDao().insert(actorParaGuardar);
                            } catch (Exception e) {
                                Log.e(TAG, "Error al guardar actor en BD local", e);
                            }
                        } else {
                            cargarDeBdLocalSiServicioFalla(actorId, etNombre, etFecha, ivFoto);
                        }
                    }

                    @Override
                    public void onError(String mensaje) {
                        runOnUiThread(() -> Toast.makeText(VistaActor.this,
                                "Error al cargar actor del servicio. Intentando BD local.",
                                Toast.LENGTH_LONG).show());
                        cargarDeBdLocalSiServicioFalla(actorId, etNombre, etFecha, ivFoto);
                    }
                });
            }).start();
        } else {
            Toast.makeText(this, "Error: ID de actor inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarDeBdLocalSiServicioFalla(int actorId, EditText etNombre, EditText etFecha, ImageView ivFoto) {
        if (database == null) {
            database = AppDatabase.getInstance(getApplicationContext());
        }
        new Thread(() -> {
            ActorEntity actorEntity = database.actorDao().getActorById(actorId);
            if (actorEntity != null) {
                currentActor = actorEntity.toActor();
                runOnUiThread(() -> mostrarDatosActor(currentActor, etNombre, etFecha, ivFoto));
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(VistaActor.this, "No se encontró información del actor", Toast.LENGTH_SHORT).show();
                    etNombre.setText("");
                    etFecha.setText("");
                });
            }
        }).start();
    }

    private void mostrarDatosActor(Actor actor, EditText etNombre, EditText etFecha, ImageView ivFoto) {
        try {
            if (actor == null) {
                Toast.makeText(this, "Error: Datos de actor no disponibles", Toast.LENGTH_SHORT).show();
                etNombre.setText("");
                etFecha.setText("");
                return;
            }

            etNombre.setText(actor.getNombre());

            if (actor.getFechaNacimiento() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String fechaFormateada = sdf.format(actor.getFechaNacimiento());
                etFecha.setText(fechaFormateada);
            } else {
                etFecha.setText("");
            }

            String fotoActorBase64 = actor.getFoto();

            if (fotoActorBase64 != null && !fotoActorBase64.isEmpty()) {
                Bitmap bitmap = ServicioREST.base64ToBitmap(fotoActorBase64);

                if (bitmap != null) {
                    ivFoto.setImageBitmap(bitmap);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Error al mostrar datos del actor", e);
            Toast.makeText(this, "Error al mostrar datos del actor", Toast.LENGTH_SHORT).show();
            etNombre.setText("Error");
            etFecha.setText("");
        }
    }

    private void guardarActor(EditText etNombre, EditText etFecha) {
        try {
            if (currentActor != null) {
                currentActor.setNombre(etNombre.getText().toString());

                String fechaStr = etFecha.getText().toString();
                if (!fechaStr.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date fechaNacimiento = sdf.parse(fechaStr);
                    currentActor.setFechaNacimiento(fechaNacimiento);
                }

                new Thread(() -> {
                    try {
                        ActorEntity actorEntity = ActorEntity.fromActor(currentActor);
                        database.actorDao().update(actorEntity);

                        runOnUiThread(() -> {
                            etNombre.setEnabled(false);
                            etFecha.setEnabled(false);
                            findViewById(R.id.btGuardar).setEnabled(false);

                            Toast.makeText(this, "Actor guardado en la base de datos", Toast.LENGTH_SHORT).show();
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Error al guardar actor en BD", e);
                        runOnUiThread(() ->
                                Toast.makeText(this, "Error al guardar actor", Toast.LENGTH_SHORT).show()
                        );
                    }
                }).start();
            } else {
                Toast.makeText(this, "Error: No hay datos de actor para guardar", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Error en el formato de la fecha", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar actor", Toast.LENGTH_SHORT).show();
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