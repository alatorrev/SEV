/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.entity.Usuario;
import com.sev.entity.CambiarContrasena;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.sev.conexion.Conexion;
import java.sql.ResultSet;

/**
 *
 * Universidad Politécnica Salesiana
 *
 * @author Axel Latorre, Jorge Castañeda Tutor: Ing. Vanessa Jurado
 *
 */
public class CambiarDAO implements Serializable {

    public boolean editCambiar(CambiarContrasena ca, Usuario u) throws SQLException {
        boolean flag;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        ResultSet rs;
        PreparedStatement pst;
        String sqlVerifica = "select * from usuario where cedula=? and clave=?";
        pst = con.getConnection().prepareStatement(sqlVerifica, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        try {
            pst.setString(1, u.getCedula());
            pst.setString(2, ca.getActualPassword());
            rs = pst.executeQuery();
            rs.last();
            int numReg = rs.getRow();
            if (numReg <= 0) {
                flag = false;
            } else {
                String sqlModifica = "update usuario set clave=?, estadoclave=?"
                        + " where cedula=? ";
                pst = con.getConnection().prepareStatement(sqlModifica);
                pst.setString(1, ca.getConfirmpassword());
                pst.setInt(2, 0);
                pst.setString(3, u.getCedula());

                pst.executeUpdate();
                BitacoraDAO daoBitacora = new BitacoraDAO();
                daoBitacora.crearRegistro("usuario", "Cambia clave", u);
                con.getConnection().commit();
                flag = true;
            }

        } catch (Exception e) {
            System.out.println("DAO REESTABLECER: " + e.getMessage());
            con.getConnection().rollback();
            flag = false;
        } finally {
            con.desconectar();
        }
        return flag;
    }

}
