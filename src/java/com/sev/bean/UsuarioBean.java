/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.AsignaRolDAO;
import com.sev.dao.RolDAO;
import com.sev.dao.UsuarioDAO;
import com.sev.entity.AsignaRol;
import com.sev.entity.Rol;
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
public class UsuarioBean implements Serializable {

    private List<Usuario> listadoUsuarios = new ArrayList<>();
    private List<Usuario> filteredUsers;
    private Usuario sessionUsuario;
    private Usuario usuario = new Usuario();
    private UsuarioDAO daoUsuario = new UsuarioDAO();
    private List<AsignaRol> listadoPermisos = new ArrayList<>();
    private List<AsignaRol> filteredAccess;
    private AsignaRolDAO daoAsignaRol = new AsignaRolDAO();

    public void authorized() {
    }

    public UsuarioBean() {
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
                listadoUsuarios = daoUsuario.findAll();
            }
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }
    }

    public void showEditDialog(Usuario u) {
        usuario = u;
    }

    public void onCancelDialog() {
        setUsuario(new Usuario());
    }

    public void commitEdit() throws SQLException {
        daoUsuario.editUsuario(usuario, sessionUsuario);
        listadoUsuarios = daoUsuario.findAll();
        setUsuario(new Usuario());
    }

    public void commitCreate() throws SQLException {
        usuario.setPassword(usuario.getCedula());
        daoUsuario.createUsuario(usuario,sessionUsuario);
        listadoUsuarios = daoUsuario.findAll();
        setUsuario(new Usuario());
    }

    public void eliminar(Usuario u) throws SQLException {
        daoUsuario.deleteUsuario(u);
        listadoUsuarios = daoUsuario.findAll();
    }

    public void showProfilesDialog(Usuario u) {
        usuario = u;
        setListadoPermisos(daoAsignaRol.rolesAsignadosbyUsuario(usuario));
    }

    public void commitProfiles() throws SQLException {
        daoAsignaRol.saveProfilesbyUsuario(getListadoPermisos(), usuario, sessionUsuario);
        setListadoPermisos(daoAsignaRol.rolesAsignadosbyUsuario(usuario));
        listadoUsuarios = daoUsuario.findAll();
    }

    public List<Usuario> getListadoUsuarios() {
        return listadoUsuarios;
    }

    public void setListadoUsuarios(List<Usuario> listadoUsuarios) {
        this.listadoUsuarios = listadoUsuarios;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<Usuario> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public List<AsignaRol> getListadoPermisos() {
        return listadoPermisos;
    }

    public void setListadoPermisos(List<AsignaRol> listadoPermisos) {
        this.listadoPermisos = listadoPermisos;
    }

    public List<AsignaRol> getFilteredAccess() {
        return filteredAccess;
    }

    public void setFilteredAccess(List<AsignaRol> filteredAccess) {
        this.filteredAccess = filteredAccess;
    }

}
