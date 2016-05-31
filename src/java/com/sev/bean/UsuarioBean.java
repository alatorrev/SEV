/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author alatorre
 */
@ManagedBean
@ViewScoped
public class UsuarioBean implements Serializable {
    private List<Usuario> listadoUsuarios = new ArrayList<>();

    public UsuarioBean() {
        for (int i = 0; i < 5; i++) {
            Usuario u = new Usuario();
            u.setNombres("Axel Adrian");
            u.setApellidos("Latorre Villalobos");
            u.setEmail("alatorre@gmail.com");
            u.setRol(1);
            listadoUsuarios.add(u);
        }
    
    }
    
    public void editar(Usuario u){
        
    }
    
    public void eliminar(Usuario u){
        System.out.println("tenemos "+listadoUsuarios.size());
        listadoUsuarios.remove(u);
        System.out.println("ahora tenemos "+listadoUsuarios.size());
    }
    
    
    public List<Usuario> getListadoUsuarios() {
        return listadoUsuarios;
    }

    public void setListadoUsuarios(List<Usuario> listadoUsuarios) {
        this.listadoUsuarios = listadoUsuarios;
    }
    
    
}
