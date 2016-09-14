/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
@ManagedBean
public class CerrarSesionBean implements Serializable{
    
    public void logout() throws IOException {
        System.out.println("saliendo del sistema");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuario", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("Usuario");
        String url = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("Url");
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
    }
}
