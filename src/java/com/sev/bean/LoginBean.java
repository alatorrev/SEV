/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.UsuarioDAO;
import com.sev.entity.Usuario;
import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Axel Latorre, Jorge Castañeda
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private Usuario sessionUsuario = new Usuario();
    private String email;
    private String contrasena;

    public LoginBean() {
    }
    
    public String authenticate() throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        setSessionUsuario(usuarioDAO.loginAction(email, contrasena));
        if (sessionUsuario != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuario", sessionUsuario);
            return "correcto";
        } else {
            String texto = "Credenciales incorrectas";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", texto));
            return "incorrecto";
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Usuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(Usuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

}
