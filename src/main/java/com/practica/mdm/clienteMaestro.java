package com.practica.mdm;

public class clienteMaestro {
    private String nombreCompleto;
    private String emailPrincipal;
    private String telefonoPrincipal;

    public clienteMaestro(String nombreCompleto, String emailPrincipal, String telefonoPrincipal) {
        this.nombreCompleto = nombreCompleto;
        this.emailPrincipal = emailPrincipal;
        this.telefonoPrincipal = telefonoPrincipal;

    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    public String getEmailPrincipal() {
        return emailPrincipal;
    }

    public void setEmailPrincipal(String emailPrincipal) {
        this.emailPrincipal = emailPrincipal;
    }

    public String getTelefonoPrincipal() {
        return telefonoPrincipal;
    }

    public void setTelefonoPrincipal(String telefonoPrincipal) {
        this.telefonoPrincipal = telefonoPrincipal;
    }
}
