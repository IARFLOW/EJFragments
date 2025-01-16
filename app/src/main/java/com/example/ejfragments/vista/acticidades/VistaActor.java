package com.example.ejfragments.vista.acticidades;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ejfragments.R;
import com.example.ejfragments.mock.ObtencionDatos;
import com.example.ejfragments.modelo.entidades.Actor;

public class VistaActor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_actor);

        int actorId = getIntent().getIntExtra("id_actor", 0);
        Actor actor = new ObtencionDatos().obtenerActor(actorId);

        if (actor != null) {
            TextView tvNombre = findViewById(R.id.tvActorNombre);
            TextView tvFechaNacimiento = findViewById(R.id.tvActorFecha);
            //ImageView ivFoto = findViewById(R.id.ivActorFoto);

            tvNombre.setText(actor.getNombre());
            tvFechaNacimiento.setText(actor.getFechaNacimiento().toString());
        }
    }
}
