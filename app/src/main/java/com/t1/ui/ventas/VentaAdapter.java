package com.t1.ui.ventas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Cliente;
import com.t1.model.Venta;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VentaAdapter extends ArrayAdapter<Venta> {

    public interface OnVentaActionListener {
        void onVerDetalle(Venta venta);
        void onEliminar(Venta venta);
    }

    private final OnVentaActionListener listener;
    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public VentaAdapter(Context context, List<Venta> ventas, OnVentaActionListener listener) {
        super(context, 0, ventas);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_venta, parent, false);
        }

        Venta venta = getItem(position);

        TextView tvInfo = convertView.findViewById(R.id.tvInfoVenta);
        Button btnDetalle = convertView.findViewById(R.id.btnVerDetalle);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

        String nombreCliente = buscarNombreCliente(venta.getIdCliente());

        tvInfo.setText("Cliente: " + nombreCliente
                + "\nFecha: " + formato.format(venta.getFecha())
                + "\nTotal: S/ " + String.format(Locale.getDefault(), "%.2f", venta.getTotal()));

        btnDetalle.setOnClickListener(v -> listener.onVerDetalle(venta));
        btnEliminar.setOnClickListener(v -> listener.onEliminar(venta));

        return convertView;
    }

    private String buscarNombreCliente(int idCliente) {
        for (Cliente c : DataManager.getInstance().clientes) {
            if (c.getId() == idCliente) return c.getNombre() + " " + c.getApellido();
        }
        return "Desconocido";
    }
}