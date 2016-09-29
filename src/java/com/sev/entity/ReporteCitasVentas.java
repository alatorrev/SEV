/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
public class ReporteCitasVentas implements Serializable {
    String idUsuario;
    String nombresUsuario;
    String apellidosUsuario;
    String idProspecto;
    String nombresProspecto;
    String apellidosProspecto;
    String titulo;
    Date fechaCita;
    Date fechaContacto;
    String Observacion;
    Double precio;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombresUsuario() {
        return nombresUsuario;
    }

    public void setNombresUsuario(String nombresUsuario) {
        this.nombresUsuario = nombresUsuario;
    }

    public String getApellidosUsuario() {
        return apellidosUsuario;
    }

    public void setApellidosUsuario(String apellidosUsuario) {
        this.apellidosUsuario = apellidosUsuario;
    }

    public String getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(String idProspecto) {
        this.idProspecto = idProspecto;
    }

    public String getNombresProspecto() {
        return nombresProspecto;
    }

    public void setNombresProspecto(String nombresProspecto) {
        this.nombresProspecto = nombresProspecto;
    }

    public String getApellidosProspecto() {
        return apellidosProspecto;
    }

    public void setApellidosProspecto(String apellidosProspecto) {
        this.apellidosProspecto = apellidosProspecto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Date getFechaContacto() {
        return fechaContacto;
    }

    public void setFechaContacto(Date fechaContacto) {
        this.fechaContacto = fechaContacto;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String Observacion) {
        this.Observacion = Observacion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    
}
