package com.dbconnect.dbconnect.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class AuthDTO {

    @NotEmpty(message = "El campo no puede estar en vacio")
    @Email(message = "Ingrese un correo valido")
    private String email;

    @NotEmpty(message = "El campo no puede estar en vacio")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
