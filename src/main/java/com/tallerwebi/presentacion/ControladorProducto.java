package com.tallerwebi.presentacion;

import java.beans.PropertyEditorSupport;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.CategoriaProducto;
import com.tallerwebi.dominio.Producto;
import com.tallerwebi.dominio.ServicioProducto;
import com.tallerwebi.dominio.excepcion.ProductoExistente;

@Controller
public class ControladorProducto {

    private ServicioProducto servicioProducto;

    @Autowired
    public ControladorProducto(ServicioProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
    }

    @RequestMapping(path = "/productos", method = RequestMethod.GET)
    public ModelAndView verProductos() {

        ModelMap modelo = new ModelMap();
        List<Producto> productos = servicioProducto.listarTodos();
        modelo.put("productos", productos);
        return new ModelAndView("lista-productos", modelo);
    }

    // 2. Ir al formulario de "Nuevo Producto"
    @RequestMapping(path = "/nuevo-producto", method = RequestMethod.GET)
    public ModelAndView irANuevoProducto() {

        ModelMap modelo = new ModelMap();
        modelo.put("producto", new Producto());
        List<CategoriaProducto> categorias = servicioProducto.listarCategorias();
        modelo.put("categorias", categorias);
        return new ModelAndView("formulario-producto", modelo);
    }

    // guardad producto
    @RequestMapping(path = "/guardar-producto", method = RequestMethod.POST)
    public ModelAndView guardarProducto(@ModelAttribute("producto") Producto producto,
                                        @RequestParam("idCategoria") Long idCategoria) {
        ModelMap modelo = new ModelMap();

        try {
        CategoriaProducto categoriaReal = servicioProducto.buscarCategoriaPorId(idCategoria);
        producto.setCategoria(categoriaReal);
            servicioProducto.agregarNuevoProducto(producto);
            return new ModelAndView("redirect:/productos");
        } catch (ProductoExistente e) {
            modelo.put("error", "El SKU ya existe en el sistema.");
            modelo.put("categorias", servicioProducto.listarCategorias());
            return new ModelAndView("formulario-producto", modelo);
        }
    }

}
