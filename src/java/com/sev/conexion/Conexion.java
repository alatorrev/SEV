/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.conexion;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.faces.context.FacesContext;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
public final class Conexion {

    Connection cn = null;
    String host;
    String databaseName;
    String databaseUser;
    String databasePassword;
    public Conexion() {
        try {
            loadDatabaseConfig(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("environment"));
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            cn = DriverManager.getConnection("jdbc:sqlserver://"+host+":1433;databaseName="+databaseName,databaseUser, databasePassword);

        } catch (SQLException|ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public void loadDatabaseConfig(String environment){
        Properties p = new Properties();
        try {
            InputStream input= FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/"+environment+".properties");
            p.load(input);
            host=p.getProperty("host");
            databaseName=p.getProperty("databaseName");
            databaseUser=p.getProperty("databaseUser");
            databasePassword=p.getProperty("databasePassword");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public static void main(String[] args) throws SQLException {
        Conexion obj = new Conexion();
        Connection cn = obj.getConnection();
        cn.close();
    }

    public Connection getConnection() {
        return cn;
    }

    public void desconectar() throws SQLException {
        cn.close();
    }
}
