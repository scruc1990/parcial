package com.dbconnect.dbconnect.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dbconnect.dbconnect.Models.DAO.IClienteDao;
import com.dbconnect.dbconnect.Models.DAO.IDetalleDao;
import com.dbconnect.dbconnect.Models.DAO.IEncabezadoDao;
import com.dbconnect.dbconnect.Models.DAO.IProductoDao;
import com.dbconnect.dbconnect.Models.Entity.Cliente;
import com.dbconnect.dbconnect.Models.Entity.Detalles;
import com.dbconnect.dbconnect.Models.Entity.Encabezado;
import com.dbconnect.dbconnect.Models.Entity.Producto;
import com.dbconnect.dbconnect.Models.Entity.factura;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarritoController {

    @Autowired
    private IClienteDao IClienteDao;
    @Autowired
    private IProductoDao IProductoDao;
    @Autowired
    private IEncabezadoDao IEncabezadoDao;
    @Autowired
    private IDetalleDao IDetalleDao;

    public String cliente;

    List<Detalles> detalles = new ArrayList<Detalles>();
    List<Producto> productos = new ArrayList<Producto>();
    List<Object> productoList = new ArrayList<>();

    factura factura = new factura();
    Encabezado encabezado = new Encabezado();
    Detalles detalleOrden = new Detalles();
    Cliente clienteOrden = new Cliente();
    Producto productosProducto = new Producto();
    Producto producto = new Producto();

    @PostMapping("/cart")
    public String addCart(@RequestParam Long id, @RequestParam Integer cantidad, @RequestParam Integer descuento,
            Model model, HttpSession session) {
        model.addAttribute("fullname", session.getAttribute("nombre") + " " + session.getAttribute("apellido"));
        model.addAttribute("rol", session.getAttribute("rol"));
        model.addAttribute("idusuario", session.getAttribute("idusuario"));
        Detalles detalleOrden = new Detalles();

        Optional<Producto> productoOptional = Optional.ofNullable(IProductoDao.findById(id));
        producto = productoOptional.get();

        // Se agrega el detalle de los productos
        detalleOrden.setIdProducto(id);
        detalleOrden.setCantidad(cantidad);
        detalleOrden.setValor(producto.getValorUni() * cantidad);
        detalleOrden.setDescuento(descuento);

        // detalleOrden.setProducto(producto);

        // validar que le producto no se añada 2 veces
        Long idProducto = producto.getId();
        boolean ingresado = detalles.stream().anyMatch(p -> p.getIdProducto() == idProducto);

        if (!ingresado) {
            detalles.add(detalleOrden);

        }

        // Se agrega el encabezado
        Date fechaCreacion = new Date();
        encabezado.setFecha(fechaCreacion);

        encabezado.setSubTotal(detalles.stream().mapToDouble(dt -> dt.getValor()).sum());
        encabezado.setDescuentoTotal(detalles.stream().mapToDouble(dt -> dt.getDescuento()).sum());
        encabezado.setTotal(detalles.stream().mapToDouble(dt -> dt.getValor() - dt.getDescuento()).sum());
        factura.setDetalle(detalles);
        factura.setEncabezado(encabezado);
        model.addAttribute("cliente", IClienteDao.findAll());
        model.addAttribute("cart", detalles);
        model.addAttribute("producto", IProductoDao.findAll());
        model.addAttribute("orden", factura);

        return "carrito/carrito";
    }

    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Long id, Model model, HttpSession session) {
        model.addAttribute("fullname", session.getAttribute("nombre") + " " + session.getAttribute("apellido"));
        model.addAttribute("rol", session.getAttribute("rol"));
        // lista nueva de prodcutos
        List<Detalles> ordenesNueva = new ArrayList<Detalles>();

        for (Detalles detalleOrden : detalles) {
            if (detalleOrden.getIdProducto() != id) {
                ordenesNueva.add(detalleOrden);
            }
        }

        // poner la nueva lista con los productos restantes
        detalles = ordenesNueva;

        encabezado.setSubTotal(detalles.stream().mapToDouble(dt -> dt.getValor()).sum());
        encabezado.setDescuentoTotal(detalles.stream().mapToDouble(dt -> dt.getDescuento()).sum());
        encabezado.setTotal(detalles.stream().mapToDouble(dt -> dt.getValor() - dt.getDescuento()).sum());
        factura.setDetalle(detalles);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", factura);
        model.addAttribute("producto", IProductoDao.findAll());
        model.addAttribute("cliente", IClienteDao.findAll());

        return "carrito/carrito";
    }

    @GetMapping("/detalleorden/{id}")
    public String order(Model model, @PathVariable long id, HttpSession session) {
        model.addAttribute("fullname", session.getAttribute("nombre") + " " + session.getAttribute("apellido"));
        model.addAttribute("rol", session.getAttribute("rol"));
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", factura);
        model.addAttribute("cliente", IClienteDao.findById(id));

        encabezado.setIdCliente(id);

        return "carrito/detalleorden";
    }

    @GetMapping("/getCart")
    public String getCart(Model model, HttpSession session) {
        if (session.getAttribute("login").equals(false)) {
            return "redirect:/";
        }
        model.addAttribute("fullname", session.getAttribute("nombre") + " " + session.getAttribute("apellido"));
        model.addAttribute("rol", session.getAttribute("rol"));

        model.addAttribute("cart", detalles);
        if (factura.getEncabezado() == null) {
            factura.setEncabezado(new Encabezado());
            factura.getEncabezado().setSubTotal((long) 0);
        }
        model.addAttribute("orden", factura);
        model.addAttribute("cliente", IClienteDao.findAll());
        model.addAttribute("producto", IProductoDao.findAll());
        model.addAttribute("selectClient", new Cliente());

        return "carrito/carrito";
    }

    @GetMapping("/detalleorden/saveOrder")
    public String saveOrder() {

        encabezado = IEncabezadoDao.saveEncabezado(encabezado);

        for (Detalles dt : detalles) {
            dt.setIdEncabezado(encabezado.getId());
            IDetalleDao.saveDetalle(dt);
            producto.setStock(producto.getStock() - dt.getCantidad());
            IProductoDao.edit(producto);
        }

        // ///limpiar carrito para nueva compra
        factura = new factura();
        encabezado = new Encabezado();
        detalleOrden = new Detalles();
        clienteOrden = new Cliente();
        detalles.clear();

        return "redirect:/home";
    }

}
