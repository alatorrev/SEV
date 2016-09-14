/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.InteresDAO;
import com.sev.entity.InteresProspecto;
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
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
@ManagedBean
@ViewScoped
public class InteresBean implements Serializable {

    private List<InteresProspecto> listadoIntereses = new ArrayList<>();
    private Usuario sessionUsuario;
    private List<InteresProspecto> filteredIntereses;
    private InteresProspecto interes = new InteresProspecto();
    private InteresDAO daoInteres = new InteresDAO();

    public InteresBean(){
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
                listadoIntereses = daoInteres.findAll();
            }
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }
    }

    public void showEditDialog(InteresProspecto in) {
        interes = in;
    }

    public void onCancelDialog() {
        setInteres(new InteresProspecto());
    }

    public void commitEdit() throws SQLException {
        daoInteres.editInteres(interes, sessionUsuario);
        listadoIntereses = daoInteres.findAll();
    }

    public void commitCreate() throws SQLException {
        daoInteres.createInteres(interes, sessionUsuario);
        listadoIntereses = daoInteres.findAll();
    }

    public void eliminar(InteresProspecto in) throws SQLException {
        daoInteres.deleteInteres(in, sessionUsuario);
        listadoIntereses = daoInteres.findAll();
    }

    public List<InteresProspecto> getListadoIntereses() {
        return listadoIntereses;
    }

    public void setListadoIntereses(List<InteresProspecto> listadoIntereses) {
        this.listadoIntereses = listadoIntereses;
    }

    public List<InteresProspecto> getFilteredIntereses() {
        return filteredIntereses;
    }

    public void setFilteredIntereses(List<InteresProspecto> filteredIntereses) {
        this.filteredIntereses = filteredIntereses;
    }

    public InteresProspecto getInteres() {
        return interes;
    }

    public void setInteres(InteresProspecto interes) {
        this.interes = interes;
    }

    public InteresDAO getDaoInteres() {
        return daoInteres;
    }

    public void setDaoInteres(InteresDAO daoInteres) {
        this.daoInteres = daoInteres;
    }

}
