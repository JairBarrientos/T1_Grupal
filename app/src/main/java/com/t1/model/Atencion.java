package com.t1.model;

import java.util.Date;

public class Atencion {
    private int id;
    private int idMascota;
    private int idVeterinario;
    private Date fecha;
    private String motivo;
    private EstadoAtencion estado;
    private String observaciones;

    public Atencion(int id, int idMascota, int idVeterinario, Date fecha,
                    String motivo, EstadoAtencion estado, String observaciones) {
        this.id = id;
        this.idMascota = idMascota;
        this.idVeterinario = idVeterinario;
        this.fecha = fecha;
        this.motivo = motivo;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }
    public int getIdVeterinario() { return idVeterinario; }
    public void setIdVeterinario(int idVeterinario) { this.idVeterinario = idVeterinario; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public EstadoAtencion getEstado() { return estado; }
    public void setEstado(EstadoAtencion estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}