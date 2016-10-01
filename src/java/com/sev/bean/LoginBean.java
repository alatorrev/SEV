/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.RecursoDAO;
import com.sev.dao.RolDAO;
import com.sev.dao.UsuarioDAO;
import com.sev.entity.Recurso;
import com.sev.entity.Rol;
import com.sev.entity.Usuario;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private Usuario sessionUsuario = new Usuario();
    private RolDAO daorol = new RolDAO();
    private List<Rol> listaRoles = new ArrayList<>();
    private String email;
    private int idRolSelected=0;
    private MenuModel modelMenu;
    private List<String> subMenuList = new ArrayList<>();
    private String contrasena;

    public LoginBean() {
        listaRoles=daorol.findAll();
    }

    public String authenticate() throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        setSessionUsuario(usuarioDAO.loginAction(email, contrasena, sessionUsuario,idRolSelected));
        if (sessionUsuario != null) {
            if (getSessionUsuario().getEstadoClave() == 1) {
                initMenu(getSessionUsuario());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuario", sessionUsuario);
                return "nuevo";

            }
            if (getSessionUsuario().getEstadoClave() == 0) {
                initMenu(getSessionUsuario());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuario", sessionUsuario);
                if (sessionUsuario.getDescripcionRol().equals("SUPERADMIN") || sessionUsuario.getDescripcionRol().equals("SUPERVISOR")){
                    return "dashboard";    
                }else if (sessionUsuario.getDescripcionRol().equals("EJECUTIVO")){
                    return "agenda";
                }else {
                    return "otros";
                }
                
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Usuario o Contraseña incorrecto"));
            RequestContext.getCurrentInstance().update("growl");
            return "incorrecto";
        }
        return null;
    }

    public void initMenu(Usuario u) throws SQLException {
        modelMenu = new DefaultMenuModel();
        String urlBase = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("Url");
        RecursoDAO daoRecurso = new RecursoDAO();
        List<Recurso> listaRecursos = daoRecurso.findAllRecursoByRol(u);
        DefaultSubMenu subItemObj = null;
        DefaultMenuItem itemObj;
        DefaultSubMenu temp = new DefaultSubMenu(), aux = new DefaultSubMenu("init");
        for (Recurso objRecurso : listaRecursos) {
            boolean flagExist = subMenuExists(objRecurso.getSubItemLabel());
            if (objRecurso.getSubItemLabel().toUpperCase().equals("NULO")) {
                itemObj = new DefaultMenuItem(objRecurso.getItemLabel());
                itemObj.setUrl(urlBase + objRecurso.getRuta());
                itemObj.setIcon(objRecurso.getItemIcon());
                modelMenu.addElement(itemObj);
            } else {
                if (!flagExist) {
                    subItemObj = new DefaultSubMenu(objRecurso.getSubItemLabel());
                    itemObj = new DefaultMenuItem(objRecurso.getItemLabel());
                    itemObj.setUrl(urlBase + objRecurso.getRuta());
                    itemObj.setIcon(objRecurso.getItemIcon());
                    subItemObj.addElement(itemObj);
                    subItemObj.setIcon(objRecurso.getSubItemIcon());
                    temp = subItemObj;
                    if (!aux.getLabel().equals(subItemObj.getLabel())) {
                        modelMenu.addElement(temp);
                    }
                } else {
                    itemObj = new DefaultMenuItem(objRecurso.getItemLabel());
                    itemObj.setUrl(urlBase + objRecurso.getRuta());
                    itemObj.setIcon(objRecurso.getItemIcon());
                    temp.addElement(itemObj);
                    //temp.setIcon(objRecurso.getSubItemIcon());
                    aux = temp;
                }
            }
        }
    }

    public boolean subMenuExists(String subItem) {
        if (subMenuList.contains(subItem)) {
            return true;
        } else {
            subMenuList.add(subItem);
            return false;
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

    public MenuModel getModelMenu() {
        return modelMenu;
    }

    public void setModelMenu(MenuModel modelMenu) {
        this.modelMenu = modelMenu;
    }

    public List<Rol> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<Rol> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public int getIdRolSelected() {
        return idRolSelected;
    }

    public void setIdRolSelected(int idRolSelected) {
        this.idRolSelected = idRolSelected;
    }

}
