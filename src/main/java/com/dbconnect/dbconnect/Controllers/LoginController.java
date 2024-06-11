package com.dbconnect.dbconnect.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.dbconnect.dbconnect.DTO.AuthDTO;
import com.dbconnect.dbconnect.Models.DAO.IClienteDao;
import com.dbconnect.dbconnect.Models.Entity.Cliente;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class LoginController {

    @Autowired
    private IClienteDao IClienteDao;

    @GetMapping({ "/login", "/" })
    public String showLoginForm(Model model, HttpSession session) {
        session.setAttribute("login", false);
        if (session.getAttribute("login").equals(true)) {
            return "redirect:/home";
        }
        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        return "/auth/login";
    }

    @GetMapping("/auth/login")
    public String create(Model model, HttpSession session) {

        if (session.getAttribute("login").equals(true)) {
            return "redirect:/home";
        }
        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        return "/";
    }

    @PostMapping("/auth/login")
    public String login(@Valid AuthDTO cliente, BindingResult result, Model model, HttpSession session) {
        if (session.getAttribute("login").equals(true)) {
            return "redirect:/home";
        }
        if (cliente.getEmail().isBlank()) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("emailEmpty", "Ingrese correo electrónico");
            return "auth/login";
        }

        if (cliente.getPassword().isBlank()) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("passwdEmpty", "Ingrese contraseña");
            return "auth/login";
        }

        Cliente user = IClienteDao.findByEmail(cliente.getEmail());
        if (user == null) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("emailNotFound", "El correo ingresado no se encuentra registrado");
            return "auth/login";
        }

        if (user.getEmail().equals(cliente.getEmail()) && user.getPassword().equals(cliente.getPassword())) {
            session.setAttribute("login", true);
            session.setAttribute("idusuario", user.getId());
            session.setAttribute("nombre", user.getNombre());
            session.setAttribute("apellido", user.getApellido());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("rol", user.getRole());
            return "redirect:/home";
        } else {
            model.addAttribute("cliente", cliente);
            model.addAttribute("passwdNotFound", "Usuario o contraseña incorrectas");
            return "auth/login";
        }

    }

    @GetMapping("/auth/logout")
    public String logout(Model model, HttpSession session) {
        Cliente cliente = new Cliente();
        session.setAttribute("login", false);
        session.removeAttribute("idusuario");
        session.removeAttribute("nombre");
        session.removeAttribute("apellido");
        session.removeAttribute("email");
        session.removeAttribute("rol");
        model.addAttribute("cliente", cliente);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String createUser(Model model, HttpSession session) {
        if (session.getAttribute("login").equals(true)) {
            return "redirect:/home";
        }
        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        return "/auth/register";
    }

    @PostMapping("/auth/register")
    public String saveClient(@Valid Cliente cliente, BindingResult result, Model model, HttpSession session) {
        cliente.setRole("Cliente");
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "/auth/register";
        }

        IClienteDao.saveClient(cliente);

        return "redirect:/";
    }

}
