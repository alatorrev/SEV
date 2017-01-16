/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.ProspectoDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import com.sev.dao.UsuarioDAO;
import com.sev.entity.AsignaProspecto;
import com.sev.entity.Usuario;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import util.Facesmethods;

/**
 *
 * Universidad Politécnica Salesiana
 *
 * @author Axel Latorre, Jorge Castañeda Tutor: Ing. Vanessa Jurado
 *
 */
@ManagedBean
@ViewScoped
public class AsignarProspectoBean implements Serializable {

    private UsuarioDAO daoUsuario = new UsuarioDAO();
    private List<Usuario> listaUsuario = daoUsuario.findAllAsigna();
    private String UsuarioIdSelected, radioButtonValue = "masivo";
    private List<AsignaProspecto> listadoProspecto = new ArrayList<>();
    private List<AsignaProspecto> filteredAccess;
    private ProspectoDAO daoProspecto = new ProspectoDAO();
    private Usuario sessionUsuario;
    private Facesmethods fcm = new Facesmethods();

    public void authorized() {
    }

    public AsignarProspectoBean() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            listaUsuario = daoUsuario.findAllAsigna();
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }
    }

    public void obtenerProspectoUsuario() {
        System.out.println(radioButtonValue);
        setListadoProspecto(daoProspecto.prospectoAsignadosbyUsuario(UsuarioIdSelected, radioButtonValue));
    }

    public void asdasdas() {
        getListadoProspecto().clear();
        setUsuarioIdSelected("0");
    }

    public void guardarPermisos() throws SQLException {
        daoProspecto.saveResourcesbyProfile(getListadoProspecto(), UsuarioIdSelected, radioButtonValue, sessionUsuario);
        obtenerProspectoUsuario();
    }

    public UsuarioDAO getDaoUsuario() {
        return daoUsuario;
    }

    public void setDaoUsuario(UsuarioDAO daoUsuario) {
        this.daoUsuario = daoUsuario;
    }

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public String getUsuarioIdSelected() {
        return UsuarioIdSelected;
    }

    public void setUsuarioIdSelected(String UsuarioIdSelected) {
        this.UsuarioIdSelected = UsuarioIdSelected;
    }

    public List<AsignaProspecto> getListadoProspecto() {
        return listadoProspecto;
    }

    public void setListadoProspecto(List<AsignaProspecto> listadoProspecto) {
        this.listadoProspecto = listadoProspecto;
    }

    public List<AsignaProspecto> getFilteredAccess() {
        return filteredAccess;
    }

    public void setFilteredAccess(List<AsignaProspecto> filteredAccess) {
        this.filteredAccess = filteredAccess;
    }

    public ProspectoDAO getDaoProspecto() {
        return daoProspecto;
    }

    public void setDaoProspecto(ProspectoDAO daoProspecto) {
        this.daoProspecto = daoProspecto;
    }

    public String getRadioButtonValue() {
        return radioButtonValue;
    }

    public void setRadioButtonValue(String radioButtonValue) {
        this.radioButtonValue = radioButtonValue;
    }

}
