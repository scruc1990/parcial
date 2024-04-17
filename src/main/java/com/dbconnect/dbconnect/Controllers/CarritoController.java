package com.dbconnect.dbconnect.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dbconnect.dbconnect.Models.DAO.ICarritoDao;
import com.dbconnect.dbconnect.Models.DAO.IClienteDao;
import com.dbconnect.dbconnect.Models.DAO.IDetalleDao;
import com.dbconnect.dbconnect.Models.DAO.IEncabezadoDao;
import com.dbconnect.dbconnect.Models.DAO.IProductoDao;
import com.dbconnect.dbconnect.Models.Entity.Carrito;
import com.dbconnect.dbconnect.Models.Entity.Cliente;
import com.dbconnect.dbconnect.Models.Entity.Detalles;
import com.dbconnect.dbconnect.Models.Entity.Encabezado;
import com.dbconnect.dbconnect.Models.Entity.Producto;
import com.dbconnect.dbconnect.Models.Entity.factura;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarritoController {

    @Autowired
    private IClienteDao IClienteDao;
    @Autowired
    private IProductoDao IProductoDao;
    @Autowired
    private ICarritoDao ICarritoDao;
    @Autowired
    private IEncabezadoDao IEncabezadoDao;
    @Autowired
    private IDetalleDao IDetalleDao;

    public String cliente;

    List<Detalles> detalles = new ArrayList<Detalles>();

    factura factura = new factura();
    Encabezado encabezado = new Encabezado();
    Detalles detalleOrden = new Detalles();
    Cliente clienteOrden = new Cliente();

    @GetMapping("/carrito/listar")
    public String ListarCliente(Model model) {
        model.addAttribute("title", "Carrito de compras");
        model.addAttribute("cliente", IClienteDao.findAll());
        model.addAttribute("producto", IProductoDao.findAll());
        model.addAttribute("carrito", new Carrito());
        factura fact = new factura(new Encabezado(), null);
        model.addAttribute("factura", fact.getDetalle());
        fact.getEncabezado().setSubTotal(0);
        model.addAttribute("encabezado", fact.getEncabezado());
        return "/carrito/listar";
    }

    @GetMapping("/carrito/listar/{id}")
    public String ListarClienteEsp(@PathVariable Long id, Model model) {
        model.addAttribute("title", "Carrito de compras");
        model.addAttribute("cliente", IClienteDao.findAll());
        model.addAttribute("producto", IProductoDao.findAll());
        model.addAttribute("carrito", new Carrito(id));
        factura fact = ICarritoDao.findAll(id);
        model.addAttribute("factura", fact.getDetalle());
        System.out.println("encabezado: " + fact.getEncabezado().getId());
        model.addAttribute("encabezado", fact.getEncabezado());

        return "/carrito/listar";
    }

    @PostMapping("/carrito/form")
    public String saveproduct(@ModelAttribute("carrito") Carrito carrito) {
        // No tiene validación si la cantidad supera el stock
        System.out.println(
                "debug: " + carrito.getIdCliente() + " " + carrito.getIdProducto() + " " + carrito.getCantidad());
        ICarritoDao.add(carrito);
        this.cliente = String.valueOf(carrito.getIdCliente());
        return "redirect:/carrito/listar/" + carrito.getIdCliente() + "";
    }

    @GetMapping("/factura/eliminar/{id}/{idCliente}")
    public String delete(@PathVariable long id, @PathVariable long idCliente) {
        // Long idLong = Long.parseLong(id);
        System.out.println("debug: " + id + " " + idCliente);
        ICarritoDao.delete(id);
        return "redirect:/carrito/listar/" + idCliente + "";
    }

    @GetMapping({ "/descuento" })
    public String validateDiscount(
            @RequestParam(name = "descuento", required = false, defaultValue = "0") int descuento, Model model) {
        System.out.println("descuento: " + descuento + " ");
        detalles.stream().forEach(dt -> dt.setDescuento(dt.getValor() * descuento / 100));

        factura.setDetalle(detalles);

        encabezado.setSubTotal(detalles.stream().mapToDouble(dt -> dt.getValor()).sum());
        encabezado.setTotal(detalles.stream().mapToDouble(dt -> dt.getValor() - dt.getDescuento()).sum());
        factura.setEncabezado(encabezado);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", factura);
        model.addAttribute("cliente", IClienteDao.findAll());
        return "carrito/carrito";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Long id, @RequestParam Integer cantidad, @RequestParam Integer descuento,
            Model model) {
        Detalles detalleOrden = new Detalles();
        Producto producto = new Producto();

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
            // System.out.println(encabezado.getDescuento() + "descuento");
            // if (encabezado.getDescuento()>0) {
            // detalleOrden.setDescuento(detalleOrden.getValor() * encabezado.getDescuento()
            // / 100);
            // }else {
            // detalleOrden.setDescuento(0);
            // }

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
        model.addAttribute("orden", factura);

        return "carrito/carrito";
    }

    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Long id, Model model) {

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
        model.addAttribute("cliente", IClienteDao.findAll());

        return "carrito/carrito";
    }

    @GetMapping("/detalleorden/{id}")
    public String order(Model model, @PathVariable long id) {
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", factura);
        model.addAttribute("cliente", IClienteDao.findById(id));

        encabezado.setIdCliente(id);

        return "carrito/detalleorden";
    }

    @GetMapping("/getCart")
    public String getCart(Model model) {

        model.addAttribute("cart", detalles);
        if (factura.getEncabezado() == null) {
            factura.setEncabezado(new Encabezado());
            factura.getEncabezado().setSubTotal((long) 0);
        }
        model.addAttribute("orden", factura);
        model.addAttribute("cliente", IClienteDao.findAll());
        model.addAttribute("selectClient", new Cliente());

        return "carrito/carrito";
    }

    @GetMapping("/detalleorden/saveOrder")
    public String saveOrder() {

        // orden.setNumero(ordenService.generarNumeroOrden());
        encabezado = IEncabezadoDao.saveEncabezado(encabezado);
        // detalleOrden.setIdEncabezado(encabezado.getId());

        for (Detalles dt : detalles) {
            dt.setIdEncabezado(encabezado.getId());
            System.out.println(dt.getIdProducto());
            IDetalleDao.saveDetalle(dt);
        }

        // ICarritoDao.Save(encabezado, true);
        // ordenService.save(orden);

        // //guardar detalles
        // for (DetalleOrden dt:detalles) {
        // dt.setOrden(orden);
        // detalleOrdenService.save(dt);
        // }

        // ///limpiar lista y orden
        // orden = new Orden();
        // detalles.clear();

        return "redirect:/";
    }

}
