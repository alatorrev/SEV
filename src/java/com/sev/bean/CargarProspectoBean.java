/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
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

        String datos[][] = importData(workbook, 0);
        for (int i = 0; i < datos.length; i++) {
            for (int j = 0; j < datos[i].length; j++) {
                System.out.print(datos[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    public String[][] importData(Workbook workbook, int tabNumber) throws IOException {

        String[][] data;

        //Create Workbook from Existing File
        Sheet sheet = workbook.getSheetAt(tabNumber);

        //Define Data & Row Array and adjust from Zero Base Numer
        data = new String[sheet.getLastRowNum() + 1][];
        Row[] row = new Row[sheet.getLastRowNum() + 1];
        Cell[][] cell = new Cell[row.length][];

        //Transfer Cell Data to Local Variable
        for (int i = 0; i < row.length; i++) {
            row[i] = sheet.getRow(i);

            //Note that cell number is not Zero Based
            cell[i] = new Cell[row[i].getLastCellNum()];
            data[i] = new String[row[i].getLastCellNum()];

            for (int j = 0; j < cell[i].length; j++) {
                cell[i][j] = row[i].getCell(j);
                if (cell[i][j] != null) {
                    switch (cell[i][j].getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:

                            if (HSSFDateUtil.isCellDateFormatted(cell[i][j]) || HSSFDateUtil.isCellInternalDateFormatted(cell[i][j])) {
                                SimpleDateFormat objFormat = new SimpleDateFormat("dd/MM/yyyy");
                                objFormat.format(cell[i][j].getDateCellValue());
                            } else {
                                Double value = cell[i][j].getNumericCellValue();
                                data[i][j] = value.intValue() + "";
                            }
                            break;
                        case Cell.CELL_TYPE_STRING:
                            data[i][j] = cell[i][j].getStringCellValue();
                            break;
                    }
                }
            }

        }

        return data;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
