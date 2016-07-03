/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.RolDAO;
import com.sev.dao.UsuarioDAO;
import com.sev.dao.CanalDAO;
import com.sev.entity.CanalCaptacion;
import com.sev.entity.Rol;
import com.sev.entity.Usuario;
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
public class CanalBean implements Serializable{
    private List<CanalCaptacion> listadoCanales = new ArrayList<>();
    private List<CanalCaptacion> filteredCanales;
    private CanalCaptacion canal = new CanalCaptacion();
    private CanalDAO daoCanal = new CanalDAO();
    
    public CanalBean() throws SQLException {
        CanalDAO daoCanal = new CanalDAO();
        listadoCanales = daoCanal.findAll();
        
    }

    public void showEditDialog(CanalCaptacion ca) {
        canal = ca;
    }

    public void onCancelDialog() {
        setCanal(new CanalCaptacion());
    }

    public void commitEdit() throws SQLException {
        daoCanal.editCanal(canal);
        listadoCanales=daoCanal.findAll();
    }

    public void commitCreate() throws SQLException {
        daoCanal.createCanal(canal);
        listadoCanales=daoCanal.findAll();
    }

    public void eliminar(CanalCaptacion ca)throws SQLException {
        daoCanal.deleteCanal(ca);
        listadoCanales=daoCanal.findAll();
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
