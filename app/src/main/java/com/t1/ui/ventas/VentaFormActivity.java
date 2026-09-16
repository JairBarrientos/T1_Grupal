package com.t1.ui.ventas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Cliente;
import com.t1.model.DetalleVenta;
import com.t1.model.Empleado;
import com.t1.model.Producto;
import com.t1.model.Servicio;
import com.t1.model.Venta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentaFormActivity extends AppCompatActivity {

    private Spinner spCliente, spTipoItem, spItem;
    private EditText etCantidad;
    private ListView lvDetalles;
    private TextView tvTotal;

    private List<Cliente> listaClientes;
    private final List<String> tipos = new ArrayList<>();
    private List<Producto> listaProductos;
    private List<Servicio> listaServicios;

    private final List<DetalleVenta> carrito = new ArrayList<>();
    private ArrayAdapter<String> adapterCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_form);

        spCliente = findViewById(R.id.spCliente);
        spTipoItem = findViewById(R.id.spTipoItem);
        spItem = findViewById(R.id.spItem);
        etCantidad = findViewById(R.id.etCantidad);
        lvDetalles = findViewById(R.id.lvDetalles);
        tvTotal = findViewById(R.id.tvTotal);
        Button btnAgregarItem = findViewById(R.id.btnAgregarItem);
        Button btnRegistrarVenta = findViewById(R.id.btnRegistrarVenta);

        configurarSpinnerCliente();
        configurarSpinnerTipo();
        configurarListaCarrito();

        btnAgregarItem.setOnClickListener(v -> agregarItemAlCarrito());
        btnRegistrarVenta.setOnClickListener(v -> registrarVenta());
    }

    private void configurarSpinnerCliente() {
        listaClientes = new ArrayList<>(DataManager.getInstance().clientes);
        List<String> nombres = new ArrayList<>();
        for (Cliente c : listaClientes) nombres.add(c.getNombre() + " " + c.getApellido());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCliente.setAdapter(adapter);
    }

    private void configurarSpinnerTipo() {
        tipos.add("PRODUCTO");
        tipos.add("SERVICIO");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoItem.setAdapter(adapter);

        spTipoItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarSpinnerItem(tipos.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        cargarSpinnerItem(tipos.get(0));
    }

    private void cargarSpinnerItem(String tipo) {
        List<String> nombres = new ArrayList<>();

        if (tipo.equals("PRODUCTO")) {
            listaProductos = new ArrayList<>(DataManager.getInstance().productos);
            for (Producto p : listaProductos) nombres.add(p.getNombre() + " (Stock: " + p.getStock() + ")");
        } else {
            listaServicios = new ArrayList<>(DataManager.getInstance().servicios);
            for (Servicio s : listaServicios) nombres.add(s.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spItem.setAdapter(adapter);
    }

    private void configurarListaCarrito() {
        adapterCarrito = new ArrayAdapter<String>(this, 0, new ArrayList<>()) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_detalle_venta, parent, false);
                }
                DetalleVenta detalle = carrito.get(position);
                TextView tvInfo = convertView.findViewById(R.id.tvInfoDetalle);
                Button btnQuitar = convertView.findViewById(R.id.btnQuitar);

                tvInfo.setText(detalle.getNombreItem() + " x" + detalle.getCantidad()
                        + " = S/ " + String.format(Locale.getDefault(), "%.2f", detalle.getSubtotal()));

                btnQuitar.setOnClickListener(v -> {
                    carrito.remove(position);
                    actualizarListaYTotal();
                });

                return convertView;
            }

            @Override
            public int getCount() {
                return carrito.size();
            }
        };
        lvDetalles.setAdapter(adapterCarrito);
    }

    private void agregarItemAlCarrito() {
        String cantidadStr = etCantidad.getText().toString().trim();
        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingresa una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cantidad <= 0) {
            Toast.makeText(this, "La cantidad debe ser mayor a cero", Toast.LENGTH_SHORT).show();
            return;
        }

        String tipoSeleccionado = tipos.get(spTipoItem.getSelectedItemPosition());
        int posicionItem = spItem.getSelectedItemPosition();

        if (tipoSeleccionado.equals("PRODUCTO")) {
            if (listaProductos == null || listaProductos.isEmpty()) {
                Toast.makeText(this, "No hay productos disponibles", Toast.LENGTH_SHORT).show();
                return;
            }
            Producto producto = listaProductos.get(posicionItem);

            int cantidadYaEnCarrito = 0;
            for (DetalleVenta d : carrito) {
                if (d.getTipo().equals("PRODUCTO") && d.getIdItem() == producto.getId()) {
                    cantidadYaEnCarrito += d.getCantidad();
                }
            }

            if (cantidad + cantidadYaEnCarrito > producto.getStock()) {
                Toast.makeText(this, "Stock insuficiente. Disponible: " + producto.getStock(), Toast.LENGTH_SHORT).show();
                return;
            }

            carrito.add(new DetalleVenta("PRODUCTO", producto.getId(), producto.getNombre(), cantidad, producto.getPrecio()));
        } else {
            if (listaServicios == null || listaServicios.isEmpty()) {
                Toast.makeText(this, "No hay servicios disponibles", Toast.LENGTH_SHORT).show();
                return;
            }
            Servicio servicio = listaServicios.get(posicionItem);
            carrito.add(new DetalleVenta("SERVICIO", servicio.getId(), servicio.getNombre(), cantidad, servicio.getPrecio()));
        }

        etCantidad.setText("");
        actualizarListaYTotal();
    }

    private void actualizarListaYTotal() {
        adapterCarrito.notifyDataSetChanged();
        double total = 0;
        for (DetalleVenta d : carrito) total += d.getSubtotal();
        tvTotal.setText("Total: S/ " + String.format(Locale.getDefault(), "%.2f", total));
    }

    private void registrarVenta() {
        if (listaClientes.isEmpty()) {
            Toast.makeText(this, "No hay clientes registrados", Toast.LENGTH_SHORT).show();
            return;
        }
        if (carrito.isEmpty()) {
            Toast.makeText(this, "Agrega al menos un item a la venta", Toast.LENGTH_SHORT).show();
            return;
        }

        int idCliente = listaClientes.get(spCliente.getSelectedItemPosition()).getId();
        Empleado usuarioActual = DataManager.getInstance().getUsuarioActual();

        Venta venta = new Venta(DataManager.getInstance().generarIdVenta(), idCliente, usuarioActual.getId(), new Date());
        for (DetalleVenta d : carrito) {
            venta.addDetalle(d);
            if (d.getTipo().equals("PRODUCTO")) {
                for (Producto p : DataManager.getInstance().productos) {
                    if (p.getId() == d.getIdItem()) {
                        p.setStock(p.getStock() - d.getCantidad());
                        break;
                    }
                }
            }
        }

        DataManager.getInstance().ventas.add(venta);
        Toast.makeText(this, "Venta registrada correctamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}