// Nombre Archivo: ListadoActoresActivity.java (CORREGIDO)
package com.example.ejfragments.vista.acticidades;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Opcional: Si quieres añadir una Toolbar aquí

import com.example.ejfragments.R;
import com.example.ejfragments.vista.fragmentos.ListaActores; // Importa tu fragmento

public class ListadoActoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el layout CORREGIDO que ahora tiene el FrameLayout
        setContentView(R.layout.activity_listado_actores);

        // Opcional: Configurar una Toolbar si quieres una barra de título específica aquí
        // Toolbar toolbar = findViewById(R.id.tu_toolbar_id_si_la_anades_al_xml);
        // setSupportActionBar(toolbar);
        // if (getSupportActionBar() != null) {
        //     getSupportActionBar().setTitle("Listado General de Actores");
        //     getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Para el botón de atrás
        // }


        // Comprobar si ya hay un fragmento (por ejemplo si la pantalla rota)
        // Si no hay fragmento, crear e insertar el fragmento ListaActores
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    // Reemplaza el contenido del FrameLayout con el fragmento ListaActores
                    .replace(R.id.contenedor_fragmento_actores, new ListaActores())
                    .commit();
        }

        // Código para salir si viene del menú (esto ya lo tenías)
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finishAffinity();
        }
    }

    // Opcional: Manejar el clic en el botón de atrás de la Toolbar (si la añadiste)
    // @Override
    // public boolean onOptionsItemSelected(MenuItem item) {
    //     if (item.getItemId() == android.R.id.home) {
    //         finish(); // Cierra esta actividad y vuelve a la anterior
    //         return true;
    //     }
    //     return super.onOptionsItemSelected(item);
    // }
}