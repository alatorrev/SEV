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
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author usuario1
 */
@ManagedBean
@ViewScoped
public class ProspectoBean implements Serializable{
    private List<Prospecto> listadoProspecto = new ArrayList<>();
    private List<Prospecto> filteredProspecto;
    private Prospecto prospecto = new Prospecto();
    private ProspectoDAO daoProspecto = new ProspectoDAO();
    private int idCanalSelected;
    private List<CanalCaptacion> selectorCanal = new ArrayList<>();
    
    
    public ProspectoBean() throws SQLException {
         CanalDAO daoCanal = new CanalDAO();
        selectorCanal=daoCanal.findAll();
        listadoProspecto = daoProspecto.findAll();
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
        listadoProspecto=daoProspecto.findAll();
    }

    public void commitCreate() throws SQLException {
        prospecto.setIdcanal(idCanalSelected);
        daoProspecto.createProspecto(prospecto);
        listadoProspecto=daoProspecto.findAll();
    }

    public void eliminar(Prospecto p)throws SQLException {
        daoProspecto.deleteProspecto(p);
        listadoProspecto=daoProspecto.findAll();
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
