package com.t1.ui.servicios;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Servicio;

public class ServicioFormActivity extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etPrecio;
    private TextView tvTitulo;
    private Servicio servicioEditando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_form);

        tvTitulo = findViewById(R.id.tvTituloForm);
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        int id = getIntent().getIntExtra(ServiciosActivity.EXTRA_SERVICIO_ID, -1);
        if (id != -1) cargarParaEditar(id);

        btnGuardar.setOnClickListener(v -> guardar());
    }

    private void cargarParaEditar(int id) {
        for (Servicio s : DataManager.getInstance().servicios) {
            if (s.getId() == id) { servicioEditando = s; break; }
        }
        if (servicioEditando != null) {
            tvTitulo.setText("Editar Servicio");
            etNombre.setText(servicioEditando.getNombre());
            etDescripcion.setText(servicioEditando.getDescripcion());
            etPrecio.setText(String.valueOf(servicioEditando.getPrecio()));
        }
    }

    private void guardar() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();

        if (nombre.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Completa nombre y precio", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El precio debe ser numérico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (servicioEditando != null) {
            servicioEditando.setNombre(nombre);
            servicioEditando.setDescripcion(descripcion);
            servicioEditando.setPrecio(precio);
            Toast.makeText(this, "Servicio actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Servicio nuevo = new Servicio(DataManager.getInstance().generarIdServicio(),
                    nombre, descripcion, precio);
            DataManager.getInstance().servicios.add(nuevo);
            Toast.makeText(this, "Servicio agregado", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}