/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.entity;

import java.io.Serializable;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
public class InteresProspecto implements Serializable {
    int idInteresProspecto;
    String descripcion;

    public int getIdInteresProspecto() {
        return idInteresProspecto;
    }

    public void setIdInteresProspecto(int idInteresProspecto) {
        this.idInteresProspecto = idInteresProspecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
