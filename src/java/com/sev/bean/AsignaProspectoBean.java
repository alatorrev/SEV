/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.AsignaProspectoDAO;
import com.sev.dao.CanalDAO;
import com.sev.dao.UsuarioDAO;
import com.sev.entity.AsignaProspecto;
import com.sev.entity.CanalCaptacion;
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
public class AsignaProspectoBean implements Serializable{
    private List<AsignaProspecto> listadoProspecto = new ArrayList<>();
    private List<AsignaProspecto> filteredProspecto;
    
    private List<AsignaProspecto> listadoProspectosSin = new ArrayList<>();
    private List<AsignaProspecto> filteredProspectoSin;
    
    private AsignaProspecto Aprospecto = new AsignaProspecto();
    private AsignaProspectoDAO daoAsignaProspecto = new AsignaProspectoDAO();
    
    private List<Usuario> listadoUsuarios = new ArrayList<>();
    private List<Usuario> filteredUsers;
    private Usuario usuario = new Usuario();
    private UsuarioDAO daoUsuario = new UsuarioDAO();
    
    private int idCanalSelected;
    private String CedulaSelectedPros;
    private String CedulaSelectedUsua;
    private List<CanalCaptacion> selectorCanal = new ArrayList<>();
    
    public AsignaProspectoBean() throws SQLException {
        CanalDAO daoCanal = new CanalDAO();
        selectorCanal=daoCanal.findAll();
        listadoProspecto = daoAsignaProspecto.findAll();
        listadoUsuarios = daoUsuario.findAll();
        listadoProspectosSin = daoAsignaProspecto.findAllSin();
    }
    
     public void commitCreate() throws SQLException {
        Aprospecto.setCedula(CedulaSelectedPros);
        usuario.setCedula(CedulaSelectedUsua);
        daoAsignaProspecto.asignarProspecto(Aprospecto, usuario);
        listadoProspecto=daoAsignaProspecto.findAll();
        listadoProspectosSin=daoAsignaProspecto.findAllSin();
    }

    public void showEditDialog(AsignaProspecto p) {
        Aprospecto = p;
    }

    public void commitEdit() throws SQLException {
        Aprospecto.setCedula(CedulaSelectedPros);
        usuario.setCedula(CedulaSelectedUsua);
        daoAsignaProspecto.editAsignacion(Aprospecto, usuario);
        listadoProspecto=daoAsignaProspecto.findAll();
    }
     
    public List<AsignaProspecto> getListadoProspecto() {
        return listadoProspecto;
    }

    public void setListadoProspecto(List<AsignaProspecto> listadoProspecto) {
        this.listadoProspecto = listadoProspecto;
    }

    public List<AsignaProspecto> getFilteredProspecto() {
        return filteredProspecto;
    }

    public void setFilteredProspecto(List<AsignaProspecto> filteredProspecto) {
        this.filteredProspecto = filteredProspecto;
    }

    public AsignaProspecto getAprospecto() {
        return Aprospecto;
    }

    public void setAprospecto(AsignaProspecto Aprospecto) {
        this.Aprospecto = Aprospecto;
    }

    public AsignaProspectoDAO getDaoAsignaProspecto() {
        return daoAsignaProspecto;
    }

    public void setDaoAsignaProspecto(AsignaProspectoDAO daoAsignaProspecto) {
        this.daoAsignaProspecto = daoAsignaProspecto;
    }

    public int getIdCanalSelected() {
        return idCanalSelected;
    }

    public void setIdCanalSelected(int idCanalSelected) {
        this.idCanalSelected = idCanalSelected;
    }

    public String getCedulaSelectedPros() {
        return CedulaSelectedPros;
    }

    public void setCedulaSelectedPros(String CedulaSelectedPros) {
        this.CedulaSelectedPros = CedulaSelectedPros;
    }
    
    public List<CanalCaptacion> getSelectorCanal() {
        return selectorCanal;
    }

    public void setSelectorCanal(List<CanalCaptacion> selectorCanal) {
        this.selectorCanal = selectorCanal;
    }

    public List<Usuario> getListadoUsuarios() {
        return listadoUsuarios;
    }

    public void setListadoUsuarios(List<Usuario> listadoUsuarios) {
        this.listadoUsuarios = listadoUsuarios;
    }

    public List<Usuario> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<Usuario> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UsuarioDAO getDaoUsuario() {
        return daoUsuario;
    }

    public void setDaoUsuario(UsuarioDAO daoUsuario) {
        this.daoUsuario = daoUsuario;
    }

    public String getCedulaSelectedUsua() {
        return CedulaSelectedUsua;
    }

    public void setCedulaSelectedUsua(String CedulaSelectedUsua) {
        this.CedulaSelectedUsua = CedulaSelectedUsua;
    }

    public List<AsignaProspecto> getListadoProspectosSin() {
        return listadoProspectosSin;
    }

    public void setListadoProspectosSin(List<AsignaProspecto> listadoProspectosSin) {
        this.listadoProspectosSin = listadoProspectosSin;
    }

    public List<AsignaProspecto> getFilteredProspectoSin() {
        return filteredProspectoSin;
    }

    public void setFilteredProspectoSin(List<AsignaProspecto> filteredProspectoSin) {
        this.filteredProspectoSin = filteredProspectoSin;
    }

    
    
    
}
