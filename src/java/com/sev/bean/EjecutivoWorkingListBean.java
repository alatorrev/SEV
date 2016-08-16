/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.ProspectoDAO;
import com.sev.entity.Prospecto;
import com.sev.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author a_latorre
 */
@ManagedBean
@ViewScoped
public class EjecutivoWorkingListBean implements Serializable {

    private List<Prospecto> listadoProspecto = new ArrayList<>();
    private List<Prospecto> filteredProspecto;
    private Usuario u = new Usuario();
    private ProspectoDAO daoProspecto = new ProspectoDAO();
    private Prospecto prospecto = new Prospecto();

    public EjecutivoWorkingListBean() {
        u = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
        listadoProspecto = daoProspecto.readProspectobyUsuario(u.getCedula());
    }
    
    public String jumpContactarDetalle(){
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Prospecto", prospecto);
        return "/contactarProspecto?faces-redirect=true&amp;includeViewParams=true";
    }
    
    public void showEditDialog(Prospecto p) {
        prospecto = p;
    }

    public void onCancelDialog() {
        setProspecto(new Prospecto());
    }

    public List<Prospecto> getListadoProspecto() {
        return listadoProspecto;
    }

    public void setListadoProspecto(List<Prospecto> listadoProspecto) {
        this.listadoProspecto = listadoProspecto;
    }

    public List<Prospecto> getFilteredProspecto() {
        return filteredProspecto;
    }

    public void setFilteredProspecto(List<Prospecto> filteredProspecto) {
        this.filteredProspecto = filteredProspecto;
    }

    public Prospecto getProspecto() {
        return prospecto;
    }

    public void setProspecto(Prospecto prospecto) {
        this.prospecto = prospecto;
    }

}
