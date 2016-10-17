/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.CambiarDAO;
import com.sev.entity.Usuario;
import com.sev.entity.CambiarContrasena;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * Universidad Politécnica Salesiana
 *
 * @author Axel Latorre, Jorge Castañeda Tutor: Ing. Vanessa Jurado
 *
 */
@ManagedBean
@ViewScoped
public class CambiarBean implements Serializable {

    private CambiarContrasena cambiar = new CambiarContrasena();
    private Usuario sessionUsuario;
    private CambiarDAO daoCambiar = new CambiarDAO();
    private Usuario u = new Usuario();

    public void authorized() {
    }

    public CambiarBean() {
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
            }
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }
    }

    public String cambioClave() throws SQLException, IOException {
        if (cambiar.getNewPassword().equals(cambiar.getConfirmpassword())) {
            if (daoCambiar.editCambiar(cambiar, sessionUsuario)) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Datos actualizados"));
                cambiar = new CambiarContrasena();
                CerrarSesionBean invalidate = new CerrarSesionBean();
                invalidate.logout();
                return "Inicio";
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Datos no actualizados:contraseña incorrecta"));
                return "Incorrecto";
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contraseñas deben coincidir"));
            return "Incorrecto";
        }
    }

    public CambiarContrasena getCambiar() {
        return cambiar;
    }

    public void setCambiar(CambiarContrasena cambiar) {
        this.cambiar = cambiar;
    }

    public CambiarDAO getDaoCambiar() {
        return daoCambiar;
    }

    public void setDaoCambiar(CambiarDAO daoCambiar) {
        this.daoCambiar = daoCambiar;
    }

}
