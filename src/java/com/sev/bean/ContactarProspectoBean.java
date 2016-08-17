/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.InteresDAO;
import com.sev.dao.ProspectoDAO;
import com.sev.entity.InteresProspecto;
import com.sev.entity.Prospecto;
import com.sev.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author a_latorre
 */
@ManagedBean
@ViewScoped
public class ContactarProspectoBean implements Serializable {

    private Usuario sessionUsuario;
    private Prospecto prospecto = new Prospecto();
    private ProspectoDAO daoProspecto = new ProspectoDAO();
    private InteresDAO daoInteres = new InteresDAO();
    private List<InteresProspecto> interesProspectoList = new ArrayList<>();
    private String cedulaProspecto;
    private int idInteresSelelected;

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
                interesProspectoList=daoInteres.findAll();
            }
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
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

    public int getIdInteresSelelected() {
        return idInteresSelelected;
    }

    public void setIdInteresSelelected(int idInteresSelelected) {
        this.idInteresSelelected = idInteresSelelected;
    }

}
