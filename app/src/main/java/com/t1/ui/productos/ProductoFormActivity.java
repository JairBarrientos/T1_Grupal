package com.t1.ui.productos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Producto;

public class ProductoFormActivity extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etPrecio, etStock;
    private TextView tvTitulo;
    private Producto productoEditando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_form);

        tvTitulo = findViewById(R.id.tvTituloForm);
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        int id = getIntent().getIntExtra(ProductosActivity.EXTRA_PRODUCTO_ID, -1);
        if (id != -1) cargarParaEditar(id);

        btnGuardar.setOnClickListener(v -> guardar());
    }

    private void cargarParaEditar(int id) {
        for (Producto p : DataManager.getInstance().productos) {
            if (p.getId() == id) { productoEditando = p; break; }
        }
        if (productoEditando != null) {
            tvTitulo.setText("Editar Producto");
            etNombre.setText(productoEditando.getNombre());
            etDescripcion.setText(productoEditando.getDescripcion());
            etPrecio.setText(String.valueOf(productoEditando.getPrecio()));
            etStock.setText(String.valueOf(productoEditando.getStock()));
        }
    }

    private void guardar() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();

        if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Completa nombre, precio y stock", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        int stock;
        try {
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio y stock deben ser numéricos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (productoEditando != null) {
            productoEditando.setNombre(nombre);
            productoEditando.setDescripcion(descripcion);
            productoEditando.setPrecio(precio);
            productoEditando.setStock(stock);
            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Producto nuevo = new Producto(DataManager.getInstance().generarIdProducto(),
                    nombre, descripcion, precio, stock);
            DataManager.getInstance().productos.add(nuevo);
            Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}