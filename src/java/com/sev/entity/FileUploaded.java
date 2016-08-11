/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.entity;

import java.io.InputStream;
import java.io.Serializable;

/**
 *
 * @author a_latorre
 */
public class FileUploaded implements Serializable {
    private String extension;
    private InputStream fileInputStream;

    public FileUploaded(String extension, InputStream fileInputStream) {
        this.extension = extension;
        this.fileInputStream = fileInputStream;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }
    
    
}
