package com.t1.ui.reportes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Atencion;
import com.t1.model.DetalleVenta;
import com.t1.model.EstadoAtencion;
import com.t1.model.Venta;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReportesActivity extends AppCompatActivity {

    private TextView tvFechaInicio, tvFechaFin, tvResultadoReporte;
    private Date fechaInicio, fechaFin;
    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        tvFechaInicio = findViewById(R.id.tvFechaInicio);
        tvFechaFin = findViewById(R.id.tvFechaFin);
        tvResultadoReporte = findViewById(R.id.tvResultadoReporte);
        Button btnFechaInicio = findViewById(R.id.btnFechaInicio);
        Button btnFechaFin = findViewById(R.id.btnFechaFin);
        Button btnGenerarReporte = findViewById(R.id.btnGenerarReporte);

        btnFechaInicio.setOnClickListener(v -> seleccionarFecha(true));
        btnFechaFin.setOnClickListener(v -> seleccionarFecha(false));
        btnGenerarReporte.setOnClickListener(v -> generarReporte());
    }

    private void seleccionarFecha(boolean esInicio) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar seleccion = Calendar.getInstance();
                    if (esInicio) {
                        seleccion.set(year, month, dayOfMonth, 0, 0, 0);
                        fechaInicio = seleccion.getTime();
                        tvFechaInicio.setText("Fecha inicio: " + formato.format(fechaInicio));
                    } else {
                        seleccion.set(year, month, dayOfMonth, 23, 59, 59);
                        fechaFin = seleccion.getTime();
                        tvFechaFin.setText("Fecha fin: " + formato.format(fechaFin));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void generarReporte() {
        if (fechaInicio == null || fechaFin == null) {
            Toast.makeText(this, "Selecciona fecha de inicio y fin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fechaInicio.after(fechaFin)) {
            Toast.makeText(this, "La fecha de inicio debe ser anterior a la de fin", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("PACIENTES ATENDIDOS\n\n");
        int totalAtendidos = 0;
        for (Atencion a : DataManager.getInstance().atenciones) {
            if (a.getEstado() == EstadoAtencion.ATENDIDA
                    && !a.getFecha().before(fechaInicio) && !a.getFecha().after(fechaFin)) {
                totalAtendidos++;
                sb.append("- ").append(formato.format(a.getFecha()))
                        .append(": ").append(a.getMotivo()).append("\n");
            }
        }
        sb.append("\nTotal atendidos: ").append(totalAtendidos).append("\n\n");

        sb.append("MONTOS RECAUDADOS\n\n");
        double totalRecaudado = 0;
        int totalVentas = 0;
        Map<String, Integer> productosVendidos = new HashMap<>();
        Map<String, Integer> serviciosVendidos = new HashMap<>();

        for (Venta v : DataManager.getInstance().ventas) {
            if (!v.getFecha().before(fechaInicio) && !v.getFecha().after(fechaFin)) {
                totalVentas++;
                totalRecaudado += v.getTotal();
                for (DetalleVenta d : v.getDetalles()) {
                    if (d.getTipo().equals("PRODUCTO")) {
                        productosVendidos.merge(d.getNombreItem(), d.getCantidad(), Integer::sum);
                    } else {
                        serviciosVendidos.merge(d.getNombreItem(), d.getCantidad(), Integer::sum);
                    }
                }
            }
        }

        sb.append("Total de ventas: ").append(totalVentas).append("\n");
        sb.append("Monto total recaudado: S/ ").append(String.format(Locale.getDefault(), "%.2f", totalRecaudado)).append("\n\n");

        sb.append("PRODUCTOS VENDIDOS\n\n");
        if (productosVendidos.isEmpty()) {
            sb.append("No se vendieron productos en este periodo\n");
        } else {
            for (Map.Entry<String, Integer> entry : productosVendidos.entrySet()) {
                sb.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" unidades\n");
            }
        }

        sb.append("\nSERVICIOS PRESTADOS\n\n");
        if (serviciosVendidos.isEmpty()) {
            sb.append("No se prestaron servicios en este periodo\n");
        } else {
            for (Map.Entry<String, Integer> entry : serviciosVendidos.entrySet()) {
                sb.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" veces\n");
            }
        }

        tvResultadoReporte.setText(sb.toString());
    }
}