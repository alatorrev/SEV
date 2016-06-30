/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.RolDAO;
import com.sev.dao.UsuarioDAO;
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
public class RolBean implements Serializable {
    
    private List<Rol> listadoRoles = new ArrayList<>();
    private List<Rol> filteredRols;
    private Rol rol = new Rol();
    private RolDAO daoRol = new RolDAO();
    
    public RolBean() throws SQLException {
        RolDAO daoRol = new RolDAO();
        listadoRoles = daoRol.findAll();
        
    }

    public void showEditDialog(Rol r) {
        rol = r;
    }

    public void onCancelDialog() {
        setRol(new Rol());
    }

    public void commitEdit() throws SQLException {
        daoRol.editRol(rol);
        listadoRoles=daoRol.findAll();
    }

    public void commitCreate() throws SQLException {
        daoRol.createRol(rol);
        listadoRoles=daoRol.findAll();
    }

    public void eliminar(Rol r)throws SQLException {
        daoRol.deleteRol(r);
        listadoRoles=daoRol.findAll();
    }

    public List<Rol> getListadoRoles() {
        return listadoRoles;
    }

    public void setListadoRoles(List<Rol> listadoRoles) {
        this.listadoRoles = listadoRoles;
    }

    public List<Rol> getFilteredRols() {
        return filteredRols;
    }

    public void setFilteredRols(List<Rol> filteredRols) {
        this.filteredRols = filteredRols;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    
}
