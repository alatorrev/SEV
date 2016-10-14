/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.ContactoDetalleDAO;
import com.sev.dao.InteresDAO;
import com.sev.dao.ProspectoDAO;
import com.sev.dao.UsuarioDAO;
import com.sev.dao.ViaDAO;
import com.sev.entity.InteresProspecto;
import com.sev.entity.Prospecto;
import com.sev.entity.ReporteHistorialContactos;
import com.sev.entity.Usuario;
import com.sev.entity.ViaComunicacion;
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
public class ConsultaHistorialcontactosBean {

    List<ViaComunicacion> listaViaComunicacion = new ArrayList<>();
    List<InteresProspecto> listaInteresProspecto = new ArrayList<>();
    List<ReporteHistorialContactos> listaReporte = new ArrayList<>();
    UsuarioDAO daoUsuario = new UsuarioDAO();
    ProspectoDAO daoProspecto = new ProspectoDAO();
    ViaDAO daoVia = new ViaDAO();
    InteresDAO daoIntetes = new InteresDAO();
    int idViaComunicacion = 0, idInteresProspecto = 0;
    Usuario usuario = new Usuario();
    private Usuario sessionUsuario;
    Date fechaInicio = new Date(), fechaFin = new Date();
    Prospecto prospecto = new Prospecto();

    public void authorized() {
    }

    public ConsultaHistorialcontactosBean() throws SQLException {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            if (sessionUsuario == null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("Usuario");
                String url = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("SesionExpirada");
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            } else {
                listaViaComunicacion = daoVia.findAll();
                listaInteresProspecto = daoIntetes.findAll();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void consultarHistorial() throws SQLException {
        ContactoDetalleDAO daocontactodetalle = new ContactoDetalleDAO();
        listaReporte = daocontactodetalle.listaHistorialContactos(usuario, prospecto, idViaComunicacion, idInteresProspecto, fechaInicio, fechaFin);
    }

    public void limpiar() {
        usuario = new Usuario();
        prospecto = new Prospecto();
        listaReporte.clear();
        idViaComunicacion = 0;
        idInteresProspecto = 0;
        fechaInicio = new Date();
        fechaFin = new Date();
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

    public List<ViaComunicacion> getListaViaComunicacion() {
        return listaViaComunicacion;
    }

    public void setListaViaComunicacion(List<ViaComunicacion> listaViaComunicacion) {
        this.listaViaComunicacion = listaViaComunicacion;
    }

    public List<InteresProspecto> getListaInteresProspecto() {
        return listaInteresProspecto;
    }

    public void setListaInteresProspecto(List<InteresProspecto> listaInteresProspecto) {
        this.listaInteresProspecto = listaInteresProspecto;
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

    public int getIdViaComunicacion() {
        return idViaComunicacion;
    }

    public void setIdViaComunicacion(int idViaComunicacion) {
        this.idViaComunicacion = idViaComunicacion;
    }

    public int getIdInteresProspecto() {
        return idInteresProspecto;
    }

    public void setIdInteresProspecto(int idInteresProspecto) {
        this.idInteresProspecto = idInteresProspecto;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<ReporteHistorialContactos> getListaReporte() {
        return listaReporte;
    }

    public void setListaReporte(List<ReporteHistorialContactos> listaReporte) {
        this.listaReporte = listaReporte;
    }

}
