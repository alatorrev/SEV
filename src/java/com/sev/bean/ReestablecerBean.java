/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.ReestablecerDAO;
import com.sev.entity.Usuario;
import com.sev.entity.ReestablecerContra;
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
public class ReestablecerBean implements Serializable {

    private List<ReestablecerContra> listadoUsuarios = new ArrayList<>();
    private List<ReestablecerContra> filteredUsers;
    private Usuario sessionUsuario;
    private ReestablecerContra reestablecer = new ReestablecerContra();
    private ReestablecerDAO daoReestablecer = new ReestablecerDAO();

    public ReestablecerBean() {
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
                listadoUsuarios = daoReestablecer.findAll();
            }
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }

    }

    public void showEditDialog(ReestablecerContra r) {
        reestablecer = r;
    }

    public void onCancelDialog() {
        setUsuario(new ReestablecerContra());
    }

    public void commitEdit() throws SQLException {
        daoReestablecer.editContra(reestablecer, sessionUsuario);
        listadoUsuarios = daoReestablecer.findAll();
    }

    public List<ReestablecerContra> getListadoUsuarios() {
        return listadoUsuarios;
    }

    public void setListadoUsuarios(List<ReestablecerContra> listadoUsuarios) {
        this.listadoUsuarios = listadoUsuarios;
    }

    public ReestablecerContra getUsuario() {
        return reestablecer;
    }

    public void setUsuario(ReestablecerContra reestablecer) {
        this.reestablecer = reestablecer;
    }

    public List<ReestablecerContra> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<ReestablecerContra> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

}
