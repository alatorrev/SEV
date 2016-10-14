/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.CitaDAO;
import com.sev.dao.ProductoDAO;
import com.sev.dao.ProspectoDAO;
import com.sev.dao.UsuarioDAO;
import com.sev.entity.Producto;
import com.sev.entity.Prospecto;
import com.sev.entity.ReporteCitasVentas;
import com.sev.entity.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * Universidad Politécnica Salesiana
 *
 * @author Axel Latorre, Jorge Castañeda Tutor: Ing. Vanessa Jurado
 *
 */
@ManagedBean
@ViewScoped
public class ConsultaCitasBean {

    List<ReporteCitasVentas> listaReporte = new ArrayList<>();
    private Usuario sessionUsuario;
    boolean completado;
    Date desde = new Date(), hasta = new Date();
    List<Producto> listaProducto = new ArrayList<>();
    UsuarioDAO daoUsuario = new UsuarioDAO();
    ProspectoDAO daoProspecto = new ProspectoDAO();
    ProductoDAO daoProducto = new ProductoDAO();
    CitaDAO daoCita= new CitaDAO();
    Usuario usuario = new Usuario();
    Prospecto prospecto = new Prospecto();
    Producto producto = new Producto();
    
    public void authorized() {
    }
    
    public ConsultaCitasBean() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            if (sessionUsuario == null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("Usuario");
                String url = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("SesionExpirada");
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void consultarCitaVentas() throws SQLException{
        listaReporte=daoCita.listaCitasVentas(usuario,prospecto,completado,producto,desde,hasta);
    }
    
    public void limpiar() {
        usuario = new Usuario();
        prospecto = new Prospecto();
        producto= new Producto();
        setCompletado(false);
        listaReporte.clear();
        desde = new Date();
        hasta = new Date();
    }
    
    public List<Usuario> completeUsuario(String pattern) throws SQLException {
        return daoUsuario.completeUsuarioMethod(pattern);
    }

    public void onItemSelectUsuario(SelectEvent event) {
        usuario = (Usuario) event.getObject();
        System.out.println(usuario.getCedula());
    }

    public List<Prospecto> completeProspecto(String pattern) throws SQLException {
        return daoProspecto.completeProspectoMethod(pattern);
    }

    public void onItemSelectProspecto(SelectEvent event) {
        prospecto = (Prospecto) event.getObject();
        System.out.println(prospecto.getCedula());
    }
    
    public List<Producto> completeProducto(String pattern) throws SQLException {
        return daoProducto.completeProductoMethod(pattern);
    }

    public void onItemSelectProducto(SelectEvent event) {
        producto = (Producto) event.getObject();
        System.out.println(producto.getDescripcion());
    }

    public List<ReporteCitasVentas> getListaReporte() {
        return listaReporte;
    }

    public void setListaReporte(List<ReporteCitasVentas> listaReporte) {
        this.listaReporte = listaReporte;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Prospecto getProspecto() {
        return prospecto;
    }

    public void setProspecto(Prospecto prospecto) {
        this.prospecto = prospecto;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
}
