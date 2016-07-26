/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.RolDAO;
import com.sev.entity.Rol;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Axel Latorre, Jorge Castañeda
 */
@ManagedBean
@ViewScoped
public class AsignarRecursoBean implements Serializable {

    private String entityValue = "usuario";
    private boolean hidefilter = true;
    private final RolDAO daoRol = new RolDAO();
    private List<Rol> listaRol = daoRol.findAll();
    private int rolIdSelected=0;
    private String usuarioSelected;
    public void radioSelected() {
        this.hidefilter = entityValue.equals("usuario");
    }

    
    
    
    public String getEntityValue() {
        return entityValue;
    }

    public void setEntityValue(String entityValue) {
        this.entityValue = entityValue;
    }

    public boolean isHidefilter() {
        return hidefilter;
    }

    public void setHidefilter(boolean hidefilter) {
        this.hidefilter = hidefilter;
    }

    public List<Rol> getListaRol() {
        return listaRol;
    }

    public void setListaRol(List<Rol> listaRol) {
        this.listaRol = listaRol;
    }

    public int getRolIdSelected() {
        return rolIdSelected;
    }

    public void setRolIdSelected(int rolIdSelected) {
        this.rolIdSelected = rolIdSelected;
    }

    public String getUsuarioSelected() {
        return usuarioSelected;
    }

    public void setUsuarioSelected(String usuarioSelected) {
        this.usuarioSelected = usuarioSelected;
    }

    
}
