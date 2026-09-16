package com.t1.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.MainActivity;
import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Empleado;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private TextView tvError;
    private Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(v -> intentarLogin());
    }

    private void intentarLogin() {
        String usuario = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarError("Completa usuario y contraseña");
            return;
        }

        Empleado empleadoEncontrado = null;
        for (Empleado e : DataManager.getInstance().empleados) {
            if (e.getUsuario().equalsIgnoreCase(usuario) && e.getPassword().equals(password)) {
                empleadoEncontrado = e;
                break;
            }
        }

        if (empleadoEncontrado != null) {
            DataManager.getInstance().setUsuarioActual(empleadoEncontrado);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            mostrarError("Usuario o contraseña incorrectos");
        }
    }

    private void mostrarError(String mensaje) {
        tvError.setText(mensaje);
        tvError.setVisibility(TextView.VISIBLE);
    }
}