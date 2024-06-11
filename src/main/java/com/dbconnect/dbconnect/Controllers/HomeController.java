package com.dbconnect.dbconnect.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.dbconnect.dbconnect.Models.DAO.IClienteDao;
import com.dbconnect.dbconnect.Models.DAO.IProductoDao;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private IProductoDao IProductoDao;

    @Autowired
    private IClienteDao IClienteDao;

    @GetMapping({ "/home" })
    public String home(Model model, HttpSession session) {
        if (session.getAttribute("login").equals(false)) {
            return "redirect:/";
        }
        model.addAttribute("productos", IProductoDao.findAll());
        model.addAttribute("cliente", IClienteDao.findAll());
        model.addAttribute("fullname", session.getAttribute("nombre") + " " + session.getAttribute("apellido"));
        model.addAttribute("rol", session.getAttribute("rol"));
        return "/home";
    }

}
