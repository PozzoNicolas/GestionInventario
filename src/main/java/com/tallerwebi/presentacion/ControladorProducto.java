package com.tallerwebi.presentacion;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
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
    
    // 1. La lista para la tabla
    List<Producto> lista = servicioProducto.listarTodos();
    modelo.put("productos", lista);
    
    // 2. EL TRUCO: Mandamos un objeto vacío para que el modal no explote
    modelo.put("producto", new Producto()); 
    
    // 3. Las categorías para que el select del modal tenga opciones
    modelo.put("categorias", servicioProducto.listarCategorias());
    
    // 4. El flag en false (por las dudas)
    modelo.put("abrirModal", false);

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
                                    @RequestParam("idCategoria") Long idCategoria,
                                    @RequestParam("archivoImagen") MultipartFile archivo,
                                    HttpServletRequest request) {
    ModelMap modelo = new ModelMap();

    try {
        CategoriaProducto categoriaReal = servicioProducto.buscarCategoriaPorId(idCategoria);
        producto.setCategoria(categoriaReal);

        // 2. Lógica de la Imagen
        if (archivo != null && !archivo.isEmpty()) {
            // Definimos la carpeta donde se guardarán las fotos (asegúrate de que exista en src/main/webapp/resources/core/img)
            String rutaRelativa = "/resources/core/img/";
            String rutaAbsoluta = request.getServletContext().getRealPath(rutaRelativa);
            
            // Creamos un nombre único para evitar que se pisen (ej: 12345_alfajor.jpg)
            String nombreArchivo = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
            
            try {
                File destino = new File(rutaAbsoluta + File.separator + nombreArchivo);
                archivo.transferTo(destino);
                
                // Guardamos solo el nombre del archivo en el objeto Producto
                producto.setImagen(nombreArchivo);
            } catch (IOException e) {
                e.printStackTrace();
                // Opcional: podrías agregar un mensaje de error al modelo si falla la subida
            }
        } else if (producto.getId() != null) {
            // SI ES EDICIÓN y no subió foto nueva, mantenemos la que ya tenía
            Producto productoActual = servicioProducto.buscarProductoPorId(producto.getId());
            producto.setImagen(productoActual.getImagen());
        }

        // LÓGICA DE DECISIÓN
        if (producto.getId() == null) {
            // Es un producto nuevo
            servicioProducto.agregarNuevoProducto(producto);
        } else {
            // Es un producto que ya existe (Edición)
            servicioProducto.modificarProducto(producto); // Necesitas este método en tu servicio
        }
        
        return new ModelAndView("redirect:/productos");

    } catch (ProductoExistente e) {
        modelo.put("error", "El SKU ya existe en el sistema.");
        modelo.put("categorias", servicioProducto.listarCategorias());
        // Importante: si falla, volvemos a mostrar el formulario/modal
        return new ModelAndView("formulario-producto", modelo); 
    }
}

    @RequestMapping(path = "/obtener-producto/{id}", method = RequestMethod.GET)
    @ResponseBody // Esto hace que devuelva los datos (JSON) y no una vista HTML
    public Producto obtenerProductoParaEditar(@PathVariable("id") Long id) {
        return servicioProducto.buscarProductoPorId(id);
    }

    @RequestMapping(path = "/actualizar-producto", method = RequestMethod.POST)
    public ModelAndView actualizarProducto(@ModelAttribute("producto") Producto producto,
            @RequestParam("idCategoria") Long idCategoria) {

        CategoriaProducto cat = servicioProducto.buscarCategoriaPorId(idCategoria);
        producto.setCategoria(cat);

        servicioProducto.modificarProducto(producto); // El método que ya testeamos
        return new ModelAndView("redirect:/productos");
    }

    @RequestMapping(path = "/preparar-edicion/{id}", method = RequestMethod.GET)
    public ModelAndView prepararEdicion(@PathVariable("id") Long id) {
        ModelMap modelo = new ModelMap();

        // 1. Cargamos la lista de siempre (porque la tabla tiene que estar atrás)
        modelo.put("productos", servicioProducto.listarTodos());
        modelo.put("categorias", servicioProducto.listarCategorias());

        // 2. Buscamos el producto específico que queremos editar
        Producto productoAEditar = servicioProducto.buscarProductoPorId(id);
        modelo.put("producto", productoAEditar);

        // 3. LA SEÑAL: Mandamos un booleano para que el HTML sepa que debe abrir el
        // modal
        modelo.put("abrirModal", true);

        return new ModelAndView("lista-productos", modelo);
    }

}
