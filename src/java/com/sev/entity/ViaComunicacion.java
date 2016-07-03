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
public class ViaComunicacion implements Serializable{
    int idViaComunicacion;
    String descripcion;

    public int getIdViaComunicacion() {
        return idViaComunicacion;
    }

    public void setIdViaComunicacion(int idViaComunicacion) {
        this.idViaComunicacion = idViaComunicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
