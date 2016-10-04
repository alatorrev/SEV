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
    private BarChartModel barra;
    
    public DashboardBean(){
        listar();
        graficar();
    }
    
    public void listar(){
        DashboardDao dash;
        try{
            dash=new DashboardDao();
            lista = dash.CantidadProspectos();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void graficar(){
        barra = new BarChartModel();
        
        for (int i = 0; i < lista.size(); i++){
            ChartSeries serie = new BarChartSeries();
            serie.setLabel(lista.get(i).getNombres());
            serie.set(lista.get(i).getNombres(), lista.get(i).getCantidad());
            barra.addSeries(serie);
        }
        
//        for (Dashboard dashboard : lista) {
//            ChartSeries serie = new BarChartSeries();
//            serie.setLabel(dashboard.getNombres());
//            serie.set(dashboard.getNombres(), dashboard.getCantidad());
//            barra.addSeries(serie);
//        }
        
        barra.setTitle("Cantidad de prospectos asignados a ejecutivos");
        barra.setLegendPosition("ne");
        
        Axis xAxis = barra.getAxis(AxisType.X);
        xAxis.setLabel("Ejecutivo");
        
        Axis yAxis = barra.getAxis(AxisType.Y);
        yAxis.setLabel("Prospectos");
        yAxis.setMin(0);
        yAxis.setMax(10);
    }

    public List<Dashboard> getLista() {
        return lista;
    }

    public void setLista(List<Dashboard> lista) {
        this.lista = lista;
    }

    public BarChartModel getBarra() {
        return barra;
    }

    public void setBarra(BarChartModel barra) {
        this.barra = barra;
    }
    
    
}
