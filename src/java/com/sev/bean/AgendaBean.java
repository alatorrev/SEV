/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.CitaDAO;
import com.sev.dao.ProductoDAO;
import com.sev.dao.ProspectoDAO;
import com.sev.entity.Cita;
import com.sev.entity.Producto;
import com.sev.entity.Prospecto;
import com.sev.entity.Usuario;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * Universidad Politécnica Salesiana
 *
 * @author Axel Latorre, Jorge Castañeda Tutor: Ing. Vanessa Jurado
 *
 */
@ManagedBean
@ViewScoped
public class AgendaBean implements Serializable {

    private Usuario sessionUsuario;
    private int idContactoDetalle = 0;
    private String idProspecto;
    private String idProspectoSelected;
    private Prospecto prospecto = new Prospecto();
    private Producto producto = new Producto();
    private List<Prospecto> listaProspectos = new ArrayList<>();
    private CitaDAO daoCita = new CitaDAO();
    private ProspectoDAO daoProspecto = new ProspectoDAO();
    private ProductoDAO daoProducto = new ProductoDAO();
    private DefaultScheduleModel modelSchedule = new DefaultScheduleModel();
    private Cita cita = new Cita();
    private List<Cita> listadoCitas = new ArrayList<>();
    private List<Producto> listadoProducto = new ArrayList<>();

    public void authorized() {
    }

    public AgendaBean() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            if (sessionUsuario == null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("Usuario");
                String url = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("SesionExpirada");
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            } else {
                String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
                idProspecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idprospecto");
                if (id != null) {
                    idContactoDetalle = Integer.parseInt(id);
                }
                listaProspectos = daoProspecto.readProspectobyUsuario(sessionUsuario.getCedula());
                buildScheduler();
            }
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }
    }

    public final void buildScheduler() throws SQLException {
        modelSchedule.clear();
        listadoCitas = daoCita.findCitaByUsuario(sessionUsuario.getCedula());
        for (Cita obj : listadoCitas) {
            DefaultScheduleEvent evento = new DefaultScheduleEvent();
            evento.setTitle(obj.getTitulo());
            evento.setStartDate(obj.getFechaInicio());
            evento.setEndDate(obj.getFechaFin());
            evento.setData(obj);
            evento.setDescription(obj.getObservacion());
            evento.setAllDay(false);
            evento.setEditable(true);
            modelSchedule.addEvent(evento);
        }
    }

    public void onCitaSelected(SelectEvent e) {
        ScheduleEvent evento = (ScheduleEvent) e.getObject();
        cita = (Cita) evento.getData();
    }

    public void onNuevaCita(SelectEvent e) {
        ScheduleEvent evento = new DefaultScheduleEvent("", (Date) e.getObject(), (Date) e.getObject());
        cita = new Cita();
        cita.setFechaInicio(new java.sql.Date(evento.getStartDate().getTime()));
        cita.setFechaFin(new java.sql.Date(evento.getEndDate().getTime()));
    }

    public void onCitaMoved(ScheduleEntryMoveEvent e) throws SQLException {
        cita = (Cita) e.getScheduleEvent().getData();
        daoCita.editarCita(cita, 0);
        buildScheduler();
    }

    public void onCitaResized(ScheduleEntryResizeEvent e) throws SQLException {
        cita = (Cita) e.getScheduleEvent().getData();
        daoCita.editarCita(cita, 0);
        buildScheduler();
    }

    public void onCitacompletada() throws SQLException {
        if (cita.getCompletado()) {
            listadoProducto = daoProducto.supportedProductbyDate(cita);
        }
    }

    public void guardarCita() {
        if (cita.getIdCita() != 0) {
            try {
                if (cita.getCompletado() && producto.getIdprod() == 0) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Si completa la cita, debe elegir un producto de referencia"));
                } else {
                    daoCita.editarCita(cita, cita.getCompletado() ? producto.getIdprod() : 0);
                    buildScheduler();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Error al crear la cita"));
            }
        } else {
            Calendar calInicio = Calendar.getInstance();
            calInicio.setTime(cita.getFechaInicio());
            Calendar calFin = Calendar.getInstance();
            calFin.setTime(cita.getFechaFin());
            if (calInicio.get(Calendar.DAY_OF_MONTH) == calFin.get(Calendar.DAY_OF_MONTH)) {
                try {
                    if (idProspecto != null) {
                        cita.setIdProspecto(idProspecto);
                        cita.setIdContacto(idContactoDetalle);
                    } else {
                        cita.setIdProspecto(idProspectoSelected);
                        cita.setIdContacto(idContactoDetalle);
                    }
                    daoCita.crearCita(cita, sessionUsuario.getCedula());
                    buildScheduler();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Error al crear la cita"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "La fecha inicial debe ser igual a la final"));
            }
        }
    }

    public int getIdContactoDetalle() {
        return idContactoDetalle;
    }

    public void setIdContactoDetalle(int idContactoDetalle) {
        this.idContactoDetalle = idContactoDetalle;
    }

    public String getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(String idProspecto) {
        this.idProspecto = idProspecto;
    }

    public DefaultScheduleModel getModelSchedule() {
        return modelSchedule;
    }

    public void setModelSchedule(DefaultScheduleModel modelSchedule) {
        this.modelSchedule = modelSchedule;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public List<Cita> getListadoCitas() {
        return listadoCitas;
    }

    public void setListadoCitas(List<Cita> listadoCitas) {
        this.listadoCitas = listadoCitas;
    }

    public List<Prospecto> getListaProspectos() {
        return listaProspectos;
    }

    public void setListaProspectos(List<Prospecto> listaProspectos) {
        this.listaProspectos = listaProspectos;
    }

    public Prospecto getProspecto() {
        return prospecto;
    }

    public void setProspecto(Prospecto prospecto) {
        this.prospecto = prospecto;
    }

    public String getIdProspectoSelected() {
        return idProspectoSelected;
    }

    public void setIdProspectoSelected(String idProspectoSelected) {
        this.idProspectoSelected = idProspectoSelected;
    }

    public Usuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(Usuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public List<Producto> getListadoProducto() {
        return listadoProducto;
    }

    public void setListadoProducto(List<Producto> listadoProducto) {
        this.listadoProducto = listadoProducto;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

}
