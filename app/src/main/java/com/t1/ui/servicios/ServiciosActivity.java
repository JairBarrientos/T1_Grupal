package com.t1.ui.servicios;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Servicio;

public class ServiciosActivity extends AppCompatActivity
        implements ServicioAdapter.OnServicioActionListener {

    private ServicioAdapter adapter;
    public static final String EXTRA_SERVICIO_ID = "extra_servicio_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

        ListView lvServicios = findViewById(R.id.lvServicios);
        Button btnAgregar = findViewById(R.id.btnAgregarServicio);

        adapter = new ServicioAdapter(this, DataManager.getInstance().servicios, this);
        lvServicios.setAdapter(adapter);

        btnAgregar.setOnClickListener(v ->
                startActivity(new Intent(ServiciosActivity.this, ServicioFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditar(Servicio servicio) {
        Intent intent = new Intent(ServiciosActivity.this, ServicioFormActivity.class);
        intent.putExtra(EXTRA_SERVICIO_ID, servicio.getId());
        startActivity(intent);
    }

    @Override
    public void onEliminar(Servicio servicio) {
        DataManager.getInstance().servicios.remove(servicio);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Servicio eliminado", Toast.LENGTH_SHORT).show();
    }
}