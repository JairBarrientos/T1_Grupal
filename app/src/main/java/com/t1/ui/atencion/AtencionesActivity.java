package com.t1.ui.atencion;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Atencion;
import com.t1.model.EstadoAtencion;

public class AtencionesActivity extends AppCompatActivity
        implements AtencionAdapter.OnAtencionActionListener {

    private AtencionAdapter adapter;
    public static final String EXTRA_ATENCION_ID = "extra_atencion_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atenciones);

        ListView lvAtenciones = findViewById(R.id.lvAtenciones);
        Button btnAgregar = findViewById(R.id.btnAgregarAtencion);

        adapter = new AtencionAdapter(this, DataManager.getInstance().atenciones, this);
        lvAtenciones.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> {
            if (DataManager.getInstance().mascotas.isEmpty()) {
                Toast.makeText(this, "Primero registra al menos un paciente", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!hayVeterinariosDisponibles()) {
                Toast.makeText(this, "Primero registra al menos un veterinario", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(AtencionesActivity.this, AtencionFormActivity.class));
        });
    }

    private boolean hayVeterinariosDisponibles() {
        for (com.t1.model.Empleado e : DataManager.getInstance().empleados) {
            if (e.getRol() == com.t1.model.Rol.VETERINARIO) return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditar(Atencion atencion) {
        Intent intent = new Intent(AtencionesActivity.this, AtencionFormActivity.class);
        intent.putExtra(EXTRA_ATENCION_ID, atencion.getId());
        startActivity(intent);
    }

    @Override
    public void onCambiarEstado(Atencion atencion) {
        EstadoAtencion estadoActual = atencion.getEstado();

        if (estadoActual == EstadoAtencion.ATENDIDA || estadoActual == EstadoAtencion.CANCELADA) {
            Toast.makeText(this, "Esta atención ya está finalizada", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] opciones;
        if (estadoActual == EstadoAtencion.REGISTRADA) {
            opciones = new String[]{"Confirmar", "Cancelar"};
        } else if (estadoActual == EstadoAtencion.CONFIRMADA) {
            opciones = new String[]{"Atender", "Postergar", "Cancelar"};
        } else {
            opciones = new String[]{"Confirmar", "Cancelar"};
        }

        new AlertDialog.Builder(this)
                .setTitle("Cambiar estado")
                .setItems(opciones, (dialog, which) -> {
                    String seleccion = opciones[which];
                    switch (seleccion) {
                        case "Confirmar":
                            atencion.setEstado(EstadoAtencion.CONFIRMADA);
                            break;
                        case "Atender":
                            atencion.setEstado(EstadoAtencion.ATENDIDA);
                            break;
                        case "Postergar":
                            atencion.setEstado(EstadoAtencion.POSTERGADA);
                            break;
                        case "Cancelar":
                            atencion.setEstado(EstadoAtencion.CANCELADA);
                            break;
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Estado actualizado a " + atencion.getEstado(), Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    @Override
    public void onEliminar(Atencion atencion) {
        DataManager.getInstance().atenciones.remove(atencion);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Atención eliminada", Toast.LENGTH_SHORT).show();
    }
}