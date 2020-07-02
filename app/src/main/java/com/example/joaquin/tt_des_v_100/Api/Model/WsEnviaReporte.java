package com.example.joaquin.tt_des_v_100.Api.Model;
import com.google.gson.annotations.SerializedName;

public class WsEnviaReporte {

    private String id_user_manda;
    private String id_user_recive;
    private String id_evento;
    private String descripcion;

    public WsEnviaReporte(String id_user_manda, String id_user_recive, String id_evento, String descripcion) {
        this.id_user_manda = id_user_manda;
        this.id_user_recive = id_user_recive;
        this.id_evento = id_evento;
        this.descripcion = descripcion;
    }

    @SerializedName("Estatus")
    public String Estatus;
}
