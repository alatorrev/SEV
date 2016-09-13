/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.ProductoDAO;
import com.sev.entity.Producto;
import com.sev.entity.Usuario;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.text.SimpleDateFormat;

/**
 *
 * @author usuario1
 */

@ManagedBean
@ViewScoped
public class ProductoBean implements Serializable {
    
    private List<Producto> listadoProductos = new ArrayList<>();
    private List<Producto> filteredProductos;
    private Usuario sessionUsuario;
    private Producto producto = new Producto();
    private ProductoDAO daoProducto = new ProductoDAO();
    
    
    public ProductoBean(){
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
                listadoProductos = daoProducto.findAll();
            }
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }
    }
    
//    public java.sql.Date sqlDate(java.util.Date calendarDate) 
//    {return new java.sql.Date(calendarDate.getTime());}
    
    
        public void showEditDialog(Producto pro) {
        producto = pro;
    }

    public void onCancelDialog() {
        setProducto(new Producto());
    }

    public void commitEdit() throws SQLException {
        daoProducto.editProducto(producto);
        listadoProductos = daoProducto.findAll();
    }

    public void commitCreate() throws SQLException {
        daoProducto.createProducto(producto);
        listadoProductos = daoProducto.findAll();
    }

    public void eliminar(Producto pro) throws SQLException {
        daoProducto.deleteProducto(pro);
        listadoProductos = daoProducto.findAll();
    }

    public List<Producto> getListadoProductos() {
        return listadoProductos;
    }

    public void setListadoProductos(List<Producto> listadoProductos) {
        this.listadoProductos = listadoProductos;
    }

    public List<Producto> getFilteredProductos() {
        return filteredProductos;
    }

    public void setFilteredProductos(List<Producto> filteredProductos) {
        this.filteredProductos = filteredProductos;
    }

    public Usuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(Usuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ProductoDAO getDaoProducto() {
        return daoProducto;
    }

    public void setDaoProducto(ProductoDAO daoProducto) {
        this.daoProducto = daoProducto;
    }
    
    
}
