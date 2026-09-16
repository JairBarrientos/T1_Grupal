package com.t1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.data.DataManager;
import com.t1.model.Empleado;
import com.t1.ui.atencion.AtencionesActivity;
import com.t1.ui.clientes.ClientesActivity;
import com.t1.ui.pacientes.PacientesActivity;
import com.t1.ui.personal.PersonalActivity;
import com.t1.ui.productos.ProductosActivity;
import com.t1.ui.reportes.ReportesActivity;
import com.t1.ui.servicios.ServiciosActivity;
import com.t1.ui.ventas.VentasActivity;
import com.t1.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvBienvenida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Empleado usuario = DataManager.getInstance().getUsuarioActual();

        if (usuario == null) {
            irALogin();
            return;
        }

        tvBienvenida = findViewById(R.id.tvBienvenida);
        tvBienvenida.setText("Bienvenido/a, " + usuario.getNombre() + " (" + usuario.getRol() + ")");

        Button btnClientes = findViewById(R.id.btnClientes);
        btnClientes.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ClientesActivity.class)));

        Button btnPacientes = findViewById(R.id.btnPacientes);
        btnPacientes.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PacientesActivity.class)));

        Button btnPersonal = findViewById(R.id.btnPersonal);
        btnPersonal.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PersonalActivity.class)));

        Button btnProductos = findViewById(R.id.btnProductos);
        btnProductos.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProductosActivity.class)));

        Button btnServicios = findViewById(R.id.btnServicios);
        btnServicios.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ServiciosActivity.class)));

        Button btnAtenciones = findViewById(R.id.btnAtenciones);
        btnAtenciones.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AtencionesActivity.class)));

        Button btnVentas = findViewById(R.id.btnVentas);
        btnVentas.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, VentasActivity.class)));

        Button btnReportes = findViewById(R.id.btnReportes);
        btnReportes.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ReportesActivity.class)));

        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DataManager.getInstance().getUsuarioActual() == null) {
            irALogin();
        }
    }

    private void cerrarSesion() {
        DataManager.getInstance().setUsuarioActual(null);
        irALogin();
    }

    private void irALogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}