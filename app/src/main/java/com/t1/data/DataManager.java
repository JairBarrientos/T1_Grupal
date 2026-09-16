package com.t1.data;

import com.t1.model.*;
import java.util.ArrayList;

public class DataManager {

    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public ArrayList<Cliente> clientes = new ArrayList<>();
    public ArrayList<Mascota> mascotas = new ArrayList<>();
    public ArrayList<Empleado> empleados = new ArrayList<>();
    public ArrayList<Producto> productos = new ArrayList<>();
    public ArrayList<Servicio> servicios = new ArrayList<>();
    public ArrayList<Atencion> atenciones = new ArrayList<>();
    public ArrayList<Venta> ventas = new ArrayList<>();

    private int nextClienteId = 1;
    private int nextMascotaId = 1;
    private int nextEmpleadoId = 1;
    private int nextProductoId = 1;
    private int nextServicioId = 1;
    private int nextAtencionId = 1;
    private int nextVentaId = 1;

    public int generarIdCliente() { return nextClienteId++; }
    public int generarIdMascota() { return nextMascotaId++; }
    public int generarIdEmpleado() { return nextEmpleadoId++; }
    public int generarIdProducto() { return nextProductoId++; }
    public int generarIdServicio() { return nextServicioId++; }
    public int generarIdAtencion() { return nextAtencionId++; }
    public int generarIdVenta() { return nextVentaId++; }

    private Empleado usuarioActual;

    public Empleado getUsuarioActual() { return usuarioActual; }
    public void setUsuarioActual(Empleado usuarioActual) { this.usuarioActual = usuarioActual; }

    private DataManager() {
        empleados.add(new Empleado(generarIdEmpleado(), "Carlos", "Ramírez",
                "cramirez", "1234", Rol.VETERINARIO, "Cirugía"));
        empleados.add(new Empleado(generarIdEmpleado(), "Lucía", "Torres",
                "ltorres", "1234", Rol.ASISTENTE, ""));

        clientes.add(new Cliente(generarIdCliente(), "Nandito", "Pérez",
                "12345678", "999888777", "Av. Siempre Viva 123", "nandito@mail.com"));

        productos.add(new Producto(generarIdProducto(), "Alimento Perro 5kg",
                "Croquetas premium", 45.0, 20));
        servicios.add(new Servicio(generarIdServicio(), "Consulta General",
                "Revisión veterinaria", 60.0));
    }
}