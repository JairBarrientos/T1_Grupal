package com.t1.ui.clientes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Cliente;

public class ClienteFormActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etDni, etTelefono, etDireccion, etEmail;
    private TextView tvTitulo;
    private Cliente clienteEditando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_form);

        tvTitulo = findViewById(R.id.tvTituloForm);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etDni = findViewById(R.id.etDni);
        etTelefono = findViewById(R.id.etTelefono);
        etDireccion = findViewById(R.id.etDireccion);
        etEmail = findViewById(R.id.etEmail);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        int clienteId = getIntent().getIntExtra(ClientesActivity.EXTRA_CLIENTE_ID, -1);
        if (clienteId != -1) {
            cargarClienteParaEditar(clienteId);
        }

        btnGuardar.setOnClickListener(v -> guardarCliente());
    }

    private void cargarClienteParaEditar(int id) {
        for (Cliente c : DataManager.getInstance().clientes) {
            if (c.getId() == id) {
                clienteEditando = c;
                break;
            }
        }

        if (clienteEditando != null) {
            tvTitulo.setText("Editar Cliente");
            etNombre.setText(clienteEditando.getNombre());
            etApellido.setText(clienteEditando.getApellido());
            etDni.setText(clienteEditando.getDni());
            etTelefono.setText(clienteEditando.getTelefono());
            etDireccion.setText(clienteEditando.getDireccion());
            etEmail.setText(clienteEditando.getEmail());
        }
    }

    private void guardarCliente() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String dni = etDni.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this, "Nombre, apellido y DNI son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (clienteEditando != null) {
            clienteEditando.setNombre(nombre);
            clienteEditando.setApellido(apellido);
            clienteEditando.setDni(dni);
            clienteEditando.setTelefono(telefono);
            clienteEditando.setDireccion(direccion);
            clienteEditando.setEmail(email);
            Toast.makeText(this, "Cliente actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Cliente nuevo = new Cliente(
                    DataManager.getInstance().generarIdCliente(),
                    nombre, apellido, dni, telefono, direccion, email
            );
            DataManager.getInstance().clientes.add(nuevo);
            Toast.makeText(this, "Cliente agregado", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}