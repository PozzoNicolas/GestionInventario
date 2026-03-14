package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioProducto {

void agregarProducto (Producto producto);
void eliminarProducto (Long id);
Producto buscarProductoPorSku (String sku);
List <Producto> listarProductosPorCategoria (CategoriaProducto categoria);
List <Producto> listarTodosLosProductos ();
List <Producto> listarProductosConStockMenorA (Integer limite);
List <Producto> listarProductosPorNombre (String nombre);

}
