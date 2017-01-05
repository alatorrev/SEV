/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.conexion.Conexion;
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
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
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
    Usuario usuario = new Usuario();
    ViaComunicacion viaComunicacion = new ViaComunicacion();
    InteresProspecto interesProspecto = new InteresProspecto();
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
        nullValidator(usuario, prospecto, viaComunicacion, interesProspecto);
        listaReporte = daocontactodetalle.listaHistorialContactos(usuario, prospecto, viaComunicacion.getIdViaComunicacion(), interesProspecto.getIdInteresProspecto(), fechaInicio, fechaFin);
    }

    public void exportpdf() throws JRException, IOException {
        nullValidator(usuario, prospecto, viaComunicacion, interesProspecto);
        Conexion con = new Conexion();
        SimpleDateFormat sdfParam = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfh = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Map<String, Object> parametros = new HashMap<String, Object>();
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servleContext = (ServletContext) context.getExternalContext().getContext();
        parametros.put("RutaImagenes", servleContext.getRealPath("/Reportes"));
        parametros.put("userName", sessionUsuario.getApellidos()+" "+sessionUsuario.getNombres()+" "+sdfh.format(new Date()));
        parametros.put("idusuario", usuario.getCedula());
        parametros.put("idprospecto", prospecto.getCedula().trim().equals("")?null:prospecto.getCedula());
        
        parametros.put("idvia", viaComunicacion.getIdViaComunicacion()==0?null:viaComunicacion.getIdViaComunicacion());
        parametros.put("viaDescripcion", viaComunicacion.getIdViaComunicacion()==0?null:viaComunicacion.getDescripcion());
        parametros.put("idinteres", interesProspecto.getIdInteresProspecto()==0?null:interesProspecto.getIdInteresProspecto());
        parametros.put("interesDescripcion", interesProspecto.getIdInteresProspecto()==0?null:interesProspecto.getDescripcion());
        
        parametros.put("fechaini", sdfParam.format(fechaInicio));
        parametros.put("fechafin", sdfParam.format(fechaFin));
        
        String dirReporte = servleContext.getRealPath("/Reportes/HistoricoContactos.jasper");
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment;filename=Reporte Historial de Contactos.pdf");
        response.setContentType("application/pdf");

        JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());
        JasperExportManager.exportReportToPdfStream(impres, response.getOutputStream());
        context.responseComplete();
    }

    public void limpiar() {
        usuario = new Usuario();
        prospecto = new Prospecto();
        viaComunicacion = new ViaComunicacion();
        interesProspecto = new InteresProspecto();
        listaReporte.clear();
        fechaInicio = new Date();
        fechaFin = new Date();
    }

    public void nullValidator(Usuario u, Prospecto p, ViaComunicacion via, InteresProspecto interes) {
        if (u == null) {
            usuario = new Usuario();
        }
        if (p == null) {
            prospecto = new Prospecto();
        }
        if (via == null) {
            viaComunicacion = new ViaComunicacion();
        }
        if (interes == null) {
            interesProspecto = new InteresProspecto();
        }
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

    public ViaComunicacion getViaComunicacion() {
        return viaComunicacion;
    }

    public void setViaComunicacion(ViaComunicacion viaComunicacion) {
        this.viaComunicacion = viaComunicacion;
    }

    public InteresProspecto getInteresProspecto() {
        return interesProspecto;
    }

    public void setInteresProspecto(InteresProspecto interesProspecto) {
        this.interesProspecto = interesProspecto;
    }

}
