package com.example.ejfragments.vista.acticidades;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejfragments.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SeleccionFechaActivity extends AppCompatActivity {

    private EditText etFecha, etHora;
    private final Calendar fechaHoraSeleccionada = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_fecha);

        CalendarView calendarView = findViewById(R.id.calendarView);
        Button btSeleccionHora = findViewById(R.id.btSeleccionHora);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        Button btAsignar = findViewById(R.id.btAsignar);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fechaHoraSeleccionada.set(year, month, dayOfMonth);
            actualizarFechaEditText();
        });

        btSeleccionHora.setOnClickListener(v -> mostrarTimePickerDialog());

        btAsignar.setOnClickListener(v -> devolverFechaHora());
    }

    private void mostrarTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    fechaHoraSeleccionada.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    fechaHoraSeleccionada.set(Calendar.MINUTE, minute);
                    actualizarHoraEditText();
                },
                fechaHoraSeleccionada.get(Calendar.HOUR_OF_DAY),
                fechaHoraSeleccionada.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void actualizarFechaEditText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        etFecha.setText(sdf.format(fechaHoraSeleccionada.getTime()));
    }

    private void actualizarHoraEditText() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        etHora.setText(sdf.format(fechaHoraSeleccionada.getTime()));
    }

    private void devolverFechaHora() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String fechaEmision = sdf.format(fechaHoraSeleccionada.getTime());

        Intent resultData = new Intent();
        resultData.putExtra("fecha_emision", fechaEmision);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }
}