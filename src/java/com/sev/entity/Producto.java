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
public class Producto implements Serializable{
    int idprod;
    String descripcion;
    double precio;
    Date fechavigenciai;
    Date fechavigenciaf;
    String estado;

    public int getIdprod() {
        return idprod;
    }

    public void setIdprod(int idprod) {
        this.idprod = idprod;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Date getFechavigenciai() {
        return fechavigenciai;
    }

    public void setFechavigenciai(Date fechavigenciai) {
        this.fechavigenciai = fechavigenciai;
    }

    public Date getFechavigenciaf() {
        return fechavigenciaf;
    }

    public void setFechavigenciaf(Date fechavigenciaf) {
        this.fechavigenciaf = fechavigenciaf;
    }

    

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    
    
    
}
