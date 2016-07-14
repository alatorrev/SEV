/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.CanalDAO;
import com.sev.dao.ProspectoDAO;
import com.sev.entity.CanalCaptacion;
import com.sev.entity.Prospecto;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author HOEM
 */
@ManagedBean
@ViewScoped
public class CargarProspectoBean implements Serializable {

    private UploadedFile file;
    private List<Prospecto> listadoProspecto = new ArrayList<>();
    private List<Prospecto> filteredProspecto;
    private Prospecto prospecto = new Prospecto();
    private ProspectoDAO daoProspecto = new ProspectoDAO();
    private int idCanalSelected;
    private List<CanalCaptacion> selectorCanal = new ArrayList<>();

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (event.getFile().equals(null)) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File is null", null));
        }
        InputStream fileIn = null;
        Workbook workbook = null;
        try {
            String extension = getExtension(event.getFile().getFileName());
            fileIn = event.getFile().getInputstream();
            switch (extension) {
                case "xls":
                    workbook = new HSSFWorkbook(fileIn);
                    break;
                case "xlsx":
                    workbook = new XSSFWorkbook(fileIn);
                    break;
            }
        } catch (IOException e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error reading file" + e, null));
        }
        setListadoProspecto(importData(workbook, 0));
    }
    
//    public void guardarListadoProspecto(){
//        ProspectoDAO dao = new ProspectoDao();
//        for (Prospecto prospecto : getListadoProspecto()) {
//            dao.guardar(prospecto);
//            
//        }
//    }
    
    public List<Prospecto> importData(Workbook workbook, int tabNumber) throws IOException {
        List<Prospecto> lista = new ArrayList<>();
        String[][] data;
        Sheet sheet = workbook.getSheetAt(tabNumber);
        data = new String[sheet.getLastRowNum() + 1][];
        Row[] row = new Row[sheet.getLastRowNum() + 1];
        Cell[][] cell = new Cell[row.length][];

        for (int i = 1; i < row.length; i++) {
            row[i] = sheet.getRow(i);
            cell[i] = new Cell[row[i].getLastCellNum()];
            data[i] = new String[row[i].getLastCellNum()];
            Prospecto prospecto = new Prospecto();
            for (int j = 0; j < cell[i].length; j++) {
                cell[i][j] = row[i].getCell(j);
                if (cell[i][j] != null) {
                    switch (j) {
                        case 0:
                            prospecto.setCanal(getDataFromCell(cell, i, j));
                        case 1:
                            prospecto.setCedula(getDataFromCell(cell, i, j));
                        case 2:
                            prospecto.setNombres(getDataFromCell(cell, i, j));
                        case 3:
                            prospecto.setApellidos(getDataFromCell(cell, i, j));
                        case 4:
                            prospecto.setCelular(getDataFromCell(cell, i, j));
                        case 6:
                            prospecto.setCasa(getDataFromCell(cell, i, j));
                        case 7:
                            prospecto.setEmail(getDataFromCell(cell, i, j));
                        case 8:
                            prospecto.setEstablecimientoProveniente(getDataFromCell(cell, i, j));
                        case 9:
                            prospecto.setCaptador(getDataFromCell(cell, i, j));
                    }
                } else {
                    switch (j) {
                        case 0:
                            prospecto.setCanal("");
                        case 1:
                            prospecto.setCedula("");
                        case 2:
                            prospecto.setNombres("");
                        case 3:
                            prospecto.setApellidos("");
                        case 4:
                            prospecto.setCelular("");
                        case 6:
                            prospecto.setCasa("");
                        case 7:
                            prospecto.setEmail("");
                        case 8:
                            prospecto.setEstablecimientoProveniente("");
                        case 9:
                            prospecto.setCaptador("");
                    }
                }
            }
            lista.add(prospecto);
        }

        return lista;
    }

    public String getDataFromCell(Cell[][] cell, int i, int j) {
        switch (cell[i][j].getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell[i][j]) || HSSFDateUtil.isCellInternalDateFormatted(cell[i][j])) {
                    SimpleDateFormat objFormat = new SimpleDateFormat("dd/MM/yyyy");
                    return objFormat.format(cell[i][j].getDateCellValue());
                } else {
                    Double value = cell[i][j].getNumericCellValue();
                    return value.intValue() + "";
                }
            case Cell.CELL_TYPE_STRING:
                return cell[i][j].getStringCellValue();
        }
        return "";
    }

    public String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<Prospecto> getListadoProspecto() {
        return listadoProspecto;
    }

    public void setListadoProspecto(List<Prospecto> listadoProspecto) {
        this.listadoProspecto = listadoProspecto;
    }

    public List<Prospecto> getFilteredProspecto() {
        return filteredProspecto;
    }

    public void setFilteredProspecto(List<Prospecto> filteredProspecto) {
        this.filteredProspecto = filteredProspecto;
    }
    
     public CargarProspectoBean() throws SQLException {
         CanalDAO daoCanal = new CanalDAO();
        selectorCanal=daoCanal.findAll();
        listadoProspecto = daoProspecto.findAll();
    }
     
     public void showEditDialog(Prospecto p) {
        prospecto = p;
    }

    public void onCancelDialog() {
        setProspecto(new Prospecto());
        setIdCanalSelected(0);
    }

    public void commitEdit() throws SQLException {
        daoProspecto.editProspecto(prospecto);
        listadoProspecto=daoProspecto.findAll();
    }

    public void commitCreate() throws SQLException {
        prospecto.setIdcanal(idCanalSelected);
        daoProspecto.createProspecto(prospecto);
        listadoProspecto=daoProspecto.findAll();
    }

    public void eliminar(Prospecto p)throws SQLException {
        daoProspecto.deleteProspecto(p);
        listadoProspecto=daoProspecto.findAll();
    }

    public Prospecto getProspecto() {
        return prospecto;
    }

    public void setProspecto(Prospecto prospecto) {
        this.prospecto = prospecto;
    }

    public ProspectoDAO getDaoProspecto() {
        return daoProspecto;
    }

    public void setDaoProspecto(ProspectoDAO daoProspecto) {
        this.daoProspecto = daoProspecto;
    }

    public int getIdCanalSelected() {
        return idCanalSelected;
    }

    public void setIdCanalSelected(int idCanalSelected) {
        this.idCanalSelected = idCanalSelected;
    }

    public List<CanalCaptacion> getSelectorCanal() {
        return selectorCanal;
    }

    public void setSelectorCanal(List<CanalCaptacion> selectorCanal) {
        this.selectorCanal = selectorCanal;
    }
     
    
}
