/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.conexion.Conexion;
import com.sev.dao.CitaDAO;
import com.sev.dao.ProductoDAO;
import com.sev.dao.ProspectoDAO;
import com.sev.dao.UsuarioDAO;
import com.sev.entity.Producto;
import com.sev.entity.Prospecto;
import com.sev.entity.ReporteCitasVentas;
import com.sev.entity.Usuario;
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
public class ConsultaCitasBean {

    List<ReporteCitasVentas> listaReporte = new ArrayList<>();
    private Usuario sessionUsuario;
    boolean completado;
    Date desde = new Date(), hasta = new Date();
    List<Producto> listaProducto = new ArrayList<>();
    UsuarioDAO daoUsuario = new UsuarioDAO();
    ProspectoDAO daoProspecto = new ProspectoDAO();
    ProductoDAO daoProducto = new ProductoDAO();
    CitaDAO daoCita = new CitaDAO();
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

    public void consultarCitaVentas() throws SQLException {
        nullValidator(usuario, prospecto, producto);
        listaReporte = daoCita.listaCitasVentas(usuario, prospecto, completado, producto, desde, hasta);
    }

    public void exportpdf() throws JRException, IOException {
        nullValidator(usuario, prospecto, producto);
        Conexion con = new Conexion();
        SimpleDateFormat sdfParam = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfh = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Map<String, Object> parametros = new HashMap<String, Object>();
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servleContext = (ServletContext) context.getExternalContext().getContext();
        parametros.put("RutaImagenes", servleContext.getRealPath("/Reportes"));
        parametros.put("idusuario", usuario.getCedula());
        parametros.put("userName", sessionUsuario.getApellidos()+" "+sessionUsuario.getNombres()+" "+sdfh.format(new Date()));
        parametros.put("idprospecto", prospecto.getCedula().trim().equals("")?null:prospecto.getCedula());
        parametros.put("idproducto", producto.getIdprod() == 0 ? null : producto.getIdprod());
        parametros.put("productoDescripcion", producto.getIdprod() == 0 ? null : producto.getDescripcion());
        parametros.put("completado", completado==true?1:0);
        parametros.put("fechaini", sdfParam.format(desde));
        parametros.put("fechafin", sdfParam.format(hasta));

        String dirReporte = servleContext.getRealPath("/Reportes/CitasVentas.jasper");
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment;filename=Reporte de Citas.pdf");
        response.setContentType("application/pdf");

        JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());
        JasperExportManager.exportReportToPdfStream(impres, response.getOutputStream());
        context.responseComplete();
    }

    public void nullValidator(Usuario u, Prospecto p, Producto pro) {
        if (u == null) {
            usuario = new Usuario();
        }
        if (p == null) {
            prospecto = new Prospecto();
        }
        if (pro == null) {
            producto = new Producto();
        }
    }

    public void limpiar() {
        usuario = new Usuario();
        prospecto = new Prospecto();
        producto = new Producto();
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
