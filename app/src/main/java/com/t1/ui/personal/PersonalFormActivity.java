package com.t1.ui.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Empleado;
import com.t1.model.Rol;

import java.util.ArrayList;
import java.util.List;

public class PersonalFormActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etUsuario, etPassword, etEspecialidad;
    private Spinner spRol;
    private TextView tvTitulo;
    private Empleado empleadoEditando;

    private final List<Rol> roles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_form);

        tvTitulo = findViewById(R.id.tvTituloForm);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        etEspecialidad = findViewById(R.id.etEspecialidad);
        spRol = findViewById(R.id.spRol);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        configurarSpinnerRol();

        int empleadoId = getIntent().getIntExtra(PersonalActivity.EXTRA_EMPLEADO_ID, -1);
        if (empleadoId != -1) {
            cargarEmpleadoParaEditar(empleadoId);
        }

        btnGuardar.setOnClickListener(v -> guardarEmpleado());
    }

    private void configurarSpinnerRol() {
        roles.add(Rol.VETERINARIO);
        roles.add(Rol.ASISTENTE);

        List<String> nombresRoles = new ArrayList<>();
        for (Rol r : roles) nombresRoles.add(r.name());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, nombresRoles);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRol.setAdapter(spinnerAdapter);

        spRol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean esVeterinario = roles.get(position) == Rol.VETERINARIO;
                etEspecialidad.setVisibility(esVeterinario ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void cargarEmpleadoParaEditar(int id) {
        for (Empleado e : DataManager.getInstance().empleados) {
            if (e.getId() == id) {
                empleadoEditando = e;
                break;
            }
        }

        if (empleadoEditando != null) {
            tvTitulo.setText("Editar Personal");
            etNombre.setText(empleadoEditando.getNombre());
            etApellido.setText(empleadoEditando.getApellido());
            etUsuario.setText(empleadoEditando.getUsuario());
            etPassword.setText(empleadoEditando.getPassword());
            etEspecialidad.setText(empleadoEditando.getEspecialidad());

            int posicion = roles.indexOf(empleadoEditando.getRol());
            if (posicion >= 0) spRol.setSelection(posicion);
        }
    }

    private void guardarEmpleado() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String usuario = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String especialidad = etEspecialidad.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa nombre, apellido, usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Empleado e : DataManager.getInstance().empleados) {
            boolean esOtroEmpleado = (empleadoEditando == null || e.getId() != empleadoEditando.getId());
            if (esOtroEmpleado && e.getUsuario().equalsIgnoreCase(usuario)) {
                Toast.makeText(this, "Ese nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Rol rolSeleccionado = roles.get(spRol.getSelectedItemPosition());
        if (rolSeleccionado != Rol.VETERINARIO) especialidad = "";

        if (empleadoEditando != null) {
            empleadoEditando.setNombre(nombre);
            empleadoEditando.setApellido(apellido);
            empleadoEditando.setUsuario(usuario);
            empleadoEditando.setPassword(password);
            empleadoEditando.setRol(rolSeleccionado);
            empleadoEditando.setEspecialidad(especialidad);
            Toast.makeText(this, "Empleado actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Empleado nuevo = new Empleado(
                    DataManager.getInstance().generarIdEmpleado(),
                    nombre, apellido, usuario, password, rolSeleccionado, especialidad
            );
            DataManager.getInstance().empleados.add(nuevo);
            Toast.makeText(this, "Empleado agregado", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}