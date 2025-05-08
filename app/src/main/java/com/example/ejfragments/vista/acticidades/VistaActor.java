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

    // Variables para persistencia
    private AppDatabase database;
    private Actor currentActor;
    private static final String TAG = "VistaActor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
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
            Log.d(TAG, "ID del actor recibido: " + actorId);

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
        } catch (Exception e) {
            Log.e(TAG, "Error en onCreate", e);
            Toast.makeText(this, "Error al iniciar la vista del actor", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para cargar actor, priorizando el servicio REST para asegurar datos frescos
    private void cargarActor(int actorId, EditText etNombre, EditText etFecha, ImageView ivFoto) {
        if (actorId != -1) {
            Log.d(TAG, "Cargando actor con ID: " + actorId + ". Se intentará obtener del servicio REST para asegurar datos frescos.");
            // Inicializar la base de datos si aún no se ha hecho (asegúrate de que 'database' esté inicializada en onCreate)
            if (database == null) {
                database = AppDatabase.getInstance(getApplicationContext());
            }

            new Thread(() -> {
                // Siempre obtener del servicio REST
                ServicioREST.obtenerActorById(actorId, new ServicioREST.ActoresCallback() {
                    @Override
                    public void onSuccess(ArrayList<Actor> actores) {
                        if (actores != null && !actores.isEmpty()) {
                            currentActor = actores.get(0); // Tomamos el primer actor del servicio
                            Log.d(TAG, "Actor obtenido del servicio REST: " + currentActor.getNombre());
                            Log.d(TAG, "FOTO DEL SERVICIO para " + currentActor.getNombre() + " (primeros 60): " +
                                    (currentActor.getFoto() != null && currentActor.getFoto().length() > 60 ?
                                            currentActor.getFoto().substring(0, 60) : currentActor.getFoto()));

                            runOnUiThread(() -> mostrarDatosActor(currentActor, etNombre, etFecha, ivFoto));

                            // Guardar o Actualizar en Room con los datos frescos del servicio REST
                            try {
                                Log.d(TAG, "Intentando guardar/actualizar en BD al actor: " + currentActor.getNombre() +
                                        " con foto (primeros 60): " + (currentActor.getFoto() != null && currentActor.getFoto().length() > 60 ?
                                        currentActor.getFoto().substring(0,60) : currentActor.getFoto()));
                                ActorEntity actorParaGuardar = ActorEntity.fromActor(currentActor);
                                database.actorDao().insert(actorParaGuardar); // OnConflictStrategy.REPLACE actualizará la entrada existente
                                Log.d(TAG, "Actor (" + currentActor.getNombre() + ") guardado/actualizado en BD local con datos del REST.");
                            } catch (Exception e) {
                                Log.e(TAG, "Error al guardar/actualizar actor (" + currentActor.getNombre() + ") en BD local con datos del REST", e);
                            }
                        } else {
                            Log.e(TAG, "Servicio REST devolvió lista de actores vacía o null para ID: " + actorId);
                            cargarDeBdLocalSiServicioFalla(actorId, etNombre, etFecha, ivFoto); // Intento de fallback
                        }
                    }

                    @Override
                    public void onError(String mensaje) {
                        Log.e(TAG, "Error al obtener actor del servicio REST para ID: " + actorId + ". Mensaje: " + mensaje);
                        runOnUiThread(() -> Toast.makeText(VistaActor.this,
                                "Error al cargar actor del servicio: " + mensaje + ". Intentando BD local.",
                                Toast.LENGTH_LONG).show());
                        cargarDeBdLocalSiServicioFalla(actorId, etNombre, etFecha, ivFoto); // Fallback
                    }
                });
            }).start();
        } else {
            Log.e(TAG, "ID de actor inválido (-1) en cargarActor");
            Toast.makeText(this, "Error: ID de actor inválido", Toast.LENGTH_SHORT).show();
        }
    }

    // Método auxiliar para cargar desde la BD local como fallback
    private void cargarDeBdLocalSiServicioFalla(int actorId, EditText etNombre, EditText etFecha, ImageView ivFoto) {
        Log.d(TAG, "Fallback: Intentando cargar actor ID " + actorId + " desde BD local.");
        // Asegúrate de que 'database' esté inicializada
        if (database == null) {
            database = AppDatabase.getInstance(getApplicationContext());
        }
        new Thread(() -> {
            ActorEntity actorEntity = database.actorDao().getActorById(actorId);
            if (actorEntity != null) {
                Log.d(TAG, "Actor encontrado en BD local (fallback): " + actorEntity.getNombre());
                currentActor = actorEntity.toActor();
                Log.d(TAG, "FOTO DESDE BD LOCAL (fallback) para " + currentActor.getNombre() + " (primeros 60): " +
                        (currentActor.getFoto() != null && currentActor.getFoto().length() > 60 ?
                                currentActor.getFoto().substring(0, 60) : currentActor.getFoto()));
                runOnUiThread(() -> mostrarDatosActor(currentActor, etNombre, etFecha, ivFoto));
            } else {
                Log.e(TAG, "Actor ID " + actorId + " no encontrado ni en REST ni en BD local (fallback).");
                runOnUiThread(() -> {
                    Toast.makeText(VistaActor.this, "No se encontró información del actor (ID: " + actorId + ")", Toast.LENGTH_SHORT).show();
                    // Limpiar campos si no se encuentra nada
                    etNombre.setText("");
                    etFecha.setText("");
                });
            }
        }).start();
    }

    // Método para mostrar datos de actor en UI
    private void mostrarDatosActor(Actor actor, EditText etNombre, EditText etFecha, ImageView ivFoto) {
        try {
            if (actor == null) {
                Log.e(TAG, "El actor es null en mostrarDatosActor. No se pueden mostrar datos.");
                Toast.makeText(this, "Error: Datos de actor no disponibles", Toast.LENGTH_SHORT).show();
                etNombre.setText("");
                etFecha.setText("");
                return;
            }

            Log.d(TAG, "Mostrando datos del actor en detalle: " + actor.getNombre());

            // Establecer nombre
            etNombre.setText(actor.getNombre());

            // Establecer fecha
            if (actor.getFechaNacimiento() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String fechaFormateada = sdf.format(actor.getFechaNacimiento());
                etFecha.setText(fechaFormateada);
                Log.d(TAG, "Fecha de nacimiento formateada para " + actor.getNombre() + ": " + fechaFormateada);
            } else {
                etFecha.setText("");
                Log.d(TAG, "Fecha de nacimiento es null para " + actor.getNombre());
            }

            // Cargar la imagen desde Base64
            String fotoActorBase64 = actor.getFoto();

            // Logs para depurar la cadena Base64 ANTES de intentar convertirla
            Log.d(TAG, "Actor (detalle): " + actor.getNombre() + " - Foto String (primeros 60 chars): " +
                    (fotoActorBase64 != null && fotoActorBase64.length() > 60 ? fotoActorBase64.substring(0, 60) + "..." : fotoActorBase64));
            Log.d(TAG, "Actor (detalle): " + actor.getNombre() + " - Foto String es null? " + (fotoActorBase64 == null));
            if (fotoActorBase64 != null) {
                Log.d(TAG, "Actor (detalle): " + actor.getNombre() + " - Foto String está vacía? " + fotoActorBase64.isEmpty());
            }


            if (fotoActorBase64 != null && !fotoActorBase64.isEmpty()) {
                Log.d(TAG, "Intentando convertir imagen base64 a bitmap para (detalle): " + actor.getNombre());
                Bitmap bitmap = ServicioREST.base64ToBitmap(fotoActorBase64);

                if (bitmap != null) {
                    Log.d(TAG, "Imagen convertida correctamente para (detalle): " + actor.getNombre());
                    ivFoto.setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "La conversión de Base64 a bitmap devolvió null para (detalle): " + actor.getNombre() + ". Usando imagen por defecto.");
                }
            } else {
                Log.d(TAG, "No hay cadena de foto (Base64) para mostrar para (detalle): " + actor.getNombre() + ". Usando imagen por defecto.");
            }

        } catch (Exception e) {
            Log.e(TAG, "Error al mostrar datos del actor: " + (actor != null ? actor.getNombre() : "Actor NULO"), e);
            Toast.makeText(this, "Error al mostrar datos del actor", Toast.LENGTH_SHORT).show();
            // Asegurarse de que los campos se limpian o se pone un valor por defecto si hay error
            etNombre.setText("Error");
            etFecha.setText("");
        }
    }

    // Método para guardar actor en Room
    private void guardarActor(EditText etNombre, EditText etFecha) {
        try {
            if (currentActor != null) {
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
                        Log.d(TAG, "Actor actualizado en la base de datos");

                        // Actualizar UI en hilo principal
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
                Log.e(TAG, "currentActor es null, no se puede guardar");
                Toast.makeText(this, "Error: No hay datos de actor para guardar", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error al parsear fecha", e);
            Toast.makeText(this, "Error en el formato de la fecha", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error general al guardar actor", e);
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