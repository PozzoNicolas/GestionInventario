package com.tallerwebi.dominio.excepcion;

public class StockInvalido extends RuntimeException {

    public StockInvalido (String mensaje){
        super(mensaje);
    }

}
