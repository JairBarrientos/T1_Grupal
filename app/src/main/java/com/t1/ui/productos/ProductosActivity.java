package com.t1.ui.productos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Producto;

public class ProductosActivity extends AppCompatActivity
        implements ProductoAdapter.OnProductoActionListener {

    private ProductoAdapter adapter;
    public static final String EXTRA_PRODUCTO_ID = "extra_producto_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        ListView lvProductos = findViewById(R.id.lvProductos);
        Button btnAgregar = findViewById(R.id.btnAgregarProducto);

        adapter = new ProductoAdapter(this, DataManager.getInstance().productos, this);
        lvProductos.setAdapter(adapter);

        btnAgregar.setOnClickListener(v ->
                startActivity(new Intent(ProductosActivity.this, ProductoFormActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditar(Producto producto) {
        Intent intent = new Intent(ProductosActivity.this, ProductoFormActivity.class);
        intent.putExtra(EXTRA_PRODUCTO_ID, producto.getId());
        startActivity(intent);
    }

    @Override
    public void onEliminar(Producto producto) {
        DataManager.getInstance().productos.remove(producto);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
    }
}