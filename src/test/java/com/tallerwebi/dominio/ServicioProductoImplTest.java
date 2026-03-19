package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.excepcion.ProductoExistente;
import com.tallerwebi.dominio.excepcion.ProductoNoEncontrado;
import com.tallerwebi.dominio.excepcion.StockInvalido;

public class ServicioProductoImplTest {

    private RepositorioProducto repositorioMock;
    private ServicioProducto servicioProducto;

    @BeforeEach
    public void setUp() {
        repositorioMock = mock(RepositorioProducto.class);
        servicioProducto = new ServicioProductoImpl(repositorioMock);
    }

    @Test
    public void queSePuedaAgregarUnNuevoProducto() {
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Caja");

        Producto producto = new Producto();
        producto.setSku("9242");
        producto.setNombre("ColBox");
        producto.setCategoria(categoria);
        producto.setPrecio(20000.0);
        when(repositorioMock.buscarProductoPorSku("9242")).thenReturn(null);

        servicioProducto.agregarNuevoProducto(producto);

        verify(repositorioMock, times(1)).agregarProducto(producto);
        ;
    }

    @Test
    public void queAlQuererGuardarUnProductoExistenteLanceExcepcion() {

        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Caja");

        Producto producto = new Producto();
        producto.setSku("9242");
        producto.setNombre("ColBox");
        producto.setCategoria(categoria);
        producto.setPrecio(20000.0);

        when(repositorioMock.buscarProductoPorSku("9242")).thenReturn(producto);

        assertThrows(ProductoExistente.class, () -> {
            servicioProducto.agregarNuevoProducto(producto);
        });

        verify(repositorioMock, never()).agregarProducto(producto);
    }

    @Test
    public void queSePuedaEliminarUnProducto() {
        Long id = 1L;
        servicioProducto.eliminarProducto(id);
        verify(repositorioMock, times(1)).eliminarProducto(id);
    }

    @Test
    public void queSePuedaBuscarUnProductoPreviamenteIngresadoPorSuSku() {
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Caja");

        Producto producto = new Producto();
        producto.setSku("9242");
        producto.setNombre("ColBox");
        producto.setCategoria(categoria);
        producto.setPrecio(20000.0);

        when(repositorioMock.buscarProductoPorSku("9242")).thenReturn(producto);

        Producto productoBuscado = servicioProducto.buscarProductoPorSku(producto.getSku());
        assertThat(productoBuscado.getNombre(), is("ColBox"));
        assertThat(productoBuscado.getPrecio(), is(20000.0));
        verify(repositorioMock, times(1)).buscarProductoPorSku("9242");
    }

    @Test
    public void queSePuedaDescontarElStockDeUnProductoGuardado() {
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Caja");

        Producto producto = new Producto();
        producto.setSku("9242");
        producto.setNombre("ColBox");
        producto.setCategoria(categoria);
        producto.setPrecio(20000.0);
        producto.setStockActual(6);

        when(repositorioMock.buscarProductoPorId(producto.getId())).thenReturn(producto);

        servicioProducto.descontarStock(producto.getId(), 2);

        assertThat(producto.getStockActual(), is(4));
        verify(repositorioMock, times(1)).modificarProducto(producto);
    }

    @Test
    public void queAlQuererDescontarStockDeUnProductoInexistenteLanceExcepcion() {

        when(repositorioMock.buscarProductoPorId(1L)).thenReturn(null);

        assertThrows(ProductoNoEncontrado.class, () -> {
            servicioProducto.descontarStock(1L, 2);
        });
        verify(repositorioMock, times(1)).buscarProductoPorId(1L);
    }

    @Test
    public void queAlQuererDescontarMasUnidadesQueLasQueHayEnStockLanceExcepcion() {
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Caja");

        Producto producto = new Producto();
        producto.setSku("9242");
        producto.setNombre("ColBox");
        producto.setCategoria(categoria);
        producto.setPrecio(20000.0);
        producto.setStockActual(6);

        when(repositorioMock.buscarProductoPorId(producto.getId())).thenReturn(producto);

        assertThrows(StockInvalido.class, () -> {
            servicioProducto.descontarStock(producto.getId(), 7);
        });
        verify(repositorioMock, times(1)).buscarProductoPorId(producto.getId());
        verify(repositorioMock, never()).modificarProducto(producto);
    }
}