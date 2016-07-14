/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;
import com.sev.dao.ReestablecerDAO;
import com.sev.dao.RolDAO;
import com.sev.dao.UsuarioDAO;
import com.sev.dao.CambiarDAO;
import com.sev.entity.Rol;
import com.sev.entity.Usuario;
import com.sev.entity.ReestablecerContra;
import com.sev.entity.CambiarContrasena;
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
public class CambiarBean implements Serializable{
    private CambiarContrasena cambiar = new CambiarContrasena();
    private CambiarDAO daoCambiar = new CambiarDAO();
    
    public void commitEdit() throws SQLException {
        daoCambiar.editCambiar(cambiar);
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