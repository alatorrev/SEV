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
public class ReporteHistorialContactos implements Serializable{
    String idUsuario;
    String nombresUsuario;
    String apellidosUsuario;
    String idProspecto;
    String nombresProspecto;
    String apellidosProspecto;
    int idViaComunicacion;
    String descripcionVia;
    int idInteres;
    String descripcioInteres;
    Date fechaContacto;

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

    public int getIdViaComunicacion() {
        return idViaComunicacion;
    }

    public void setIdViaComunicacion(int idViaComunicacion) {
        this.idViaComunicacion = idViaComunicacion;
    }

    public String getDescripcionVia() {
        return descripcionVia;
    }

    public void setDescripcionVia(String descripcionVia) {
        this.descripcionVia = descripcionVia;
    }

    public int getIdInteres() {
        return idInteres;
    }

    public void setIdInteres(int idInteres) {
        this.idInteres = idInteres;
    }

    public String getDescripcioInteres() {
        return descripcioInteres;
    }

    public void setDescripcioInteres(String descripcioInteres) {
        this.descripcioInteres = descripcioInteres;
    }

    public Date getFechaContacto() {
        return fechaContacto;
    }

    public void setFechaContacto(Date fechaContacto) {
        this.fechaContacto = fechaContacto;
    }
    
}
