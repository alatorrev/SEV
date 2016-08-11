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

/**
 *
 * @author usuario1
 */


@ManagedBean
@ViewScoped
public class AsignarProspectoBean  implements Serializable {
     private UsuarioDAO daoUsuario = new UsuarioDAO();
     private List<Usuario> listaUsuario = daoUsuario.findAll();
     private String UsuarioIdSelected;
     private List<AsignaProspecto> listadoProspecto = new ArrayList<>();
     private List<AsignaProspecto> filteredAccess;
     private ProspectoDAO daoProspecto = new ProspectoDAO();
     

    public void obtenerProspectoUsuario() {
        
        setListadoProspecto(daoProspecto.prospectoAsignadosbyUsuario(UsuarioIdSelected));
    }
    
    public void guardarPermisos() throws SQLException{
        daoProspecto.saveResourcesbyProfile(getListadoProspecto(), UsuarioIdSelected);
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
     
    
    
}
