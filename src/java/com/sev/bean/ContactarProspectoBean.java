/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.ContactoDetalleDAO;
import com.sev.dao.InteresDAO;
import com.sev.dao.ProspectoDAO;
import com.sev.dao.ViaDAO;
import com.sev.entity.InteresProspecto;
import com.sev.entity.Prospecto;
import com.sev.entity.Usuario;
import com.sev.entity.ViaComunicacion;
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
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
@ManagedBean
@ViewScoped
public class ContactarProspectoBean implements Serializable {

    private Usuario sessionUsuario;
    private Prospecto prospecto = new Prospecto();
    private ProspectoDAO daoProspecto = new ProspectoDAO();
    private InteresDAO daoInteres = new InteresDAO();
    private ViaDAO viaDao = new ViaDAO();
    private ContactoDetalleDAO daoContactoDetalle = new ContactoDetalleDAO();
    private List<InteresProspecto> interesProspectoList = new ArrayList<>();
    private List<ViaComunicacion> viaComunicacionList = new ArrayList<>();
    private String cedulaProspecto, observaciones;
    private int idInteresSelected, idViaComunicacionSelected, keyGenerated;

    public void authorized() {
    }
    
    public ContactarProspectoBean() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            if (sessionUsuario == null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("Usuario");
                String url = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("SesionExpirada");
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            } else {
                cedulaProspecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cedulaProspecto");
                prospecto = daoProspecto.readProspectoContact(cedulaProspecto, sessionUsuario.getCedula());
                idInteresSelected = prospecto.getIdInteres();
                interesProspectoList = daoInteres.findAll();
                viaComunicacionList = viaDao.findAll();
            }
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }

    }

    public void guardarContactoDetalle() throws SQLException {
        keyGenerated = daoContactoDetalle.crearContactoDetalle(sessionUsuario,prospecto, idViaComunicacionSelected, idInteresSelected, observaciones);
    }
    
    public void declineCitaDialog(){
        try {
            String urlBase = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("Url");
            String url="/faces/workingList.xhtml";
            FacesContext.getCurrentInstance().getExternalContext().redirect(urlBase+url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void verValues() {
        System.out.println(cedulaProspecto);
    }

    public String getCedulaProspecto() {
        return cedulaProspecto;
    }

    public void setCedulaProspecto(String cedulaProspecto) {
        this.cedulaProspecto = cedulaProspecto;
    }

    public Prospecto getProspecto() {
        return prospecto;
    }

    public void setProspecto(Prospecto prospecto) {
        this.prospecto = prospecto;
    }

    public List<InteresProspecto> getInteresProspectoList() {
        return interesProspectoList;
    }

    public void setInteresProspectoList(List<InteresProspecto> interesProspectoList) {
        this.interesProspectoList = interesProspectoList;
    }

    public int getIdInteresSelected() {
        return idInteresSelected;
    }

    public void setIdInteresSelected(int idInteresSelected) {
        this.idInteresSelected = idInteresSelected;
    }

    public int getIdViaComunicacionSelected() {
        return idViaComunicacionSelected;
    }

    public void setIdViaComunicacionSelected(int idViaComunicacionSelected) {
        this.idViaComunicacionSelected = idViaComunicacionSelected;
    }

    public List<ViaComunicacion> getViaComunicacionList() {
        return viaComunicacionList;
    }

    public void setViaComunicacionList(List<ViaComunicacion> viaComunicacionList) {
        this.viaComunicacionList = viaComunicacionList;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getKeyGenerated() {
        return keyGenerated;
    }

    public void setKeyGenerated(int keyGenerated) {
        this.keyGenerated = keyGenerated;
    }

}
