package com.t1.ui.productos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.t1.R;
import com.t1.model.Producto;

import java.util.List;

public class ProductoAdapter extends ArrayAdapter<Producto> {

    public interface OnProductoActionListener {
        void onEditar(Producto producto);
        void onEliminar(Producto producto);
    }

    private final OnProductoActionListener listener;

    public ProductoAdapter(Context context, List<Producto> productos, OnProductoActionListener listener) {
        super(context, 0, productos);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_producto, parent, false);
        }

        Producto producto = getItem(position);

        TextView tvInfo = convertView.findViewById(R.id.tvInfoProducto);
        Button btnEditar = convertView.findViewById(R.id.btnEditar);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

        tvInfo.setText(producto.getNombre() + "\nS/ " + producto.getPrecio() + " | Stock: " + producto.getStock());

        btnEditar.setOnClickListener(v -> listener.onEditar(producto));
        btnEliminar.setOnClickListener(v -> listener.onEliminar(producto));

        return convertView;
    }
}