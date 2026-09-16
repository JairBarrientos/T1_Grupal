package com.t1.ui.personal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.t1.R;
import com.t1.model.Empleado;

import java.util.List;

public class EmpleadoAdapter extends ArrayAdapter<Empleado> {

    public interface OnEmpleadoActionListener {
        void onEditar(Empleado empleado);
        void onEliminar(Empleado empleado);
    }

    private final OnEmpleadoActionListener listener;

    public EmpleadoAdapter(Context context, List<Empleado> empleados, OnEmpleadoActionListener listener) {
        super(context, 0, empleados);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_personal, parent, false);
        }

        Empleado empleado = getItem(position);

        TextView tvInfo = convertView.findViewById(R.id.tvInfoPersonal);
        Button btnEditar = convertView.findViewById(R.id.btnEditar);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

        String detalle = empleado.getNombre() + " " + empleado.getApellido()
                + "\nUsuario: " + empleado.getUsuario() + " | Rol: " + empleado.getRol();
        if (empleado.getRol() == com.t1.model.Rol.VETERINARIO && !empleado.getEspecialidad().isEmpty()) {
            detalle += " (" + empleado.getEspecialidad() + ")";
        }

        tvInfo.setText(detalle);

        btnEditar.setOnClickListener(v -> listener.onEditar(empleado));
        btnEliminar.setOnClickListener(v -> listener.onEliminar(empleado));

        return convertView;
    }
}