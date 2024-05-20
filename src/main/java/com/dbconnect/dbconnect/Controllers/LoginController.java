package com.dbconnect.dbconnect.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbconnect.dbconnect.DTO.AuthDTO;
import com.dbconnect.dbconnect.Models.DAO.IClienteDao;
import com.dbconnect.dbconnect.Models.Entity.Cliente;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("auth")
public class LoginController {

    @Autowired
    private IClienteDao IClienteDao;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        return "auth/login";
    }

    @PostMapping("/login")
    public String saveClient(@Valid AuthDTO cliente, BindingResult result) {

        if (result.hasErrors()) {
            return "redirect:/auth/login";
        }
        // Nota: implementar validaciones de usuario y contraseña
        Cliente user = IClienteDao.findByEmail(cliente.getEmail());

        if (user != null && user.getEmail().equals(user.getEmail()) && user.getPassword().equals(user.getPassword())) {  
            return "redirect:/home";
        }
        return "redirect:/auth/login";
    }

}
