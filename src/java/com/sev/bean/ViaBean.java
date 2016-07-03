/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;
import com.sev.dao.RolDAO;
import com.sev.dao.UsuarioDAO;
import com.sev.dao.CanalDAO;
import com.sev.dao.ViaDAO;
import com.sev.entity.CanalCaptacion;
import com.sev.entity.Rol;
import com.sev.entity.Usuario;
import com.sev.entity.ViaComunicacion;
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
public class ViaBean implements Serializable{
    private List<ViaComunicacion> listadoVias = new ArrayList<>();
    private List<ViaComunicacion> filteredVias;
    private ViaComunicacion via = new ViaComunicacion();
    private ViaDAO daoVia = new ViaDAO();
    
    public ViaBean() throws SQLException {
        ViaDAO daoVia = new ViaDAO();
        listadoVias = daoVia.findAll();
        
    }

    public void showEditDialog(ViaComunicacion vi) {
        via = vi;
    }

    public void onCancelDialog() {
        setVia(new ViaComunicacion());
    }

    public void commitEdit() throws SQLException {
        daoVia.editVia(via);
        listadoVias=daoVia.findAll();
    }

    public void commitCreate() throws SQLException {
        daoVia.createVia(via);
        listadoVias=daoVia.findAll();
    }

    public void eliminar(ViaComunicacion vi)throws SQLException {
        daoVia.deleteVia(vi);
        listadoVias=daoVia.findAll();
    }

    public List<ViaComunicacion> getListadoVias() {
        return listadoVias;
    }

    public void setListadoVias(List<ViaComunicacion> listadoVias) {
        this.listadoVias = listadoVias;
    }

    public List<ViaComunicacion> getFilteredVias() {
        return filteredVias;
    }

    public void setFilteredVias(List<ViaComunicacion> filteredVias) {
        this.filteredVias = filteredVias;
    }

    public ViaComunicacion getVia() {
        return via;
    }

    public void setVia(ViaComunicacion via) {
        this.via = via;
    }

    public ViaDAO getDaoVia() {
        return daoVia;
    }

    public void setDaoVia(ViaDAO daoVia) {
        this.daoVia = daoVia;
    }

    
    
    
}
