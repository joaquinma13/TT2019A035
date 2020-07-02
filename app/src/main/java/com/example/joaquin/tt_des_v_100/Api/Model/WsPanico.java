package com.example.joaquin.tt_des_v_100.Api.Model;
import com.google.gson.annotations.SerializedName;

public class WsPanico {

    private String id_user;

    public WsPanico(String id_user) {
        this.id_user = id_user;
    }

    @SerializedName("Estatus")
    public String Estatus;
}
