package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioProducto {

void agregarNuevoProducto (Producto producto);
void eliminarProducto (Long id);
Producto buscarProductoPorSku (String sku);
void descontarStock (Long id, Integer unidadesADescontar);
void aumentarStock (Long id, Integer unidadesASumar);
List<Producto> buscarProductosConStockBajo(Integer stockAlerta);
void modificarPrecio(Long id, Double precioNuevo);
List<Producto> listarTodos();
List<CategoriaProducto> listarCategorias();
CategoriaProducto buscarCategoriaPorId(Long idCategoria);
Producto buscarProductoPorId(Long id);
void modificarProducto(Producto producto);
List<Producto> listarPorCategoria (Long idCategoria);
}
