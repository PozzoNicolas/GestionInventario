package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.isNotNull;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tallerwebi.dominio.CategoriaProducto;
import com.tallerwebi.dominio.Producto;
import com.tallerwebi.dominio.RepositorioProducto;
import com.tallerwebi.infraestructura.config.HibernateTestConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestConfig.class })
@Transactional
@Rollback
public class RepositorioProductoImplTest {

    @Autowired
    SessionFactory sf;
    @Autowired
    RepositorioProducto repositorioProducto;

    @Test
    public void queSePuedaGuardarUnProducto() {
        /* sku, nombre, categoría, precio */
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Cajas");
        sf.getCurrentSession().save(categoria);

        Producto p = new Producto();
        p.setSku("9242");
        p.setNombre("ColBox");
        p.setCategoria(categoria);
        p.setPrecio(100.0);

        repositorioProducto.agregarProducto(p);
        Producto buscado = sf.getCurrentSession().get(Producto.class, p.getId());
        
        assertThat(buscado, is(notNullValue()));
        assertThat(buscado.getSku(), equalTo("9242"));
        assertThat(buscado.getCategoria().getNombre(), equalTo("Cajas"));
    }

    @Test
    public void queSePuedaEliminarUnProducto() {
        /* sku, nombre, categoría, precio */
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Cajas");
        sf.getCurrentSession().save(categoria);

        Producto p = new Producto();
        p.setSku("9242");
        p.setNombre("ColBox");
        p.setCategoria(categoria);
        p.setPrecio(100.0);

        repositorioProducto.agregarProducto(p);
        repositorioProducto.eliminarProducto(p.getId());
        Producto buscado = sf.getCurrentSession().get(Producto.class, p.getId());
        
        assertThat(buscado, is(nullValue()));
    }

    @Test
    public void poderBuscarUnProductoPorSuSkuYSiExisteQueMeLoDevuelva() {
        /* sku, nombre, categoría, precio */
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Cajas");
        sf.getCurrentSession().save(categoria);

        Producto p = new Producto();
        p.setSku("9242");
        p.setNombre("ColBox");
        p.setCategoria(categoria);
        p.setPrecio(100.0);

        repositorioProducto.agregarProducto(p);

        Producto buscado = repositorioProducto.buscarProductoPorSku("9242");
        
        assertThat(buscado, is(notNullValue()));
        assertThat(buscado.getSku(), equalTo("9242"));
        assertThat(buscado.getNombre(), equalTo("ColBox"));
    }

    @Test
    public void poderBuscarUnProductoPorSuSkuYSiNoExisteQueMeDevuelvaUnProductoNull() {
        Producto buscado = repositorioProducto.buscarProductoPorSku("9244");
        assertThat(buscado, is(nullValue()));
    }

    @Test
    public void poderListarProductosFiltradosPorSuCategoria() {
        /* sku, nombre, categoría, precio */
        CategoriaProducto categoria1 = new CategoriaProducto();
        categoria1.setNombre("Cajas");
        sf.getCurrentSession().save(categoria1);

        CategoriaProducto categoria2 = new CategoriaProducto();
        categoria2.setNombre("Hermeticos");
        sf.getCurrentSession().save(categoria2);

        Producto p = new Producto();
        p.setSku("9242");
        p.setNombre("ColBox");
        p.setCategoria(categoria1);
        p.setPrecio(100.0);
        Producto p2 = new Producto();
        p2.setSku("70");
        p2.setNombre("Contenedor ultra");
        p2.setCategoria(categoria2);
        p2.setPrecio(10.0);
        Producto p3 = new Producto();
        p3.setSku("9419");
        p3.setNombre("ColBox");
        p3.setCategoria(categoria1);
        p3.setPrecio(100.0);

        repositorioProducto.agregarProducto(p);
        repositorioProducto.agregarProducto(p2);
        repositorioProducto.agregarProducto(p3);

        List<Producto> filtrados = repositorioProducto.listarProductosPorCategoria(categoria1);
        
        assertThat(filtrados, hasSize(2));
        assertThat(filtrados, containsInAnyOrder(p, p3));
        assertThat(filtrados.get(0).getNombre(), equalToIgnoringCase("colbox"));
    }

    @Test
    public void poderListarTodosLosProductos(){
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Cajas");
        sf.getCurrentSession().save(categoria);

        Producto p = new Producto();
        p.setSku("9242");
        p.setNombre("ColBox");
        p.setCategoria(categoria);
        p.setPrecio(100.0);
        Producto p2 = new Producto();
        p2.setSku("70");
        p2.setNombre("Contenedor ultra");
        p2.setCategoria(categoria);
        p2.setPrecio(10.0);

        repositorioProducto.agregarProducto(p);
        repositorioProducto.agregarProducto(p2);

        List<Producto> buscados = repositorioProducto.listarTodosLosProductos();

        assertThat(buscados, hasSize(2));
        assertThat(buscados, containsInAnyOrder(p, p2));
    }

    @Test
    public void quePuedaListarProductosConStockIgualOMenorAUnNumeroAsignado(){

        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Cajas");
        sf.getCurrentSession().save(categoria);

        Producto p = new Producto();
        p.setSku("9242");
        p.setNombre("ColBox");
        p.setCategoria(categoria);
        p.setPrecio(100.0);
        p.setStockActual(5);
        Producto p2 = new Producto();
        p2.setSku("70");
        p2.setNombre("Contenedor ultra");
        p2.setCategoria(categoria);
        p2.setPrecio(10.0);
        p2.setStockActual(2);
        Producto p3 = new Producto();
        p3.setSku("700");
        p3.setNombre("Contenedor ultra");
        p3.setCategoria(categoria);
        p3.setPrecio(10.0);
        p3.setStockActual(8);

        repositorioProducto.agregarProducto(p);
        repositorioProducto.agregarProducto(p2);
        repositorioProducto.agregarProducto(p3);

        List<Producto> filtrados = repositorioProducto.listarProductosConStockMenorA(5);

        assertThat(filtrados, hasSize(2));
        assertThat(filtrados, containsInAnyOrder(p, p2));
    }

    @Test
    public void queSePuedaListarProductosPorCoincidenciaEnElNombre(){

        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Cajas");
        sf.getCurrentSession().save(categoria);

        Producto p = new Producto();
        p.setSku("9242");
        p.setNombre("ColBox");
        p.setCategoria(categoria);
        p.setPrecio(100.0);
    
        Producto p2 = new Producto();
        p2.setSku("70");
        p2.setNombre("Contenedor ultra");
        p2.setCategoria(categoria);
        p2.setPrecio(10.0);

        repositorioProducto.agregarProducto(p);
        repositorioProducto.agregarProducto(p2);

        List<Producto> filtrados = repositorioProducto.listarProductosPorNombre("co");

        assertThat(filtrados, hasSize(2));
        assertThat(filtrados, containsInAnyOrder(p, p2));
    }


}
