package com.example.joaquin.tt_des_v_100.Api.Model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WsConfiguracion {


    private String id_user;
    private String distancia;
    private String guardian;
    private String tiempo;
    private String sesion;


    public WsConfiguracion() {
    }

    public WsConfiguracion(String id_user, String distancia, String guardian, String tiempo, String sesion) {
        this.id_user = id_user;
        this.distancia = distancia;
        this.guardian = guardian;
        this.tiempo = tiempo;
        this.sesion = sesion;
    }

    @SerializedName("Configuracion")
    public List<WsConfiguracion.Configuracion> Configuracion = new ArrayList<>();

    @SerializedName("Resultados")
    public String Resultados;

    @SerializedName("Estatus")
    public String Estatus;


    public class Configuracion{

        @SerializedName("id_user")
        public String id_user;

        @SerializedName("distancia")
        public String distancia;

        @SerializedName("guardian")
        public String guardian;

        @SerializedName("tiempo")
        public String tiempo;

        @SerializedName("sesion")
        public String sesion;

    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }
}
