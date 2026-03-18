package com.tallerwebi.dominio.excepcion;

public class ProductoNoEncontrado extends RuntimeException {
    public ProductoNoEncontrado (String mensaje){
        super (mensaje);
    }

}
