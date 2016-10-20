/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.ChartSeries;
import com.sev.dao.DashboardDao;
import com.sev.entity.Dashboard;
import java.util.ArrayList;

/**
 *
 * @author usuario1
 */
@ManagedBean
@ViewScoped
public class DashboardBean {

    private List<Dashboard> lista = new ArrayList<>();
    private List<Dashboard> lista1 = new ArrayList<>();
    private List<Dashboard> lista2 = new ArrayList<>();
    private BarChartModel barra;
    private BarChartModel barra1;
    private BarChartModel barra2;
    private Dashboard dash = new Dashboard();
    ChartSeries serie = new BarChartSeries();
    ChartSeries serie1 = new BarChartSeries();
    ChartSeries serie2 = new BarChartSeries();

    public DashboardBean() {
        listar();
        graficar();
    }

    public void listar() {
        DashboardDao dash;
        try {
            dash = new DashboardDao();
            lista = dash.CantidadProspectos();
            lista1 = dash.SumaProductos();
            lista2 = dash.NuevosClientes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void graficar() {
        barra = new BarChartModel();
        barra1 = new BarChartModel();
        barra2 = new BarChartModel();
        ChartSeries serie = new BarChartSeries();
        ChartSeries serie1 = new BarChartSeries();
        ChartSeries serie2 = new BarChartSeries();

//        for (int i = 0; i < lista.size(); i++) {
//            serie.set(lista.get(i).getNombres(), lista.get(i).getCantidad());
//            serie.setLabel(lista.get(i).getNombres());
//        }
//        barra.addSeries(serie);

        serie.setLabel("Ejecutivos");
        for (Dashboard dashboard : lista) {
            serie.set(dashboard.getNombres(), dashboard.getCantidad());
        }
        
        barra.addSeries(serie);
        barra.setTitle("Cantidad de prospectos asignados a ejecutivos");
        barra.setLegendPosition("ne");
        barra.setDatatipFormat("%2$d");
        barra.setShowPointLabels(true);
        barra.setAnimate(true);
        

        Axis xAxis = barra.getAxis(AxisType.X);
        xAxis.setLabel("Ejecutivo");

        Axis yAxis = barra.getAxis(AxisType.Y);
        yAxis.setLabel("Prospectos");
        yAxis.setMin(0);
        yAxis.setMax(10);

        /* ------------------------------------------------------------------------- */

        serie1.setLabel("Ejecutivos");
        for (Dashboard dashboard1 : lista1) {
            serie1.set(dashboard1.getNombres(), dashboard1.getSuma());
        }
        
        barra1.addSeries(serie1);
        barra1.setTitle("Ventas realizadas según el ejecutivo en el mes en curso");
        barra1.setLegendPosition("ne");
        barra1.setShowPointLabels(true);
        barra1.setDatatipFormat("$ %2$.2f");
        barra1.setAnimate(true);
        
        
        

        Axis xAxis1 = barra1.getAxis(AxisType.X);
        xAxis1.setLabel("Ejecutivo");

        Axis yAxis1 = barra1.getAxis(AxisType.Y);
        yAxis1.setLabel("Ventas $");
        yAxis1.setMin(0);
        yAxis1.setMax(2500);
        yAxis1.setTickFormat("%.2f");
        
        
        /* --------------------------------------------------------------- */
        serie2.setLabel("Prospectos");
        for (Dashboard dashboard2 : lista2) {
            serie2.set(dashboard2.getFecha(), dashboard2.getCantidad());
        }
        
        barra2.addSeries(serie2);
        barra2.setTitle("Cantidad de prospectos que se han convertido en clientes en el mes actual");
        barra2.setLegendPosition("ne");
        barra2.setShowPointLabels(true);
        barra2.setDatatipFormat("%2$d");
        barra2.setAnimate(true);
        
        Axis xAxis2 = barra2.getAxis(AxisType.X);
        xAxis2.setLabel("Mes");

        Axis yAxis2 = barra2.getAxis(AxisType.Y);
        yAxis2.setLabel("Cantidad");
        yAxis2.setMin(0);
        yAxis2.setMax(30);
        //yAxis2.setTickFormat("%.2f");
        
    }

    public List<Dashboard> getLista() {
        return lista;
    }

    public void setLista(List<Dashboard> lista) {
        this.lista = lista;
    }

    public List<Dashboard> getLista1() {
        return lista1;
    }

    public void setLista1(List<Dashboard> lista1) {
        this.lista1 = lista1;
    }
    
     public List<Dashboard> getLista2() {
        return lista2;
    }

    public void setLista2(List<Dashboard> lista2) {
        this.lista2 = lista2;
    }
    
    public BarChartModel getBarra() {
        return barra;
    }

    public void setBarra(BarChartModel barra) {
        this.barra = barra;
    }

    public BarChartModel getBarra1() {
        return barra1;
    }

    public void setBarra1(BarChartModel barra1) {
        this.barra1 = barra1;
    }

   public BarChartModel getBarra2() {
        return barra2;
    }

    public void setBarra2(BarChartModel barra2) {
        this.barra2 = barra2;
    }

    
}
