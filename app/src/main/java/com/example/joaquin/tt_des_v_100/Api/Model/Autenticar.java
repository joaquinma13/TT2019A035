package com.example.joaquin.tt_des_v_100.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Autenticar {

    private String identificador;
    private String contrasena;

    public Autenticar(String identificador, String contrasena) {
        this.identificador = identificador;
        this.contrasena = contrasena;
    }

    @SerializedName("Usuario")
    public List<Autenticar.Usuario> Usuario = new ArrayList<>();

    @SerializedName("Estatus")
    public String Estatus;

    public class Usuario{

        @SerializedName("id_user")
        public String id_user;

        @SerializedName("nombre")
        public String nombre;

        @SerializedName("correo")
        public String correo;

        @SerializedName("telefono")
        public String telefono;

        @SerializedName("contrasena")
        public String contrasena;

    }
}
