package com.t1.ui.pacientes;

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
import com.t1.model.Cliente;
import com.t1.model.Mascota;

import java.util.ArrayList;
import java.util.List;

public class PacienteFormActivity extends AppCompatActivity {

    private EditText etNombre, etEspecie, etRaza, etEdad, etPeso;
    private Spinner spCliente;
    private TextView tvTitulo;
    private Mascota mascotaEditando;

    private List<Cliente> listaClientesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_form);

        tvTitulo = findViewById(R.id.tvTituloForm);
        etNombre = findViewById(R.id.etNombre);
        etEspecie = findViewById(R.id.etEspecie);
        etRaza = findViewById(R.id.etRaza);
        etEdad = findViewById(R.id.etEdad);
        etPeso = findViewById(R.id.etPeso);
        spCliente = findViewById(R.id.spCliente);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        configurarSpinnerClientes();

        int mascotaId = getIntent().getIntExtra(PacientesActivity.EXTRA_MASCOTA_ID, -1);
        if (mascotaId != -1) {
            cargarMascotaParaEditar(mascotaId);
        }

        btnGuardar.setOnClickListener(v -> guardarMascota());
    }

    private void configurarSpinnerClientes() {
        listaClientesSpinner = new ArrayList<>(DataManager.getInstance().clientes);

        List<String> nombresClientes = new ArrayList<>();
        for (Cliente c : listaClientesSpinner) {
            nombresClientes.add(c.getNombre() + " " + c.getApellido() + " (DNI: " + c.getDni() + ")");
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, nombresClientes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCliente.setAdapter(spinnerAdapter);
    }

    private void cargarMascotaParaEditar(int id) {
        for (Mascota m : DataManager.getInstance().mascotas) {
            if (m.getId() == id) {
                mascotaEditando = m;
                break;
            }
        }

        if (mascotaEditando != null) {
            tvTitulo.setText("Editar Paciente");
            etNombre.setText(mascotaEditando.getNombre());
            etEspecie.setText(mascotaEditando.getEspecie());
            etRaza.setText(mascotaEditando.getRaza());
            etEdad.setText(String.valueOf(mascotaEditando.getEdad()));
            etPeso.setText(String.valueOf(mascotaEditando.getPeso()));

            for (int i = 0; i < listaClientesSpinner.size(); i++) {
                if (listaClientesSpinner.get(i).getId() == mascotaEditando.getIdCliente()) {
                    spCliente.setSelection(i);
                    break;
                }
            }
        }
    }

    private void guardarMascota() {
        String nombre = etNombre.getText().toString().trim();
        String especie = etEspecie.getText().toString().trim();
        String raza = etRaza.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();
        String pesoStr = etPeso.getText().toString().trim();

        if (nombre.isEmpty() || especie.isEmpty() || edadStr.isEmpty() || pesoStr.isEmpty()) {
            Toast.makeText(this, "Completa nombre, especie, edad y peso", Toast.LENGTH_SHORT).show();
            return;
        }

        int edad;
        double peso;
        try {
            edad = Integer.parseInt(edadStr);
            peso = Double.parseDouble(pesoStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Edad y peso deben ser numéricos", Toast.LENGTH_SHORT).show();
            return;
        }

        int posicionSeleccionada = spCliente.getSelectedItemPosition();
        if (posicionSeleccionada < 0 || listaClientesSpinner.isEmpty()) {
            Toast.makeText(this, "Selecciona un dueño", Toast.LENGTH_SHORT).show();
            return;
        }
        int idClienteSeleccionado = listaClientesSpinner.get(posicionSeleccionada).getId();

        if (mascotaEditando != null) {
            mascotaEditando.setNombre(nombre);
            mascotaEditando.setEspecie(especie);
            mascotaEditando.setRaza(raza);
            mascotaEditando.setEdad(edad);
            mascotaEditando.setPeso(peso);
            mascotaEditando.setIdCliente(idClienteSeleccionado);
            Toast.makeText(this, "Paciente actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Mascota nueva = new Mascota(
                    DataManager.getInstance().generarIdMascota(),
                    nombre, especie, raza, edad, peso, idClienteSeleccionado
            );
            DataManager.getInstance().mascotas.add(nueva);
            Toast.makeText(this, "Paciente agregado", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}