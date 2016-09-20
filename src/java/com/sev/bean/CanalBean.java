/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.CanalDAO;
import com.sev.entity.CanalCaptacion;
import com.sev.entity.Usuario;
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
 *
 * @author Axel Latorre, Jorge Castañeda Tutor: Ing. Vanessa Jurado
 *
 */
@ManagedBean
@ViewScoped
public class CanalBean implements Serializable {

    private List<CanalCaptacion> listadoCanales = new ArrayList<>();
    private List<CanalCaptacion> filteredCanales;
    private Usuario sessionUsuario;
    private CanalCaptacion canal = new CanalCaptacion();
    private CanalDAO daoCanal = new CanalDAO();

    public void authorized() {
    }

    public CanalBean() throws SQLException {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            if (sessionUsuario == null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("Usuario");
                String url = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("SesionExpirada");
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            } else {
                listadoCanales = daoCanal.findAll();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void showEditDialog(CanalCaptacion ca) {
        canal = ca;
    }

    public void onCancelDialog() {
        setCanal(new CanalCaptacion());
    }

    public void commitEdit() throws SQLException {
        daoCanal.editCanal(canal, sessionUsuario);
        listadoCanales = daoCanal.findAll();
    }

    public void commitCreate() throws SQLException {
        daoCanal.createCanal(canal, sessionUsuario);
        listadoCanales = daoCanal.findAll();
    }

    public void eliminar(CanalCaptacion ca) throws SQLException {
        daoCanal.deleteCanal(ca, sessionUsuario);
        listadoCanales = daoCanal.findAll();
    }

    public List<CanalCaptacion> getListadoCanales() {
        return listadoCanales;
    }

    public void setListadoCanales(List<CanalCaptacion> listadoCanales) {
        this.listadoCanales = listadoCanales;
    }

    public List<CanalCaptacion> getFilteredCanales() {
        return filteredCanales;
    }

    public void setFilteredCanales(List<CanalCaptacion> filteredCanales) {
        this.filteredCanales = filteredCanales;
    }

    public CanalCaptacion getCanal() {
        return canal;
    }

    public void setCanal(CanalCaptacion canal) {
        this.canal = canal;
    }

    public CanalDAO getDaoCanal() {
        return daoCanal;
    }

    public void setDaoCanal(CanalDAO daoCanal) {
        this.daoCanal = daoCanal;
    }

}
