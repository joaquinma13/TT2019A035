package com.example.joaquin.tt_des_v_100.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WsRecibeBitacora {

    private String id_user;
    private String senal;
    private String bateria;
    private String imei;
    private String modelo;
    private String latitud;
    private String longitud;
    private String fecha;

    public WsRecibeBitacora (String id_user){
        this.id_user = id_user;
    }

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

}
