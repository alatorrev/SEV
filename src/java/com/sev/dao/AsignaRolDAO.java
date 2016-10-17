/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.conexion.Conexion;
import com.sev.entity.AsignaRol;
import com.sev.entity.Usuario;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Universidad Politécnica Salesiana
 *
 * @author Axel Latorre, Jorge Castañeda Tutor: Ing. Vanessa Jurado
 *
 */
public class AsignaRolDAO implements Serializable {

    public List<AsignaRol> rolesAsignadosbyUsuario(Usuario u) {
        List<AsignaRol> lista = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "SELECT R.IDROL,R.DESCRIPCION,A.ESTADO FROM ROL R LEFT JOIN "
                + "(SELECT UR.IDUSUARIO,UR.IDROL,UR.ESTADO FROM USUARIOROL UR INNER JOIN USUARIO U ON "
                + "UR.IDUSUARIO=U.CEDULA WHERE U.CEDULA=?) A ON A.IDROL=R.IDROL "
                + "where r.ESTADO=1";
        try {
            pst = con.getConnection().prepareStatement(sql);
            pst.setString(1, u.getCedula());
            rs = pst.executeQuery();
            while (rs.next()) {
                AsignaRol ar = new AsignaRol();
                ar.setIdRol(rs.getInt(1));
                ar.setDescripcionRol(rs.getString(2));
                ar.setEstado((rs.getInt(3) != 0));
                lista.add(ar);
            }
        } catch (Exception e) {
            System.out.println("DAO ASIGNAROL: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(AsignaRolDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    public void saveProfilesbyUsuario(List<AsignaRol> listadoAR, Usuario us, Usuario session) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        con.getConnection().setAutoCommit(false);
        String query = "MERGE USUARIOROL AS A "
                + "USING (SELECT ? AS IDUSUARIO, ? AS IDROL,? AS ESTADO) AS T "
                + "ON(A.IDUSUARIO=T.IDUSUARIO AND A.IDROL=T.IDROL) "
                + "WHEN MATCHED THEN UPDATE SET A.ESTADO=T.ESTADO "
                + "WHEN NOT MATCHED THEN INSERT (IDUSUARIO,IDROL,ESTADO) VALUES(?,?,?);";
        try {
            pst = con.getConnection().prepareStatement(query);
            for (AsignaRol ar : listadoAR) {
                pst.setString(1, us.getCedula());
                pst.setInt(2, ar.getIdRol());
                pst.setInt(3, ar.getEstado() == true ? 1 : 0);
                pst.setString(4, us.getCedula());
                pst.setInt(5, ar.getIdRol());
                pst.setInt(6, ar.getEstado() == true ? 1 : 0);
                pst.executeUpdate();
                BitacoraDAO daoBitacora = new BitacoraDAO();
                daoBitacora.crearRegistro("usuariorol", "Asignacion roles", session);
            }
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO ASIGNAROL: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }

}
