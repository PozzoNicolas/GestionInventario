package com.tallerwebi.dominio.excepcion;

public class ProductoExistente extends RuntimeException{

    public ProductoExistente (String mensaje){
        super (mensaje);
    }

}
