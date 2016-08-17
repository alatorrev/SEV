/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.CanalDAO;
import com.sev.dao.ProspectoDAO;
import com.sev.entity.CanalCaptacion;
import com.sev.entity.Prospecto;
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
 * @author usuario1
 */
@ManagedBean
@ViewScoped
public class ProspectoBean implements Serializable {

    private List<Prospecto> listadoProspecto = new ArrayList<>();
    private List<Prospecto> filteredProspecto;
    private Prospecto prospecto = new Prospecto();
    private ProspectoDAO daoProspecto = new ProspectoDAO();
    private Usuario sessionUsuario;
    private int idCanalSelected;
    private List<CanalCaptacion> selectorCanal = new ArrayList<>();

    public ProspectoBean() {
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
                CanalDAO daoCanal = new CanalDAO();
                selectorCanal = daoCanal.findAll();
                listadoProspecto = daoProspecto.findAll();
            }
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }

    }

    public void showEditDialog(Prospecto p) {
        prospecto = p;
    }

    public void onCancelDialog() {
        setProspecto(new Prospecto());
        setIdCanalSelected(0);
    }

    public void commitEdit() throws SQLException {
        daoProspecto.editProspecto(prospecto);
        listadoProspecto = daoProspecto.findAll();
    }

    public void commitCreate() throws SQLException {
        prospecto.setIdcanal(idCanalSelected);
        daoProspecto.createProspecto(prospecto);
        listadoProspecto = daoProspecto.findAll();
    }

    public void eliminar(Prospecto p) throws SQLException {
        daoProspecto.deleteProspecto(p);
        listadoProspecto = daoProspecto.findAll();
    }

    public Prospecto getProspecto() {
        return prospecto;
    }

    public void setProspecto(Prospecto prospecto) {
        this.prospecto = prospecto;
    }

    public ProspectoDAO getDaoProspecto() {
        return daoProspecto;
    }

    public void setDaoProspecto(ProspectoDAO daoProspecto) {
        this.daoProspecto = daoProspecto;
    }

    public int getIdCanalSelected() {
        return idCanalSelected;
    }

    public void setIdCanalSelected(int idCanalSelected) {
        this.idCanalSelected = idCanalSelected;
    }

    public List<CanalCaptacion> getSelectorCanal() {
        return selectorCanal;
    }

    public void setSelectorCanal(List<CanalCaptacion> selectorCanal) {
        this.selectorCanal = selectorCanal;
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

}
