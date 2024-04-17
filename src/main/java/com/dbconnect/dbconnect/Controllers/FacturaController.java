package com.dbconnect.dbconnect.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.dbconnect.dbconnect.Models.DAO.ICarritoDao;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FacturaController {
    @Autowired
    private ICarritoDao ICarritoDao;

    @GetMapping("/facturas/listar")
    public String listarFacturas(Model model) {
        model.addAttribute("facturas", ICarritoDao.findAll());
        return "facturas/listar";
    }
    
}
