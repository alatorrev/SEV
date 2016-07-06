/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.entity;

import java.io.Serializable;

/**
 *
 * @author a_latorre
 */
public class Prospecto implements Serializable {
    private String canal;
    private String nombres;
    private String apellidos;
    private String email;
    private String establecimientoProveniente;
    private String captador;

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstablecimientoProveniente() {
        return establecimientoProveniente;
    }

    public void setEstablecimientoProveniente(String establecimientoProveniente) {
        this.establecimientoProveniente = establecimientoProveniente;
    }

    public String getCaptador() {
        return captador;
    }

    public void setCaptador(String captador) {
        this.captador = captador;
    }
    
    
}
