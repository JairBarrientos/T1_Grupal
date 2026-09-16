package com.t1.ui.clientes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.t1.R;
import com.t1.data.DataManager;
import com.t1.model.Cliente;

public class ClientesActivity extends AppCompatActivity
        implements ClienteAdapter.OnClienteActionListener {

    private ListView lvClientes;
    private ClienteAdapter adapter;

    public static final String EXTRA_CLIENTE_ID = "extra_cliente_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        lvClientes = findViewById(R.id.lvClientes);
        Button btnAgregar = findViewById(R.id.btnAgregarCliente);

        adapter = new ClienteAdapter(this, DataManager.getInstance().clientes, this);
        lvClientes.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(ClientesActivity.this, ClienteFormActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditar(Cliente cliente) {
        Intent intent = new Intent(ClientesActivity.this, ClienteFormActivity.class);
        intent.putExtra(EXTRA_CLIENTE_ID, cliente.getId());
        startActivity(intent);
    }

    @Override
    public void onEliminar(Cliente cliente) {
        DataManager.getInstance().clientes.remove(cliente);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
    }
}