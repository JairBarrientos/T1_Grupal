package com.t1.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Venta {
    private int id;
    private int idCliente;
    private int idEmpleado;
    private Date fecha;
    private List<DetalleVenta> detalles;

    public Venta(int id, int idCliente, int idEmpleado, Date fecha) {
        this.id = id;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.detalles = new ArrayList<>();
    }

    public double getTotal() {
        double total = 0;
        for (DetalleVenta d : detalles) {
            total += d.getSubtotal();
        }
        return total;
    }

    public int getId() { return id; }
    public int getIdCliente() { return idCliente; }
    public int getIdEmpleado() { return idEmpleado; }
    public Date getFecha() { return fecha; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void addDetalle(DetalleVenta detalle) { detalles.add(detalle); }
}