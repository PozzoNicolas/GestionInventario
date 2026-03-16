package com.tallerwebi.dominio;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.ProductoExistente;

@Service
@Transactional
public class ServicioProductoImpl implements ServicioProducto {

    private RepositorioProducto repositorio;

    @Autowired
    public ServicioProductoImpl (RepositorioProducto repositorio){
        this.repositorio = repositorio;
    }

    @Override
    public void agregarNuevoProducto(Producto producto) {
        
        Producto prod = repositorio.buscarProductoPorSku(producto.getSku());
        if (prod != null) {
            throw new ProductoExistente ("El producto ingresado ya existe en la lista.");
        }
        repositorio.agregarProducto(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarProducto'");
    }

    @Override
    public Producto buscarProductoPorSku(String sku) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarProductoPorSku'");
    }

    @Override
    public void descontarStock(Long id, Integer unidadesADescontar) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'descontarStock'");
    }

    @Override
    public void aumentarStock(Long id, Integer unidadesASumar) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'aumentarStock'");
    }

    @Override
    public List<Producto> buscarProductosConStockBajo(Integer stockAlerta) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarProductosConStockBajo'");
    }

    @Override
    public void modificarPrecio(Long id, Double precioNuevo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modificarPrecio'");
    }

}
