/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import com.sev.dao.ProspectoDAO;
import com.sev.entity.CanalCaptacion;
import com.sev.entity.FileUploaded;
import com.sev.entity.Prospecto;
import com.sev.entity.Usuario;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
@ManagedBean
@ViewScoped
public class CargarProspectoBean implements Serializable {

    private StreamedContent downloadableFile;
    private Usuario sessionUsuario;
    private final List<FileUploaded> uploadedFilesList = new ArrayList<>();
    private List<Prospecto> listadoProspecto = new ArrayList<>();
    private List<Prospecto> failedProspectoList = new ArrayList<>();
    private List<Prospecto> selectedProspectos;
    private List<Prospecto> filteredProspecto;
    private Prospecto prospecto = new Prospecto();
    private ProspectoDAO daoProspecto = new ProspectoDAO();
    private int idCanalSelected, cantidadProspectosRepetidos = 0;
    private List<CanalCaptacion> selectorCanal = new ArrayList<>();

    public CargarProspectoBean(){
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            if (sessionUsuario == null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("Usuario");
                String url = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("SesionExpirada");
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            } else {
                /**
                 * se ejecutan las lineas del constructor**
                 */
                InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/matriz/prospectos.xlsx");
                downloadableFile = new DefaultStreamedContent(stream, "application/xlsx", "matriz_prospectos.xlsx");
            }
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (event.getFile().equals(null)) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File is null", null));
        }
        try {
            uploadedFilesList.add(new FileUploaded(
                    getExtension(event.getFile().getFileName()),
                    event.getFile().getInputstream()));
        } catch (IOException e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error reading file" + e, null));
        }
    }

//    public List<Prospecto> verifyFromDatabase(List<Prospecto> listadoXLS) {
//        List<Prospecto> lista = new ArrayList<>();
//        for (Prospecto p : listadoXLS) {
//            for (Prospecto db : listadoProspectoDB) {
//                if (p.getCedula().equals(db.getCedula())) {
//                    p.setRepeated("repetido");
//                    break;
//                }
//            }
//            lista.add(p);
//        }
//        return lista;
//    }
    public List<Prospecto> verifyFromExcel(List<Prospecto> listadoXLS) {
        int secuencialProspecto = getListadoProspecto().size();
        List<Prospecto> lista = getListadoProspecto();
        for (Prospecto p : listadoXLS) {
            secuencialProspecto++;
            p.setSecuencial(secuencialProspecto);
            if (lista.contains(p)) {
                p.setRepeated("repetido");
                cantidadProspectosRepetidos++;
                lista.add(p);
            } else {
                lista.add(p);
            }
        }
        return lista;
    }

    public void clearFailedList() {
        failedProspectoList.clear();
        listadoProspecto.clear();
        cantidadProspectosRepetidos = 0;
    }

    public void guardarListadoProspecto() throws SQLException {
        ProspectoDAO dao = new ProspectoDAO();
        for (Prospecto prospecto : getListadoProspecto()) {
            int flag = dao.guardarProspecto(prospecto);
            if (flag != 0) {
                failedProspectoList.add(prospecto);
            }
        }
    }

    public void deleteRows() {
        Iterator iterador = listadoProspecto.iterator();
        System.out.println(selectedProspectos.size());
        if (!selectedProspectos.isEmpty()) {
            for (Prospecto p : selectedProspectos) {
                while (iterador.hasNext()) {
                    Prospecto lsp = (Prospecto) iterador.next();
                    if (lsp.getCedula().equals(p.getCedula()) && lsp.getSecuencial() == p.getSecuencial()) {
                        iterador.remove();
                        if (p.getRepeated().equals("repetido")) {
                            cantidadProspectosRepetidos--;
                        }
                    }
                }
                iterador = listadoProspecto.iterator();
            }
            Iterator iterador2 = listadoProspecto.iterator();
            int contador = 0;
            while (iterador2.hasNext()) {
                contador++;
                Prospecto piterado = (Prospecto) iterador2.next();
                piterado.setSecuencial(contador);
                int indice = contador - 1;
                listadoProspecto.set(indice, piterado);
            }
            RequestContext.getCurrentInstance().update("frm:prospectoTable");
        }
    }

    public void downloadMatriz() {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/matriz/prospectos.xlsx");
        downloadableFile = new DefaultStreamedContent(stream, "application/xlsx", "matriz_prospectos.xlsx");
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

    public void uploadFileList() {
        Workbook workbook = null;
        for (FileUploaded fu : uploadedFilesList) {
            try {
                switch (fu.getExtension()) {
                    case "xls":
                        workbook = new HSSFWorkbook(fu.getFileInputStream());
                        break;
                    case "xlsx":
                        workbook = new XSSFWorkbook(fu.getFileInputStream());
                        break;
                }
                setListadoProspecto(verifyFromExcel(importData(workbook, 0)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (cantidadProspectosRepetidos != 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            String texto = "El listado cargado contiene " + cantidadProspectosRepetidos + " elementos repetidos. Po favor verifica.";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", texto));
        }
        uploadedFilesList.clear();
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
            if (!"".equals(prospecto.getCedula())) {
                lista.add(prospecto);
            }
        }

        return lista;
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

    public StreamedContent getDownloadableFile() {
        return downloadableFile;
    }

    public void setDownloadableFile(StreamedContent downloadableFile) {
        this.downloadableFile = downloadableFile;
    }

    public List<Prospecto> getSelectedProspectos() {
        return selectedProspectos;
    }

    public void setSelectedProspectos(List<Prospecto> selectedProspectos) {
        this.selectedProspectos = selectedProspectos;
    }

    public List<Prospecto> getFailedProspectoList() {
        return failedProspectoList;
    }

    public void setFailedProspectoList(List<Prospecto> failedProspectoList) {
        this.failedProspectoList = failedProspectoList;
    }

}
