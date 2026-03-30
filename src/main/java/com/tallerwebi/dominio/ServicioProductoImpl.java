package com.tallerwebi.dominio;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.ProductoExistente;
import com.tallerwebi.dominio.excepcion.ProductoNoEncontrado;
import com.tallerwebi.dominio.excepcion.StockInvalido;

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
        repositorio.eliminarProducto(id);
    }

    @Override
    public Producto buscarProductoPorSku(String sku) {
        return repositorio.buscarProductoPorSku(sku);
    }

    @Override
    public void descontarStock(Long id, Integer unidadesADescontar) {
        
        Producto producto = repositorio.buscarProductoPorId(id);
        if(producto == null){
            throw new ProductoNoEncontrado("Producto no encontrado.");
        }

        if(producto.getStockActual() < unidadesADescontar){
            throw new StockInvalido("Error: No es posible eliminar mas unidades de las que existenten. Unidades en stock: " + producto.getStockActual());
        }

        producto.setStockActual(producto.getStockActual() - unidadesADescontar);
        repositorio.modificarProducto(producto);
    }

    @Override
    public void aumentarStock(Long id, Integer unidadesASumar) {
        
        Producto producto = repositorio.buscarProductoPorId(id);
        if (producto == null) {
            throw new ProductoNoEncontrado("Producto no encontrado.");
        }

        if(unidadesASumar <= 0){
            throw new StockInvalido("No se pueden sumar unidades negativas");
        }

        producto.setStockActual(producto.getStockActual()+unidadesASumar);
        repositorio.modificarProducto(producto);
    }

    @Override
    public List<Producto> buscarProductosConStockBajo(Integer stockAlerta) {

        if(stockAlerta < 0){
            throw new StockInvalido("No es posible buscar productos con stock en negativo.");
        }

        List<Producto> productos = repositorio.listarProductosConStockMenorA(stockAlerta);
        return productos;
    }

    @Override
    public void modificarPrecio(Long id, Double precioNuevo) {
        
        Producto producto = repositorio.buscarProductoPorId(id);
        if(producto == null){
            throw new ProductoNoEncontrado("Producto no encontrado.");
        }
        if(precioNuevo <= 0){
            throw new IllegalArgumentException("El precio del producto debe ser positivo"); 
        }

        producto.setPrecio(precioNuevo);
        repositorio.modificarProducto(producto);
    }

    @Override
    public List<Producto> listarTodos() {
        return repositorio.listarTodosLosProductos();
    }

    @Override
    public List<CategoriaProducto> listarCategorias() {
       return repositorio.listarTodasLasCategorias();
    }

    @Override
    public CategoriaProducto buscarCategoriaPorId(Long idCategoria) {
        return repositorio.buscarCategoriaPorId(idCategoria);
    }

    @Override
    public Producto buscarProductoPorId(Long id) {
        return repositorio.buscarProductoPorId(id);
    }

    @Override
    public void modificarProducto(Producto producto) {
    Producto productoExistente = repositorio.buscarProductoPorId(producto.getId());
    if (!productoExistente.getSku().equals(producto.getSku())) {
        if (repositorio.buscarProductoPorSku(producto.getSku()) != null) {
            throw new ProductoExistente("");
        }
    }
    repositorio.agregarProducto(producto);
    }

}
