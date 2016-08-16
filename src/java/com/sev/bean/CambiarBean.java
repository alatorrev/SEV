/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;
import com.sev.dao.CambiarDAO;
import com.sev.entity.Usuario;
import com.sev.entity.CambiarContrasena;
import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
/**
 *
 * @author usuario1
 */

@ManagedBean
@ViewScoped
public class CambiarBean implements Serializable{
    private CambiarContrasena cambiar = new CambiarContrasena();
    private CambiarDAO daoCambiar = new CambiarDAO();
    private Usuario u = new Usuario();
    

    public CambiarBean() {
        u = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
    }
    
    
    
    public void commitEdit() throws SQLException {
        
        daoCambiar.editCambiar(cambiar,u);
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
