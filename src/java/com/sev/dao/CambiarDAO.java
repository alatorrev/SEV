/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;
import com.sev.entity.ReestablecerContra;
import com.sev.entity.Usuario;
import com.sev.entity.CambiarContrasena;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sqlserver.Conexion;
/**
 */
/**
 *
 * @author usuario1
 */
public class CambiarDAO implements Serializable{
    
    public void editCambiar(CambiarContrasena ca) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update usuario set clave=?"
                + " where cedula=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, ca.getPassword());
            
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO REESTABLECER: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
}
