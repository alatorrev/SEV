/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.ProspectoDAO;
import com.sev.entity.CanalCaptacion;
import com.sev.entity.Prospecto;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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
 * @author Axel Latorre, Jorge Castañeda
 */
@ManagedBean
@ViewScoped
public class CargarProspectoBean implements Serializable {

    private UploadedFile file;
    private List<Prospecto> listadoProspecto = new ArrayList<>();
    private List<Prospecto> listadoProspectoDB;
    private List<Prospecto> filteredProspecto;
    private Prospecto prospecto = new Prospecto();
    private ProspectoDAO daoProspecto = new ProspectoDAO();
    private int idCanalSelected;
    private List<CanalCaptacion> selectorCanal = new ArrayList<>();

    public CargarProspectoBean() throws SQLException {
        listadoProspectoDB = daoProspecto.findAll();
    }

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
        //setListadoProspecto(verificarListadoExcelvsDB(importData(workbook, 0)));
        verificarListadoExcel(verificarListadoExcel(importData(workbook, 0)));
    }

    public List<Prospecto> verificarListadoExcelvsDB(List<Prospecto> listadoXLS) {
        List<Prospecto> lista = new ArrayList<>();
        for (Prospecto p : listadoXLS) {
            for (Prospecto db : listadoProspectoDB) {
                if (p.getCedula().equals(db.getCedula())) {
                    p.setRepeated(true);
                    break;
                }

            }
            lista.add(p);
        }
        return lista;
    }

    public List<Prospecto> verificarListadoExcel(List<Prospecto> listadoXLS) {
        List<Prospecto> lista=new ArrayList();
        if (new HashSet<>(listadoXLS).size()!= listadoXLS.size()) {
            System.out.println("REPETIDO");
        }
        return  lista;
    }

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
                            prospecto.setCedula(getDataFromCell(cell, i, j));
                            break;
                        case 1:
                            prospecto.setDescripcionCanal(getDataFromCell(cell, i, j));
                            break;
                        case 2:
                            prospecto.setNombres(getDataFromCell(cell, i, j));
                            break;
                        case 3:
                            prospecto.setApellidos(getDataFromCell(cell, i, j));
                            break;
                        case 4:
                            prospecto.setCelular(getDataFromCell(cell, i, j));
                            break;
                        case 5:
                            prospecto.setCasa(getDataFromCell(cell, i, j));
                            break;
                        case 6:
                            prospecto.setEmail(getDataFromCell(cell, i, j));
                            break;
                        case 7:
                            prospecto.setEstablecimientoProveniente(getDataFromCell(cell, i, j));
                            break;
                        case 8:
                            prospecto.setCaptador(getDataFromCell(cell, i, j));
                            break;
                    }
                } else {
                    switch (j) {
                        case 0:
                            prospecto.setCedula("");
                            break;
                        case 1:
                            prospecto.setDescripcionCanal("");
                            break;
                        case 2:
                            prospecto.setNombres("");
                            break;
                        case 3:
                            prospecto.setApellidos("");
                            break;
                        case 4:
                            prospecto.setCelular("");
                            break;
                        case 6:
                            prospecto.setCasa("");
                            break;
                        case 7:
                            prospecto.setEmail("");
                            break;
                        case 8:
                            prospecto.setEstablecimientoProveniente("");
                            break;
                        case 9:
                            prospecto.setCaptador("");
                            break;
                    }
                }
            }
            lista.add(prospecto);
        }

        return lista;
    }

    public void guardarListadoProspecto() throws SQLException {
        ProspectoDAO dao = new ProspectoDAO();
        for (Prospecto prospecto : getListadoProspecto()) {
            dao.guardarProspecto(prospecto);
        }
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

    public Prospecto getProspecto() {
        return prospecto;
    }

    public void setProspecto(Prospecto prospecto) {
        this.prospecto = prospecto;
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
