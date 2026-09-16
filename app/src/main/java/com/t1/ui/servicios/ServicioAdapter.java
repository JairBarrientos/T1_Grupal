package com.t1.ui.servicios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.t1.R;
import com.t1.model.Servicio;

import java.util.List;

public class ServicioAdapter extends ArrayAdapter<Servicio> {

    public interface OnServicioActionListener {
        void onEditar(Servicio servicio);
        void onEliminar(Servicio servicio);
    }

    private final OnServicioActionListener listener;

    public ServicioAdapter(Context context, List<Servicio> servicios, OnServicioActionListener listener) {
        super(context, 0, servicios);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_servicio, parent, false);
        }

        Servicio servicio = getItem(position);

        TextView tvInfo = convertView.findViewById(R.id.tvInfoServicio);
        Button btnEditar = convertView.findViewById(R.id.btnEditar);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

        tvInfo.setText(servicio.getNombre() + "\nS/ " + servicio.getPrecio());

        btnEditar.setOnClickListener(v -> listener.onEditar(servicio));
        btnEliminar.setOnClickListener(v -> listener.onEliminar(servicio));

        return convertView;
    }
}