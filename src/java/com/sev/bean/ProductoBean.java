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
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import util.Facesmethods;

/**
 *
 * Universidad Politécnica Salesiana
 *
 * @author Axel Latorre, Jorge Castañeda Tutor: Ing. Vanessa Jurado
 *
 */
@ManagedBean
@ViewScoped
public class ProductoBean implements Serializable {

    private List<Producto> listadoProductos = new ArrayList<>();
    private List<Producto> filteredProductos;
    private Usuario sessionUsuario;
    private Producto producto = new Producto();
    private ProductoDAO daoProducto = new ProductoDAO();
    private Facesmethods fcm = new Facesmethods();

    public void authorized() {
    }

    public ProductoBean() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            listadoProductos = daoProducto.findAll();
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }
    }

    public void showEditDialog(Producto pro) {
        producto = pro;
    }

    public void onCancelDialog() {
        setProducto(new Producto());
    }

    public void commitEdit() throws SQLException {
        daoProducto.editProducto(producto, sessionUsuario);
        listadoProductos = daoProducto.findAll();
    }

    public void commitCreate() throws SQLException {
        daoProducto.createProducto(producto, sessionUsuario);
        listadoProductos = daoProducto.findAll();
    }

    public void eliminar(Producto pro) throws SQLException {
        daoProducto.deleteProducto(pro, sessionUsuario);
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
