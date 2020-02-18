package com.example.joaquin.tt_des_v_100.Api.Model;

import com.google.gson.annotations.SerializedName;

public class WsRecibeLocation {

    private String V_TMP_USUARIO;
    private String V_TMP_LATITUD;
    private String V_TMP_LONGITUD;
    private String V_TMP_FECHA;
    private String V_TMP_TIPOACCION;
    private String V_TMP_DATOS;

    public WsRecibeLocation(String v_TMP_USUARIO, String v_TMP_LATITUD, String v_TMP_LONGITUD, String v_TMP_FECHA, String V_TMP_TIPOACCION, String V_TMP_DATOS) {
        V_TMP_USUARIO = v_TMP_USUARIO;
        V_TMP_LATITUD = v_TMP_LATITUD;
        V_TMP_LONGITUD = v_TMP_LONGITUD;
        V_TMP_FECHA = v_TMP_FECHA;
        this.V_TMP_TIPOACCION = V_TMP_TIPOACCION;
        this.V_TMP_DATOS = V_TMP_DATOS;
    }

    @SerializedName("Respuesta")
    public String Respuesta;

}
