package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Producto;
import com.tallerwebi.dominio.ServicioProducto;
import com.tallerwebi.dominio.excepcion.ProductoExistente;

public class ControladorProductoTest {

    private ControladorProducto controladorProducto;
    private ServicioProducto servicioProductoMock;

    @BeforeEach
    public void setUp(){
        servicioProductoMock = mock(ServicioProducto.class);
        controladorProducto = new ControladorProducto(servicioProductoMock);
    }

    @Test
    public void queAlPedirLaListaDeProductosMeDevuelvaLaVistaCorrectaYLaLista(){

        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto());

        when(servicioProductoMock.listarTodos()).thenReturn(lista);

        ModelAndView mav = controladorProducto.verProductos();

        assertThat(mav.getViewName(), is("lista-productos"));
        assertThat(mav.getModel().get("productos"), is(notNullValue()));

        List<Producto> listaEnModelo = (List<Producto>) mav.getModel().get("productos");
        assertThat(listaEnModelo, hasSize(1));
    }

    @Test
    public void queAlGuardarUnProductoExitosamenteRedirijaALaLista(){

        Producto producto = new Producto();

        ModelAndView mav = controladorProducto.guardarProducto(producto);
        assertThat(mav.getViewName(), is("redirect:/productos"));
        verify(servicioProductoMock, times(1)).agregarNuevoProducto(producto);
    }

    @Test
    public void queAlGuardarProductoExistenteMeDevuelvaAlFormularioConError(){

        Producto producto = new Producto();

        doThrow(ProductoExistente.class).when(servicioProductoMock).agregarNuevoProducto(any());

        ModelAndView mav = controladorProducto.guardarProducto(producto);

        assertThat(mav.getViewName(), is("formulario-producto"));
        assertThat(mav.getModel().get("error"), is("El SKU ya existe en el sistema."));
    }



}
