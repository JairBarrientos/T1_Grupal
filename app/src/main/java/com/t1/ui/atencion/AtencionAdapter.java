package com.t1.ui.atencion;

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
import com.t1.model.Atencion;
import com.t1.model.Empleado;
import com.t1.model.Mascota;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AtencionAdapter extends ArrayAdapter<Atencion> {

    public interface OnAtencionActionListener {
        void onEditar(Atencion atencion);
        void onCambiarEstado(Atencion atencion);
        void onEliminar(Atencion atencion);
    }

    private final OnAtencionActionListener listener;
    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public AtencionAdapter(Context context, List<Atencion> atenciones, OnAtencionActionListener listener) {
        super(context, 0, atenciones);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_atencion, parent, false);
        }

        Atencion atencion = getItem(position);

        TextView tvInfo = convertView.findViewById(R.id.tvInfoAtencion);
        Button btnEditar = convertView.findViewById(R.id.btnEditar);
        Button btnEstado = convertView.findViewById(R.id.btnEstado);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

        String nombreMascota = buscarNombreMascota(atencion.getIdMascota());
        String nombreVeterinario = buscarNombreVeterinario(atencion.getIdVeterinario());

        tvInfo.setText("Paciente: " + nombreMascota
                + "\nVeterinario: " + nombreVeterinario
                + "\nFecha: " + formato.format(atencion.getFecha())
                + "\nMotivo: " + atencion.getMotivo()
                + "\nEstado: " + atencion.getEstado());

        btnEditar.setOnClickListener(v -> listener.onEditar(atencion));
        btnEstado.setOnClickListener(v -> listener.onCambiarEstado(atencion));
        btnEliminar.setOnClickListener(v -> listener.onEliminar(atencion));

        return convertView;
    }

    private String buscarNombreMascota(int idMascota) {
        for (Mascota m : DataManager.getInstance().mascotas) {
            if (m.getId() == idMascota) return m.getNombre();
        }
        return "Desconocido";
    }

    private String buscarNombreVeterinario(int idVeterinario) {
        for (Empleado e : DataManager.getInstance().empleados) {
            if (e.getId() == idVeterinario) return e.getNombre() + " " + e.getApellido();
        }
        return "Desconocido";
    }
}