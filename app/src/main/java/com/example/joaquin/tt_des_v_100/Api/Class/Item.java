package com.example.joaquin.tt_des_v_100.Api.Class;

public class Item {

    // Cuentas
    private String status;
    private String nombre;
    private String cliente;
    private String producto;
    private String credito;
    private String no_cliente;
    private String dte_ag;
    private String hour_ag;
    private double latitud;
    private double longitud;
    private String addressgoogle;
    private String comentario;
    private boolean state;

    private int index;
    private double distancia;

    // Fotos
    private String strPhoto;
    private String tipoFoto;

    // Ruteo
    private String EMC;
    private int iEMC;


    // Datos de telefono adicionale
    private String tipo;
    private String diasContacto;
    private String lada, telefono, ext;
    private String hora1, hora2;
    private String parentesco;

    // Datos de direcciones adicionales
    private String cp, ciudad;
    private String estado, municipio;
    private String colonia, calle;
    private String numInt, numExt;

    public double distance = 0;
    // Datos de correos adicionales
    private String tipoEmail;
    private String Email;
    private String parEmail;
    private String de_quien;

    private String tipoCuenta;

    private String estrellas;
    private int pago;

    private boolean exist;
    private String id;
    private String radio;



    // Contacto
    public Item(String nombre, String telefono, String status) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.status = status;
    }

    // Zona
    public Item(String id, String nombre, Double latitud, Double longitud, String radio) {
        this.id = id;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.radio = radio;
    }



    public String getStatus() {
        return status;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCliente() {
        return cliente;
    }

    public String getProducto() {
        return producto;
    }

    public String getCredito() {
        return credito;
    }

    public String getNo_cliente() {
        return no_cliente;
    }

    public String getDte_ag() {
        return dte_ag;
    }

    public String getHour_ag() {
        return hour_ag;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getAddressgoogle() {
        return addressgoogle;
    }

    public String getStrPhoto() {
        return strPhoto;
    }

    public String getEMC() {
        return EMC;
    }

    public int getiEMC() {
        return iEMC;
    }

    public double getDistancia() {
        return distancia;
    }


    public int getIndex() {
        return index;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDiasContacto() {
        return diasContacto;
    }

    public String getLada() {
        return lada;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getExt() {
        return ext;
    }

    public String getHora1() {
        return hora1;
    }

    public String getHora2() {
        return hora2;
    }

    public String getParentesco() {
        return parentesco;
    }

    public String getCp() {
        return cp;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getColonia() {
        return colonia;
    }

    public String getCalle() {
        return calle;
    }

    public String getNumInt() {
        return numInt;
    }

    public String getNumExt() {
        return numExt;
    }

    public double getDistance() {
        return distance;
    }

    public String getTipoEmail() {
        return tipoEmail;
    }

    public String getEmail() {
        return Email;
    }

    public String getParEmail() {
        return parEmail;
    }

    public String getTipoFoto() {
        return tipoFoto;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public String getEstrellas() {
        return estrellas;
    }

    public int getPago() {
        return pago;
    }

    public String getComentario() {
        return comentario;
    }

    public String getId() {
        return id;
    }

    public boolean isExist() {
        return exist;
    }
}
