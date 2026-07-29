package com.sistemamedico.app.dto;

public class LoginResponse {

    private String token;
    private String username;
    private String tipoUsuario;

    public LoginResponse(String token, String username, String tipoUsuario) {
        this.token = token;
        this.username = username;
        this.tipoUsuario = tipoUsuario;
    }

    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getTipoUsuario() { return tipoUsuario; }
}