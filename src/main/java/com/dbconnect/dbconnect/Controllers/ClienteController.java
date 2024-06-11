package com.dbconnect.dbconnect.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.dbconnect.dbconnect.Models.DAO.IClienteDao;
import com.dbconnect.dbconnect.Models.DAO.IEmailServices;
import com.dbconnect.dbconnect.Models.Entity.Cliente;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClienteController {

    @Autowired
    private IClienteDao IClienteDao;
    
    @Autowired
    private IEmailServices IEmailServices;

    @GetMapping("/cliente/listar")
    public String Listar(Model model, HttpSession session) {
        if (session.getAttribute("login").equals(false)) {
            return "redirect:/";
        }
        model.addAttribute("fullname", session.getAttribute("nombre") + " " + session.getAttribute("apellido"));
        model.addAttribute("rol", session.getAttribute("rol"));
        model.addAttribute("title", "Listado de Clientes");
        model.addAttribute("cliente", IClienteDao.findAll());
        return "/cliente/listar";
    }

    @GetMapping("/cliente/form")
    public String create(Model model, HttpSession session) {
        if (session.getAttribute("login").equals(false)) {
            return "redirect:/";
        }
        model.addAttribute("fullname", session.getAttribute("nombre") + " " + session.getAttribute("apellido"));
        model.addAttribute("rol", session.getAttribute("rol"));
        Cliente cliente = new Cliente();
        model.addAttribute("title", "Nuevo Cliente");
        model.addAttribute("cliente", cliente);
        return "/cliente/form";
    }

    @PostMapping("/cliente/form")
    public String saveClient(@Valid Cliente cliente, BindingResult result, Model model, HttpSession session) {
        if (session.getAttribute("login").equals(false)) {
            return "redirect:/";
        }
        model.addAttribute("fullname", session.getAttribute("nombre") + " " + session.getAttribute("apellido"));
        model.addAttribute("rol", session.getAttribute("rol"));
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("title", "Nuevo Cliente");
            return "/cliente/form";
        }
        IClienteDao.saveClient(cliente);
        IEmailServices.sendEmail(cliente.getEmail(), "Prueba", "Usuario creado con exito");

        return "redirect:/cliente/listar";
    }

    @GetMapping("/cliente/eliminar/{id}")
    public String delete(@PathVariable String id) {
        Long idLong = Long.parseLong(id);
        IClienteDao.delete(idLong);
        return "redirect:/cliente/listar";
    }

    @GetMapping("/cliente/editar/{param}")
    public String edit(@PathVariable String param, Model model, HttpSession session) {
        model.addAttribute("fullname", session.getAttribute("nombre") + " " + session.getAttribute("apellido"));
        model.addAttribute("rol", session.getAttribute("rol"));

        Long id = Long.parseLong(param);

        model.addAttribute("title", "Editar Cliente");
        model.addAttribute("cliente", IClienteDao.findById(id));

        return "/cliente/formEditar";
    }

    @PostMapping("/cliente/editar")
    public String edit(@Valid Cliente cliente, BindingResult result, Model model, HttpSession session) {
        model.addAttribute("fullname", session.getAttribute("nombre") + " " + session.getAttribute("apellido"));
        model.addAttribute("rol", session.getAttribute("rol"));
        if (result.hasErrors()) {
            model.addAttribute("title", "Editar Cliente");
            return "/cliente/formEditar";
        }
        IClienteDao.edit(cliente);

        return "redirect:/cliente/listar";
    }

}
