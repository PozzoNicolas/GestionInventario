package com.tallerwebi.dominio;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioProductoImplTest {


    private RepositorioProducto repositorioMock;
    private ServicioProducto servicioProducto;

    @BeforeEach
    public void setUp(){
        repositorioMock = mock(RepositorioProducto.class);
        servicioProducto = new ServicioProductoImpl(repositorioMock);          
    }

    @Test
    public void queSePuedaAgregarUnNuevoProducto(){
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Caja");

        Producto producto = new Producto();
        producto.setSku("9242");
        producto.setNombre("ColBox");
        producto.setCategoria(categoria);
        producto.setPrecio(20000.0);
        when(repositorioMock.buscarProductoPorSku("9242")).thenReturn(null);

        servicioProducto.agregarNuevoProducto(producto);

        verify(repositorioMock, times(1)).agregarProducto(producto);;

    }

}
