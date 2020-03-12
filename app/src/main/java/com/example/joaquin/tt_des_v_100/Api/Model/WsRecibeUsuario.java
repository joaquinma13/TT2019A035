package com.example.joaquin.tt_des_v_100.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WsRecibeUsuario {

    private String id_user;
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasena;
    private String bandera;
    private String codigo;
    private String token;

    public WsRecibeUsuario(String id_user, String nombre, String correo, String telefono, String contrasena,
                           String bandera, String token){
        this.id_user = id_user;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.bandera = bandera;
        this.token = token;
    }

    public WsRecibeUsuario(String id_user, String contrasena, String codigo, String bandera){
        this.id_user = id_user;
        this.contrasena = contrasena;
        this.codigo = codigo;
        this.bandera = bandera;
    }

    public WsRecibeUsuario(String id_user, String telefono){
        this.id_user = id_user;
        this.telefono = telefono;
    }

    @SerializedName("Usuario")
    public List<WsRecibeUsuario.Usuario> Usuario = new ArrayList<>();

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
