package com.t1.ui.personal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Empleado;

public class PersonalActivity extends AppCompatActivity
        implements EmpleadoAdapter.OnEmpleadoActionListener {

    private ListView lvPersonal;
    private EmpleadoAdapter adapter;

    public static final String EXTRA_EMPLEADO_ID = "extra_empleado_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        lvPersonal = findViewById(R.id.lvPersonal);
        Button btnAgregar = findViewById(R.id.btnAgregarPersonal);

        adapter = new EmpleadoAdapter(this, DataManager.getInstance().empleados, this);
        lvPersonal.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalActivity.this, PersonalFormActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditar(Empleado empleado) {
        Intent intent = new Intent(PersonalActivity.this, PersonalFormActivity.class);
        intent.putExtra(EXTRA_EMPLEADO_ID, empleado.getId());
        startActivity(intent);
    }

    @Override
    public void onEliminar(Empleado empleado) {
        Empleado usuarioActual = DataManager.getInstance().getUsuarioActual();
        if (usuarioActual != null && usuarioActual.getId() == empleado.getId()) {
            Toast.makeText(this, "No puedes eliminar tu propio usuario mientras estás logueado", Toast.LENGTH_LONG).show();
            return;
        }

        if (DataManager.getInstance().empleados.size() <= 1) {
            Toast.makeText(this, "Debe existir al menos un empleado en el sistema", Toast.LENGTH_LONG).show();
            return;
        }

        DataManager.getInstance().empleados.remove(empleado);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Empleado eliminado", Toast.LENGTH_SHORT).show();
    }
}