package modelos;

import java.io.Serializable;

public class Tienda implements Serializable {

    //declara los datos que contiene la clase con private para encapsularlos
    //agregar id para el manejo de datos

    private int id;
    private String nombreTienda;
    private String direccion;
    private double latitud;
    private double longitud;
    private String descripcion;

    public Tienda() {
        //cunado no conocemos los datos completos
    }

    public Tienda(int id, String nombreTienda, String direccion, double latitud, double longitud, String descripcion) {
        this.id = id;
        this.nombreTienda = nombreTienda;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
    }

    //solo los datos que vamos a utilizar
    public Tienda(String nombreTienda, String descripcion) {
        this.nombreTienda = nombreTienda;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
