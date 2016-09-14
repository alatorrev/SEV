/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.conexion.Conexion;
import com.sev.entity.Usuario;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
public class BitacoraDAO {

    public void crearRegistro(String tabla, String tipotrans, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        try {
            PreparedStatement pstbitacora;
            String querybitacora = "insert into bitacora(TABLA, TIPOTRANS, FECHAREALIZADA, IDUSUARIO) values(?,?,GETDATE(),?)";
            pstbitacora = con.getConnection().prepareStatement(querybitacora);
            pstbitacora.setString(1, tabla);
            pstbitacora.setString(2, tipotrans);
            pstbitacora.setString(3, u.getCedula());
            pstbitacora.executeUpdate();
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO BITACORA: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }
}
