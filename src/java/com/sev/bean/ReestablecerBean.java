/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.ReestablecerDAO;
import com.sev.dao.RolDAO;
import com.sev.dao.UsuarioDAO;
import com.sev.entity.Rol;
import com.sev.entity.Usuario;
import com.sev.entity.ReestablecerContra;
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
public class ReestablecerBean implements Serializable {
    private List<ReestablecerContra> listadoUsuarios = new ArrayList<>();
    private List<ReestablecerContra> filteredUsers;
    private ReestablecerContra reestablecer = new ReestablecerContra();
    private ReestablecerDAO daoReestablecer = new ReestablecerDAO();

    public ReestablecerBean() throws SQLException {
        listadoUsuarios = daoReestablecer.findAll();
        
    }

    public void showEditDialog(ReestablecerContra r) {
        reestablecer = r;
    }

    public void onCancelDialog() {
        setUsuario(new ReestablecerContra());
    }

    public void commitEdit() throws SQLException {
        daoReestablecer.editContra(reestablecer);
        listadoUsuarios=daoReestablecer.findAll();
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
