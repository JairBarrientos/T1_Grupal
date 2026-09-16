package com.t1.model;

public class Empleado {
    private int id;
    private String nombre;
    private String apellido;
    private String usuario;
    private String password;
    private Rol rol;
    private String especialidad;

    public Empleado(int id, String nombre, String apellido, String usuario,
                    String password, Rol rol, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuario = usuario;
        this.password = password;
        this.rol = rol;
        this.especialidad = especialidad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + rol + ")";
    }
}