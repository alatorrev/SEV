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
    private int idprospecto;
    private String canal;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String celular;
    private String casa;
    private String email;
    private String establecimientoProveniente;
    private String captador;
    private int idcanal;
    private String DescripcionCanal;

    public int getIdprospecto() {
        return idprospecto;
    }

    public void setIdprospecto(int idprospecto) {
        this.idprospecto = idprospecto;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCasa() {
        return casa;
    }

    public void setCasa(String casa) {
        this.casa = casa;
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

    public int getIdcanal() {
        return idcanal;
    }

    public void setIdcanal(int idcanal) {
        this.idcanal = idcanal;
    }

    public String getDescripcionCanal() {
        return DescripcionCanal;
    }

    public void setDescripcionCanal(String DescripcionCanal) {
        this.DescripcionCanal = DescripcionCanal;
    }
    
    
    
}
