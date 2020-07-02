package com.example.joaquin.tt_des_v_100.Api.Model;

import com.google.gson.annotations.SerializedName;


public class WsEnviaEvento {

    private String id_user;
    private String latitud;
    private String longitud;
    private String tipo;
    private String descripcion;
    private String imagen;
    private String fecha;
    private String ruta;
    private String cadena;

    public void setEstado(String estado) {
        this.estado = estado;
    }

    private String estado;



    public String getEstado() {
        return estado;
    }

    public String getId_user() {
        return id_user;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public String getFecha() {
        return fecha;
    }

    public String getRuta() {
        return ruta;
    }

    public String getCadena() {
        return cadena;
    }

    public WsEnviaEvento(String cadena, String ruta, String descripcion, String id_user,
                         String tipo, String latitud, String longitud, String imagen, String fecha, String estado) {
        this.cadena = cadena;
        this.ruta = ruta;
        this.descripcion = descripcion;
        this.id_user = id_user;
        this.tipo = tipo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagen = imagen;
        this.fecha = fecha;
        this.estado = estado;
    }

    //registrar evento
    public WsEnviaEvento(String id_user, String tipo, String descripcion, String latitud, String longitud, String imagen, String fecha){
        this.id_user = id_user;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagen = imagen;
        this.fecha = fecha;
    }

    @SerializedName("Estatus")
    public String Estatus;
}
