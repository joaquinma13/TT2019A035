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


    public Item() {
    }

    // id
    public Item(boolean exist, String id) {
        this.exist = exist;
        this.id = id;
    }


    // Contacto
    public Item(String nombre, String telefono, boolean state) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.state = state;
    }

    // Contacto
    public Item(String nombre, boolean state, Double latitud, Double longitud) {
        this.nombre = nombre;
        this.state = state;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Contacto
    public Item(String nombre, String telefono, String status) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.status = status;
    }


    // Cuentas
    public Item(String status, String nombre, String cliente, String producto, String credito, String no_cliente,
                String dte_ag, String hour_ag, double latitud, double longitud, String tipoCuenta) {
        this.status = status;
        this.nombre = nombre;
        this.cliente = cliente;
        this.producto = producto;
        this.credito = credito;
        this.no_cliente = no_cliente;
        this.dte_ag = dte_ag;
        this.hour_ag = hour_ag;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tipoCuenta = tipoCuenta;
    }

    //Historial
    public Item(String status, String nombre, String cliente, String producto, String credito, String no_cliente,
                String dte_ag, String hour_ag, double latitud, double longitud, String tipoCuenta, String estrellas, int pago, String comentario) {
        this.status = status;
        this.nombre = nombre;
        this.cliente = cliente;
        this.producto = producto;
        this.credito = credito;
        this.no_cliente = no_cliente;
        this.dte_ag = dte_ag;
        this.hour_ag = hour_ag;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tipoCuenta = tipoCuenta;
        this.estrellas = estrellas;
        this.pago = pago;
        this.comentario = comentario;
    }

    /*********************************** DIRECCION ADICIONAL **************************************/
    public Item(String cp, String ciudad, String estado, String municipio, String colonia, String calle,
                String numInt, String numExt, String hora1, String hora2, String diasContacto, String parentesco, String de_quien) {
        this.cp = cp;
        this.ciudad = ciudad;
        this.estado = estado;
        this.municipio = municipio;
        this.colonia = colonia;
        this.calle = calle;
        this.numInt = numInt;
        this.numExt = numExt;
        this.hora1 = hora1;
        this.hora2 = hora2;
        if (diasContacto.contains("1") || diasContacto.contains("0"))
            this.diasContacto = diasContacto;
        else
            this.diasContacto = "1111100";

        this.parentesco = parentesco;
        this.de_quien = de_quien;
    }


    /*********************************** TELEFONO ADICIONAL ***************************************/
    public Item(String tipo, String diasContacto, String lada, String telefono, String hora1,
                String hora2, String parentesco, String ext, String de_quien) {

        this.tipo = tipo;
        if (diasContacto.contains("1") || diasContacto.contains("0"))
            this.diasContacto = diasContacto;
        else
            this.diasContacto = "1111100";
        this.lada = lada;
        this.telefono = telefono;
        this.hora1 = hora1;
        this.hora2 = hora2;
        this.parentesco = parentesco;
        this.ext = ext;
        this.de_quien = de_quien;
    }

    public Item(String tipoEmail, String email, String parentesco, String de_quien) {
        this.tipoEmail = tipoEmail;
        this.Email = email;
        this.parentesco = parentesco;
        this.de_quien = de_quien;
    }

    /*********************************** EMAIL ADICIONAL ***************************************/



    // Ruta
    public Item(String EMC, int iEMC) {
        this.EMC = EMC;
        this.iEMC = iEMC;
    }

    // Markers
    public Item(String nombre, String cliente, String producto, String credito, String no_cliente,
                double latitud, double longitud, int index, double distancia, String status) {
        this.status = status;
        this.nombre = nombre;
        this.cliente = cliente;
        this.producto = producto;
        this.credito = credito;
        this.no_cliente = no_cliente;
        this.latitud = latitud;
        this.longitud = longitud;
        this.index = index;
        this.distancia = distancia;
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
