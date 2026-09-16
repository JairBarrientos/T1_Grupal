package com.t1.ui.ventas;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.DetalleVenta;
import com.t1.model.Venta;

import java.util.Locale;

public class VentasActivity extends AppCompatActivity
        implements VentaAdapter.OnVentaActionListener {

    private VentaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        ListView lvVentas = findViewById(R.id.lvVentas);
        Button btnAgregar = findViewById(R.id.btnAgregarVenta);

        adapter = new VentaAdapter(this, DataManager.getInstance().ventas, this);
        lvVentas.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> {
            if (DataManager.getInstance().clientes.isEmpty()) {
                Toast.makeText(this, "Primero registra al menos un cliente", Toast.LENGTH_SHORT).show();
                return;
            }
            if (DataManager.getInstance().productos.isEmpty() && DataManager.getInstance().servicios.isEmpty()) {
                Toast.makeText(this, "Registra al menos un producto o servicio", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(VentasActivity.this, VentaFormActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onVerDetalle(Venta venta) {
        StringBuilder sb = new StringBuilder();
        for (DetalleVenta d : venta.getDetalles()) {
            sb.append(d.getNombreItem())
                    .append(" x").append(d.getCantidad())
                    .append(" = S/ ").append(String.format(Locale.getDefault(), "%.2f", d.getSubtotal()))
                    .append("\n");
        }
        sb.append("\nTotal: S/ ").append(String.format(Locale.getDefault(), "%.2f", venta.getTotal()));

        new AlertDialog.Builder(this)
                .setTitle("Detalle de Venta")
                .setMessage(sb.toString())
                .setPositiveButton("Cerrar", null)
                .show();
    }

    @Override
    public void onEliminar(Venta venta) {
        DataManager.getInstance().ventas.remove(venta);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Venta eliminada", Toast.LENGTH_SHORT).show();
    }
}