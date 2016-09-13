/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.entity;
import java.io.Serializable;
/**
 *
 * @author usuario1
 */
public class Producto implements Serializable{
    int idprod;
    String descripcion;
    double precio;
    String fechavigenciai;
    String fechavigenciaf;
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

    public String getFechavigenciai() {
        return fechavigenciai;
    }

    public void setFechavigenciai(String fechavigenciai) {
        this.fechavigenciai = fechavigenciai;
    }

    public String getFechavigenciaf() {
        return fechavigenciaf;
    }

    public void setFechavigenciaf(String fechavigenciaf) {
        this.fechavigenciaf = fechavigenciaf;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    
    
    
}
