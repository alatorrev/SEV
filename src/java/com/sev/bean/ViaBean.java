/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.RolDAO;
import com.sev.dao.UsuarioDAO;
import com.sev.dao.CanalDAO;
import com.sev.dao.ViaDAO;
import com.sev.entity.CanalCaptacion;
import com.sev.entity.Rol;
import com.sev.entity.Usuario;
import com.sev.entity.ViaComunicacion;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
@ManagedBean
@ViewScoped
public class ViaBean implements Serializable {

    private List<ViaComunicacion> listadoVias = new ArrayList<>();
    private Usuario sessionUsuario;
    private List<ViaComunicacion> filteredVias;
    private ViaComunicacion via = new ViaComunicacion();
    private ViaDAO daoVia = new ViaDAO();

    public void authorized() {
    }
    
    public ViaBean() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            if (sessionUsuario == null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("Usuario");
                String url = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("SesionExpirada");
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            } else {
                /**
                 * se ejecutan las lineas del constructor**
                 */
                listadoVias = daoVia.findAll();
            }
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }
    }

    public void showEditDialog(ViaComunicacion vi) {
        via = vi;
    }

    public void onCancelDialog() {
        setVia(new ViaComunicacion());
    }

    public void commitEdit() throws SQLException {
        daoVia.editVia(via, sessionUsuario);
        listadoVias = daoVia.findAll();
    }

    public void commitCreate() throws SQLException {
        daoVia.createVia(via, sessionUsuario);
        listadoVias = daoVia.findAll();
    }

    public void eliminar(ViaComunicacion vi) throws SQLException {
        daoVia.deleteVia(vi, sessionUsuario);
        listadoVias = daoVia.findAll();
    }

    public List<ViaComunicacion> getListadoVias() {
        return listadoVias;
    }

    public void setListadoVias(List<ViaComunicacion> listadoVias) {
        this.listadoVias = listadoVias;
    }

    public List<ViaComunicacion> getFilteredVias() {
        return filteredVias;
    }

    public void setFilteredVias(List<ViaComunicacion> filteredVias) {
        this.filteredVias = filteredVias;
    }

    public ViaComunicacion getVia() {
        return via;
    }

    public void setVia(ViaComunicacion via) {
        this.via = via;
    }

    public ViaDAO getDaoVia() {
        return daoVia;
    }

    public void setDaoVia(ViaDAO daoVia) {
        this.daoVia = daoVia;
    }

}
