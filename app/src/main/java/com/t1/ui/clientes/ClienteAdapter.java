package com.t1.ui.clientes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.t1.R;
import com.t1.model.Cliente;

import java.util.List;

public class ClienteAdapter extends ArrayAdapter<Cliente> {

    public interface OnClienteActionListener {
        void onEditar(Cliente cliente);
        void onEliminar(Cliente cliente);
    }

    private final OnClienteActionListener listener;

    public ClienteAdapter(Context context, List<Cliente> clientes, OnClienteActionListener listener) {
        super(context, 0, clientes);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cliente, parent, false);
        }

        Cliente cliente = getItem(position);

        TextView tvInfo = convertView.findViewById(R.id.tvInfoCliente);
        Button btnEditar = convertView.findViewById(R.id.btnEditar);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

        tvInfo.setText(cliente.getNombre() + " " + cliente.getApellido()
                + "\nDNI: " + cliente.getDni() + " | Tel: " + cliente.getTelefono());

        btnEditar.setOnClickListener(v -> listener.onEditar(cliente));
        btnEliminar.setOnClickListener(v -> listener.onEliminar(cliente));

        return convertView;
    }
}