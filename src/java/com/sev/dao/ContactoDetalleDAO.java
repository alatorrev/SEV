/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.conexion.Conexion;
import com.sev.entity.Prospecto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Universidad Politécnica Salesiana
 *
 * @author Axel Latorre, Jorge Castañeda Tutor: Ing. Vanessa Jurado
 *
 */
public class ContactoDetalleDAO {

    public int crearContactoDetalle(Prospecto p, int via, int interes, String observacion) {
        Conexion con = new Conexion();
        ResultSet rs;
        int autoIncrementidContactoDetalle = 0;
        try {
            con.getConnection().setAutoCommit(false);
            String sqlInsert = "insert into DETALLECONTACTO(IDPROSPECTO,IDVIA,IDINTPROS,FECHACONTACTO,OBSERVACION)"
                    + "values(?,?,?,GETDATE(),?)";
            PreparedStatement pst = con.getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, p.getCedula());
            pst.setInt(2, via);
            pst.setInt(3, interes);
            pst.setString(4, observacion);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                autoIncrementidContactoDetalle = rs.getInt(1);
                System.out.println(autoIncrementidContactoDetalle);
            }
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                con.getConnection().rollback();
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(ContactoDetalleDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return autoIncrementidContactoDetalle;
    }
}
