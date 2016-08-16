/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.entity.Prospecto;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author a_latorre
 */
@ManagedBean
@ViewScoped
public class ContactarProspectoBean implements Serializable{
    private String cedulaProspecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cedulaProspecto");

    public void verValues(){
        System.out.println(cedulaProspecto);
    }
    
    public String getCedulaProspecto() {
        return cedulaProspecto;
    }

    public void setCedulaProspecto(String cedulaProspecto) {
        this.cedulaProspecto = cedulaProspecto;
    }

    
    
    




}
