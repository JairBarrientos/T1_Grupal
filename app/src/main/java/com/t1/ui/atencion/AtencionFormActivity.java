package com.t1.ui.atencion;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Atencion;
import com.t1.model.Empleado;
import com.t1.model.EstadoAtencion;
import com.t1.model.Mascota;
import com.t1.model.Rol;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AtencionFormActivity extends AppCompatActivity {

    private Spinner spMascota, spVeterinario;
    private EditText etMotivo, etObservaciones;
    private TextView tvTitulo, tvFechaSeleccionada;
    private Atencion atencionEditando;

    private List<Mascota> listaMascotas;
    private List<Empleado> listaVeterinarios;
    private Date fechaSeleccionada;

    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atencion_form);

        tvTitulo = findViewById(R.id.tvTituloForm);
        spMascota = findViewById(R.id.spMascota);
        spVeterinario = findViewById(R.id.spVeterinario);
        etMotivo = findViewById(R.id.etMotivo);
        etObservaciones = findViewById(R.id.etObservaciones);
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
        Button btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        configurarSpinners();

        btnSeleccionarFecha.setOnClickListener(v -> abrirDatePicker());

        int atencionId = getIntent().getIntExtra(AtencionesActivity.EXTRA_ATENCION_ID, -1);
        if (atencionId != -1) {
            cargarAtencionParaEditar(atencionId);
        }

        btnGuardar.setOnClickListener(v -> guardarAtencion());
    }

    private void configurarSpinners() {
        listaMascotas = new ArrayList<>(DataManager.getInstance().mascotas);
        List<String> nombresMascotas = new ArrayList<>();
        for (Mascota m : listaMascotas) nombresMascotas.add(m.getNombre() + " (" + m.getEspecie() + ")");
        ArrayAdapter<String> adapterMascota = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresMascotas);
        adapterMascota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMascota.setAdapter(adapterMascota);

        listaVeterinarios = new ArrayList<>();
        for (Empleado e : DataManager.getInstance().empleados) {
            if (e.getRol() == Rol.VETERINARIO) listaVeterinarios.add(e);
        }
        List<String> nombresVeterinarios = new ArrayList<>();
        for (Empleado e : listaVeterinarios) nombresVeterinarios.add(e.getNombre() + " " + e.getApellido());
        ArrayAdapter<String> adapterVet = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresVeterinarios);
        adapterVet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVeterinario.setAdapter(adapterVet);
    }

    private void abrirDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (fechaSeleccionada != null) calendar.setTime(fechaSeleccionada);

        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar seleccion = Calendar.getInstance();
                    seleccion.set(year, month, dayOfMonth, 0, 0, 0);
                    fechaSeleccionada = seleccion.getTime();
                    tvFechaSeleccionada.setText("Fecha: " + formato.format(fechaSeleccionada));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void cargarAtencionParaEditar(int id) {
        for (Atencion a : DataManager.getInstance().atenciones) {
            if (a.getId() == id) { atencionEditando = a; break; }
        }

        if (atencionEditando != null) {
            tvTitulo.setText("Editar Atención");
            etMotivo.setText(atencionEditando.getMotivo());
            etObservaciones.setText(atencionEditando.getObservaciones());
            fechaSeleccionada = atencionEditando.getFecha();
            tvFechaSeleccionada.setText("Fecha: " + formato.format(fechaSeleccionada));

            for (int i = 0; i < listaMascotas.size(); i++) {
                if (listaMascotas.get(i).getId() == atencionEditando.getIdMascota()) {
                    spMascota.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < listaVeterinarios.size(); i++) {
                if (listaVeterinarios.get(i).getId() == atencionEditando.getIdVeterinario()) {
                    spVeterinario.setSelection(i);
                    break;
                }
            }
        }
    }

    private void guardarAtencion() {
        String motivo = etMotivo.getText().toString().trim();
        String observaciones = etObservaciones.getText().toString().trim();

        if (motivo.isEmpty()) {
            Toast.makeText(this, "Ingresa el motivo de la atención", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fechaSeleccionada == null) {
            Toast.makeText(this, "Selecciona una fecha", Toast.LENGTH_SHORT).show();
            return;
        }
        if (listaMascotas.isEmpty() || listaVeterinarios.isEmpty()) {
            Toast.makeText(this, "Faltan pacientes o veterinarios registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        int idMascota = listaMascotas.get(spMascota.getSelectedItemPosition()).getId();
        int idVeterinario = listaVeterinarios.get(spVeterinario.getSelectedItemPosition()).getId();

        if (atencionEditando != null) {
            atencionEditando.setIdMascota(idMascota);
            atencionEditando.setIdVeterinario(idVeterinario);
            atencionEditando.setFecha(fechaSeleccionada);
            atencionEditando.setMotivo(motivo);
            atencionEditando.setObservaciones(observaciones);
            Toast.makeText(this, "Atención actualizada", Toast.LENGTH_SHORT).show();
        } else {
            Atencion nueva = new Atencion(
                    DataManager.getInstance().generarIdAtencion(),
                    idMascota, idVeterinario, fechaSeleccionada, motivo,
                    EstadoAtencion.REGISTRADA, observaciones
            );
            DataManager.getInstance().atenciones.add(nueva);
            Toast.makeText(this, "Atención registrada", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}