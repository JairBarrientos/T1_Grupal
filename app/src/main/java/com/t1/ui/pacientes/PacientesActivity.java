package com.t1.ui.pacientes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Mascota;

public class PacientesActivity extends AppCompatActivity
        implements MascotaAdapter.OnMascotaActionListener {

    private ListView lvPacientes;
    private MascotaAdapter adapter;

    public static final String EXTRA_MASCOTA_ID = "extra_mascota_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);

        lvPacientes = findViewById(R.id.lvPacientes);
        Button btnAgregar = findViewById(R.id.btnAgregarPaciente);

        adapter = new MascotaAdapter(this, DataManager.getInstance().mascotas, this);
        lvPacientes.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> {
            if (DataManager.getInstance().clientes.isEmpty()) {
                Toast.makeText(this, "Primero registra al menos un cliente", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(PacientesActivity.this, PacienteFormActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditar(Mascota mascota) {
        Intent intent = new Intent(PacientesActivity.this, PacienteFormActivity.class);
        intent.putExtra(EXTRA_MASCOTA_ID, mascota.getId());
        startActivity(intent);
    }

    @Override
    public void onEliminar(Mascota mascota) {
        DataManager.getInstance().mascotas.remove(mascota);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Paciente eliminado", Toast.LENGTH_SHORT).show();
    }
}