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
import com.sev.entity.HorarioCitas;
import com.sev.entity.Producto;
import com.sev.entity.Prospecto;
import com.sev.entity.Usuario;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
public class AgendaBean implements Serializable {

    private Usuario sessionUsuario;
    private int idContactoDetalle = 0;
    private String idProspecto, hioSelected;
    private HorarioCitas horarioCitas = new HorarioCitas();
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
    private List<String> listadoFechasInicial = new ArrayList<>();
    private List<HorarioCitas> listadoFechaFinal = new ArrayList();
    private Facesmethods fcm = new Facesmethods();

    public void authorized() {
    }

    public AgendaBean() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
            idProspecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idprospecto");
            if (id != null) {
                idContactoDetalle = Integer.parseInt(id);
            }
            listaProspectos = daoProspecto.readProspectobyUsuario(sessionUsuario.getCedula());
            buildScheduler();

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

    public void onCitaSelected(SelectEvent e) throws ParseException {
        SimpleDateFormat hf = new SimpleDateFormat("HH:mm");
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);

        ScheduleEvent evento = (ScheduleEvent) e.getObject();
        cita = (Cita) evento.getData();
        listadoFechasInicial = listadoHorasInicio(now.getTime());
        hioSelected = hf.format(cita.getFechaInicio());
        Date temp = new Date();
        temp.setTime(cita.getFechaInicio().getTime());
        listadoFechaFinal = listadoHorasFinalizacion(temp);
        horarioCitas = buscarFechaFinalización(cita.getFechaFin());

    }

    public HorarioCitas buscarFechaFinalización(Date citaEnd) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Iterator iterador = listadoFechaFinal.iterator();
        while (iterador.hasNext()) {
            HorarioCitas hc = (HorarioCitas) iterador.next();
            if (hc.getHora().equals(df.format(citaEnd))) {
                return hc;
            }
        }
        return new HorarioCitas();
    }

    public void onNuevaCita(SelectEvent e) throws ParseException {
        ScheduleEvent evento = new DefaultScheduleEvent("", (Date) e.getObject(), (Date) e.getObject());
        cita = new Cita();
        cita.setFechaInicio(new java.sql.Date(evento.getStartDate().getTime()));
        cita.setFechaFin(new java.sql.Date(evento.getEndDate().getTime()));
        listadoFechasInicial = listadoHorasInicio(new java.sql.Date(evento.getStartDate().getTime()));
        hioSelected = "";
        listadoFechaFinal = listadoHorasFinalizacion(new java.sql.Date(evento.getStartDate().getTime()));
    }

    public void onCitaMoved(ScheduleEntryMoveEvent e) throws SQLException {
        cita = (Cita) e.getScheduleEvent().getData();
        boolean flag = daoCita.editarCita(cita, 0);
        if (flag) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Cita reagendada"));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Lo sentimos, ocurrió un problema"));
        }
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
            cita.setEstado(false);
        }
    }

    public void guardarCita() throws ParseException {
        if (cita.getIdCita() != 0) {
            saveIfEdit();
        } else {
            saveIfNew();
        }
    }

    public void updateEndingTimeHour() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        setListadoFechaFinal(listadoHorasFinalizacion(sdf.parse(df.format(getCita().getFechaInicio()) + " " + hioSelected)));
        setHorarioCitas(getListadoFechaFinal().get(1));
    }

    public void saveIfNew() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        cita.setFechaInicio(sdf.parse(df.format(getCita().getFechaInicio()) + " " + hioSelected));
        cita.setFechaFin(sdf.parse(df.format(getCita().getFechaFin()) + " " + getHorarioCitas().getHora()));
        Calendar calInicio = Calendar.getInstance();
        calInicio.setTime(cita.getFechaInicio());
        Calendar calFin = Calendar.getInstance();
        calFin.setTime(cita.getFechaFin());
        if (calInicio.get(Calendar.DAY_OF_MONTH) == calFin.get(Calendar.DAY_OF_MONTH)) {
            if (cita.getFechaInicio().getTime() < cita.getFechaFin().getTime()) {
                try {
                    if (idProspecto != null) {
                        cita.setIdProspecto(idProspecto);
                        cita.setIdContacto(idContactoDetalle);
                    } else {
                        cita.setIdProspecto(idProspectoSelected);
                        cita.setIdContacto(idContactoDetalle);
                    }
                    boolean flag = daoCita.crearCita(cita, sessionUsuario.getCedula());
                    if (flag) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Cita agendada"));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al crear la cita"));
                    }
                    buildScheduler();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Error al crear la cita"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "La hora inicial debe ser menor a la final"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "La fecha inicial debe ser igual a la final"));
        }
    }

    public void saveIfEdit() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        cita.setFechaInicio(sdf.parse(df.format(getCita().getFechaInicio()) + " " + hioSelected));
        cita.setFechaFin(sdf.parse(df.format(getCita().getFechaFin()) + " " + getHorarioCitas().getHora()));
        Calendar calInicio = Calendar.getInstance();
        calInicio.setTime(cita.getFechaInicio());
        Calendar calFin = Calendar.getInstance();
        calFin.setTime(cita.getFechaFin());
        if (calInicio.get(Calendar.DAY_OF_MONTH) == calFin.get(Calendar.DAY_OF_MONTH)) {
            if (cita.getFechaInicio().getTime() < cita.getFechaFin().getTime()) {
                try {
                    if (cita.getCompletado() && producto.getIdprod() == 0) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Si completa la cita, debe elegir un producto de referencia"));
                    } else {
                        boolean flag = daoCita.editarCita(cita, cita.getCompletado() ? producto.getIdprod() : 0);
                        if (flag) {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Información del cita actualizada"));
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al crear la cita"));
                        }
                        buildScheduler();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Error al crear la cita"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "La hora inicial debe ser menor a la final"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "La fecha inicial debe ser igual a la final"));
        }
    }

    public static List<String> listadoHorasInicio(Date fechareferencia) throws ParseException {
        List<String> listado = new ArrayList<>();
        SimpleDateFormat formatoHoras = new SimpleDateFormat("HH:mm");
        Calendar calInicio = Calendar.getInstance();
        calInicio.setTime(fechareferencia);
        Calendar calStop = Calendar.getInstance();
        calStop.setTimeInMillis(fechareferencia.getTime() + (24 * 3600 * 1000));//sumo 1 día

        while (calInicio.get(Calendar.DAY_OF_MONTH) != calStop.get(Calendar.DAY_OF_MONTH)) {
            listado.add(formatoHoras.format(fechareferencia));
            fechareferencia.setTime(fechareferencia.getTime() + (1000 * 60 * 30));//sumo 30 minutos
            calInicio.setTime(fechareferencia);
        }
        return listado;
    }

    public static List<HorarioCitas> listadoHorasFinalizacion(Date fechareferencia) throws ParseException {
        List<HorarioCitas> listado = new ArrayList<>();
        SimpleDateFormat formatoHoras = new SimpleDateFormat("HH:mm");
        Date temp = new Date();
        temp.setTime(fechareferencia.getTime());
        Calendar calInicio = Calendar.getInstance();
        calInicio.setTime(fechareferencia);
        Calendar calStop = Calendar.getInstance();
        calStop.setTimeInMillis(fechareferencia.getTime() + (24 * 3600 * 1000));//sumo 1 día

        while (calInicio.get(Calendar.DAY_OF_MONTH) != calStop.get(Calendar.DAY_OF_MONTH)) {
            long resultado = fechareferencia.getTime() - temp.getTime();
            HorarioCitas hc = new HorarioCitas();
            hc.setHora(formatoHoras.format(fechareferencia));
            hc.setTiempoEnCita("(" + ((double) (resultado / (double) (1000 * 3600))) + " horas)");
            listado.add(hc);
            fechareferencia.setTime(fechareferencia.getTime() + (1000 * 60 * 30));//sumo 30 minutos
            calInicio.setTime(fechareferencia);
        }
        return listado;
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

    public List<String> getListadoFechasInicial() {
        return listadoFechasInicial;
    }

    public void setListadoFechasInicial(List<String> listadoFechasInicial) {
        this.listadoFechasInicial = listadoFechasInicial;
    }

    public String getHioSelected() {
        return hioSelected;
    }

    public void setHioSelected(String hioSelected) {
        this.hioSelected = hioSelected;
    }

    public HorarioCitas getHorarioCitas() {
        return horarioCitas;
    }

    public void setHorarioCitas(HorarioCitas horarioCitas) {
        this.horarioCitas = horarioCitas;
    }

    public List<HorarioCitas> getListadoFechaFinal() {
        return listadoFechaFinal;
    }

    public void setListadoFechaFinal(List<HorarioCitas> listadoFechaFinal) {
        this.listadoFechaFinal = listadoFechaFinal;
    }

}
