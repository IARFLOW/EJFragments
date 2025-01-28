package com.example.ejfragments.vista.acticidades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ejfragments.R;
import com.example.ejfragments.vista.fragmentos.ListaActores;

public class ListadoActoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_actores);

        // Verificar si se debe cerrar la app
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finishAffinity();
        }

        // ... resto del código
    }

    // Método para cerrar desde otras actividades
    public static void cerrarAplicacion(Context context) {
        Intent intent = new Intent(context, ListadoActoresActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        context.startActivity(intent);
    }

}