package com.tallerwebi.infraestructura;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tallerwebi.dominio.CategoriaProducto;
import com.tallerwebi.dominio.Producto;
import com.tallerwebi.dominio.RepositorioProducto;

@Repository
public class RepositorioProductoImpl implements RepositorioProducto {

    private SessionFactory sf;

    public RepositorioProductoImpl(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public void agregarProducto(Producto producto) {
        sf.getCurrentSession().save(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        Session session = sf.getCurrentSession();
        Producto productoABorrar = session.get(Producto.class, id);

        if (productoABorrar != null) {
            session.delete(productoABorrar);
        }
    }

    @Override
    public Producto buscarProductoPorSku(String sku) {

        CriteriaBuilder builder = sf.getCriteriaBuilder();
        CriteriaQuery<Producto> query = builder.createQuery(Producto.class);
        Root<Producto> root = query.from(Producto.class);
        Predicate condicion = builder.equal(root.get("sku"), sku);
        query.select(root).where(condicion);
        try {
            return sf.getCurrentSession().createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Producto> listarProductosPorCategoria(CategoriaProducto categoria) {
        CriteriaBuilder builder = sf.getCriteriaBuilder();
        CriteriaQuery<Producto> query = builder.createQuery(Producto.class);
        Root<Producto> root = query.from(Producto.class);
        query.select(root).where(builder.equal(root.get("categoria"), categoria));
        return sf.getCurrentSession().createQuery(query).getResultList();
    }

    @Override
    public List<Producto> listarTodosLosProductos() {

        return sf.getCurrentSession()
                .createQuery("from Producto", Producto.class)
                .getResultList();
    }

    @Override
    public List<Producto> listarProductosConStockMenorA(Integer limite) {
        
        CriteriaBuilder builder = sf.getCriteriaBuilder();
        CriteriaQuery<Producto> query = builder.createQuery(Producto.class);
        Root<Producto> root = query.from(Producto.class);
        Predicate condicion = builder.lessThanOrEqualTo(root.get("stockActual"), limite);
        query.select(root).where(condicion);
        return sf.getCurrentSession().createQuery(query).getResultList();
    }

    @Override
    public List<Producto> listarProductosPorNombre(String nombre) {
        
        CriteriaBuilder builder = sf.getCriteriaBuilder();
        CriteriaQuery<Producto> query = builder.createQuery(Producto.class);
        Root<Producto> root = query.from(Producto.class);
        Predicate condicion = builder.like(builder.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
        query.select(root).where(condicion);
        return sf.getCurrentSession().createQuery(query).getResultList();
    }

    @Override
    public void modificarProducto(Producto producto) {
        sf.getCurrentSession().update(producto);
    }

    @Override
    public Producto buscarProductoPorId(Long id) {
        return sf.getCurrentSession().find(Producto.class, id);
    }

    @Override
    public List<CategoriaProducto> listarTodasLasCategorias() {
       return sf.getCurrentSession()
            .createQuery("FROM CategoriaProducto", CategoriaProducto.class)
            .getResultList();
    }

    @Override
    public CategoriaProducto buscarCategoriaPorId(Long idCategoria) {
        return sf.getCurrentSession().get(CategoriaProducto.class, idCategoria);
    }

}
