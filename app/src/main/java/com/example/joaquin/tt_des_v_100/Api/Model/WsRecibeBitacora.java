package com.example.joaquin.tt_des_v_100.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WsRecibeBitacora {

    private String id_user;
    private String id_zona;
    private String senal;
    private String nombre;
    private String radio;
    private String bateria;
    private String imei;
    private String modelo;
    private String latitud;
    private String longitud;
    private String fecha;
    private String tipo;
    private String descripcion;
    private String imagen;


    public WsRecibeBitacora (String id_user){
        this.id_user = id_user;
    }


    //registrar zonas
    public WsRecibeBitacora(String id_user, String id_zona, String nombre, String latitud, String longitud, String radio){
        this.id_user = id_user;
        this.id_zona = id_zona;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.radio = radio;
    }

    //registrar bitacora
    public WsRecibeBitacora(String id_user, String senal, String bateria, String imei, String modelo, String latitud,
                            String longitud, String fecha){
        this.id_user = id_user;
        this.senal = senal;
        this.bateria = bateria;
        this.imei = imei;
        this.modelo = modelo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
    }


    //registrar evento
    public WsRecibeBitacora(String id_user, String tipo, String descripcion, String latitud, String longitud){
        this.id_user = id_user;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
    }


    @SerializedName("Bitacora")
    public List<WsRecibeBitacora.Bitacora> Bitacora = new ArrayList<>();

    @SerializedName("Estatus")
    public String Estatus;


    public class Bitacora{


        @SerializedName("id_user")
        public String id_user;

        @SerializedName("senal")
        public String senal;

        @SerializedName("bateria")
        public String bateria;

        @SerializedName("imei")
        public String imei;

        @SerializedName("modelo")
        public String modelo;

        @SerializedName("latitud")
        public String latitud;

        @SerializedName("longitud")
        public String longitud;

        @SerializedName("fecha")
        public String fecha;

        @SerializedName("nombre")
        public String nombre;

        @SerializedName("telefono")
        public String telefono;

    }

    @SerializedName("Zonas")
    public List<WsRecibeBitacora.Zonas> Zonas = new ArrayList<>();

    @SerializedName("Resultados")
    public String Resultados;

    public class Zonas{

        @SerializedName("id_zona")
        public String id_zona;

        @SerializedName("nombre")
        public String nombre;

        @SerializedName("latitud")
        public String latitud;

        @SerializedName("longitud")
        public String longitud;

        @SerializedName("radio")
        public String radio;


    }

}
