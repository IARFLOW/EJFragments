// Nombre Archivo: ListadoActoresActivity.java (CORREGIDO)
package com.example.ejfragments.vista.acticidades;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ejfragments.R;
import com.example.ejfragments.vista.fragmentos.ListaActores; // Importa tu fragmento

public class ListadoActoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_actores);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    // Reemplaza el contenido del FrameLayout con el fragmento ListaActores
                    .replace(R.id.contenedor_fragmento_actores, new ListaActores())
                    .commit();
        }

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finishAffinity();
        }
    }
}