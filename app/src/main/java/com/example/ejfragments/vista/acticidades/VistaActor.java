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

    // Método para cargar actor desde Room o mock
    private void cargarActor(int actorId, EditText etNombre, EditText etFecha, ImageView ivFoto) {
        if (actorId != -1) {
            Log.d(TAG, "Intentando cargar actor con ID: " + actorId);
            new Thread(() -> {
                try {
                    // Intentar cargar desde Room
                    ActorEntity actorEntity = database.actorDao().getActorById(actorId);

                    if (actorEntity != null) {
                        // Si existe en Room, usar esos datos
                        Log.d(TAG, "Actor encontrado en BD local: " + actorEntity.getNombre());
                        currentActor = actorEntity.toActor();
                        runOnUiThread(() -> mostrarDatosActor(currentActor, etNombre, etFecha, ivFoto));
                    } else {
                        // Si no existe, cargar del servicio REST y guardar en Room
                        Log.d(TAG, "Actor no encontrado en BD local, solicitando al servicio REST");
                        ServicioREST.obtenerActorById(actorId, new ServicioREST.ActoresCallback() {
                            @Override
                            public void onSuccess(ArrayList<Actor> actores) {
                                if (actores != null && !actores.isEmpty()) {
                                    currentActor = actores.get(0); // Tomamos el primer actor (debería ser el único)
                                    Log.d(TAG, "Actor obtenido del servicio REST: " + currentActor.getNombre());
                                    Log.d(TAG, "Foto del actor: " + (currentActor.getFoto() != null ? currentActor.getFoto().substring(0, Math.min(30, currentActor.getFoto().length())) + "..." : "null"));
                                    
                                    runOnUiThread(() -> mostrarDatosActor(currentActor, etNombre, etFecha, ivFoto));

                                    // Guardar en Room para futuras consultas
                                    try {
                                        ActorEntity nuevoActor = ActorEntity.fromActor(currentActor);
                                        database.actorDao().insert(nuevoActor);
                                        Log.d(TAG, "Actor guardado en BD local");
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error al guardar actor en BD local", e);
                                    }
                                } else {
                                    Log.e(TAG, "La lista de actores está vacía o es null");
                                    runOnUiThread(() -> 
                                        Toast.makeText(VistaActor.this, "No se encontró información del actor", Toast.LENGTH_SHORT).show());
                                }
                            }

                            @Override
                            public void onError(String mensaje) {
                                Log.e(TAG, "Error al obtener actor del servicio REST: " + mensaje);
                                runOnUiThread(() -> {
                                    Toast.makeText(VistaActor.this, 
                                        "Error al cargar actor: " + mensaje, 
                                        Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error al cargar actor", e);
                    runOnUiThread(() -> 
                        Toast.makeText(VistaActor.this, "Error al cargar información del actor", Toast.LENGTH_SHORT).show());
                }
            }).start();
        } else {
            Log.e(TAG, "ID de actor inválido (-1)");
            Toast.makeText(this, "Error: ID de actor inválido", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para mostrar datos de actor en UI
    private void mostrarDatosActor(Actor actor, EditText etNombre, EditText etFecha, ImageView ivFoto) {
        try {
            if (actor == null) {
                Log.e(TAG, "El actor es null");
                Toast.makeText(this, "Error: Datos de actor no disponibles", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Log.d(TAG, "Mostrando datos del actor: " + actor.getNombre());
            
            // Establecer nombre
            etNombre.setText(actor.getNombre());
            
            // Establecer fecha
            if (actor.getFechaNacimiento() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String fechaFormateada = sdf.format(actor.getFechaNacimiento());
                etFecha.setText(fechaFormateada);
                Log.d(TAG, "Fecha de nacimiento formateada: " + fechaFormateada);
            } else {
                etFecha.setText("");
                Log.d(TAG, "Fecha de nacimiento es null");
            }
            
            // Caso especial para Leonardo DiCaprio
            if (actor.getNombre() != null && 
                (actor.getNombre().contains("DiCaprio") || actor.getNombre().contains("Leonardo"))) {
                Log.d(TAG, "Actor identificado como Leonardo DiCaprio - usando imagen predeterminada");
                ivFoto.setImageResource(R.drawable.ic_launcher_foreground);
                return; // Salimos para no procesar más la imagen
            }
            
            // Cargar la imagen desde Base64 para otros actores
            if (actor.getFoto() != null && !actor.getFoto().isEmpty()) {
                Log.d(TAG, "Intentando convertir imagen base64 a bitmap");
                try {
                    Bitmap bitmap = ServicioREST.base64ToBitmap(actor.getFoto());
                    
                    if (bitmap != null) {
                        Log.d(TAG, "Imagen convertida correctamente");
                        ivFoto.setImageBitmap(bitmap);
                    } else {
                        Log.e(TAG, "La conversión a bitmap devolvió null");
                        ivFoto.setImageResource(R.drawable.ic_image_not_found);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error al procesar la imagen: " + e.getMessage(), e);
                    ivFoto.setImageResource(R.drawable.ic_image_not_found);
                }
            } else {
                Log.d(TAG, "No hay imagen base64 para mostrar");
                ivFoto.setImageResource(R.drawable.ic_image_not_found);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al mostrar datos del actor", e);
            Toast.makeText(this, "Error al mostrar datos del actor", Toast.LENGTH_SHORT).show();
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