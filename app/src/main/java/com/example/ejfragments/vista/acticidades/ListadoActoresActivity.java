package com.example.ejfragments.vista.acticidades;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ejfragments.R;

public class ListadoActoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_actores);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finishAffinity();
        }

    }

}