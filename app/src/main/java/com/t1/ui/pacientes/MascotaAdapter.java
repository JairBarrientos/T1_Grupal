package com.t1.ui.pacientes;

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
import com.t1.model.Mascota;

import java.util.List;

public class MascotaAdapter extends ArrayAdapter<Mascota> {

    public interface OnMascotaActionListener {
        void onEditar(Mascota mascota);
        void onEliminar(Mascota mascota);
    }

    private final OnMascotaActionListener listener;

    public MascotaAdapter(Context context, List<Mascota> mascotas, OnMascotaActionListener listener) {
        super(context, 0, mascotas);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_paciente, parent, false);
        }

        Mascota mascota = getItem(position);

        TextView tvInfo = convertView.findViewById(R.id.tvInfoPaciente);
        Button btnEditar = convertView.findViewById(R.id.btnEditar);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

        String nombreDueño = buscarNombreCliente(mascota.getIdCliente());

        tvInfo.setText(mascota.getNombre() + " - " + mascota.getEspecie() + " (" + mascota.getRaza() + ")"
                + "\nEdad: " + mascota.getEdad() + " años | Peso: " + mascota.getPeso() + " kg"
                + "\nDueño: " + nombreDueño);

        btnEditar.setOnClickListener(v -> listener.onEditar(mascota));
        btnEliminar.setOnClickListener(v -> listener.onEliminar(mascota));

        return convertView;
    }

    private String buscarNombreCliente(int idCliente) {
        for (Cliente c : DataManager.getInstance().clientes) {
            if (c.getId() == idCliente) {
                return c.getNombre() + " " + c.getApellido();
            }
        }
        return "Desconocido";
    }
}