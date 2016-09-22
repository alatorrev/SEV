/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.conexion.Conexion;
import com.sev.entity.Prospecto;
import com.sev.entity.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public int crearContactoDetalle(Usuario u,Prospecto p, int via, int interes, String observacion) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
            
            String sqlUpdateProspecto="update prospecto set idcancap=?,nombres=?,apellidos=?,celular=?,casa=?,correo=?,"
                    + "establecimiento=?, responsable=?, fecha_modif=?,idintpros=?"
                + " where cedula=?";
            pst=con.getConnection().prepareStatement(sqlUpdateProspecto);
            pst.setInt(1, p.getIdcanal());
            pst.setString(2, p.getNombres());
            pst.setString(3, p.getApellidos());
            pst.setString(4, p.getCelular());
            pst.setString(5, p.getCasa());
            pst.setString(6, p.getEmail());
            pst.setString(7, p.getEstablecimientoProveniente());
            pst.setString(8, p.getCaptador());
            pst.setString(9, sdf.format(new Date()));
            pst.setInt(10,p.getIdInteres());
            pst.setString(11, p.getCedula());
            pst.executeUpdate();
            con.getConnection().commit();
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("detallecontacto", "insert", u);
            daoBitacora.crearRegistro("prospecto", "Edit", u);
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
