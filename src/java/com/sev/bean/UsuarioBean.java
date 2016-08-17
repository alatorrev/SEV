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
import javax.faces.context.FacesContext;

/**
 *
 * @author alatorre
 */
@ManagedBean
@ViewScoped
public class UsuarioBean implements Serializable {

    private List<Usuario> listadoUsuarios = new ArrayList<>();
    private List<Usuario> filteredUsers;
    private Usuario sessionUsuario;
    private Usuario usuario = new Usuario();
    private UsuarioDAO daoUsuario = new UsuarioDAO();
    private int idRolSeleted;
    private List<Rol> selectorRoles = new ArrayList<>();

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
                RolDAO daoRol = new RolDAO();
                selectorRoles = daoRol.findAll();
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
        setIdRolSeleted(0);
    }

    public void commitEdit() throws SQLException {
        usuario.setIdRol(idRolSeleted);
        daoUsuario.editUsuario(usuario);
        listadoUsuarios = daoUsuario.findAll();
    }

    public void commitCreate() throws SQLException {
        usuario.setIdRol(idRolSeleted);
        usuario.setPassword(usuario.getCedula());
        daoUsuario.createUsuario(usuario);
        listadoUsuarios = daoUsuario.findAll();
        setUsuario(new Usuario());
    }

    public void eliminar(Usuario u) throws SQLException {
        daoUsuario.deleteUsuario(u);
        listadoUsuarios = daoUsuario.findAll();
    }

    public List<Rol> getSelectorRoles() {
        return selectorRoles;
    }

    public void setSelectorRoles(List<Rol> selectorRoles) {
        this.selectorRoles = selectorRoles;
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

    public int getIdRolSeleted() {
        return idRolSeleted;
    }

    public void setIdRolSeleted(int idRolSeleted) {
        this.idRolSeleted = idRolSeleted;
    }

}
