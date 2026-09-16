package com.t1.model;

public class DetalleVenta {
    private String tipo;
    private int idItem;
    private String nombreItem;
    private int cantidad;
    private double precioUnitario;

    public DetalleVenta(String tipo, int idItem, String nombreItem,
                        int cantidad, double precioUnitario) {
        this.tipo = tipo;
        this.idItem = idItem;
        this.nombreItem = nombreItem;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() { return cantidad * precioUnitario; }

    public String getTipo() { return tipo; }
    public int getIdItem() { return idItem; }
    public String getNombreItem() { return nombreItem; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
}