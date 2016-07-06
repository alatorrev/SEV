/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;
import com.sev.entity.ReestablecerContra;
import com.sev.entity.Usuario;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sqlserver.Conexion;
/**
 *
 * @author usuario1
 */
public class ReestablecerDAO implements Serializable {
    
    public void editContra(ReestablecerContra r) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update usuario set clave=?"
                + " where cedula=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, r.getPassword());
            pst.setString(2, r.getCedula());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO REESTABLECER: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public List<ReestablecerContra> findAll() throws SQLException {
        Conexion con = new Conexion();
        List<ReestablecerContra> listadoUsuarios = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select u.cedula,u.nombres,u.apellidos,u.email,u.prioridad,r.IDROL,r.DESCRIPCION from USUARIO u "
                + "inner join USUARIOROL ru on u.CEDULA=ru.IDUSUARIO inner join ROL r on ru.IDROL=r.IDROL";
        pst = con.getConnection().prepareStatement(query);
        try {
            rs = pst.executeQuery();
            while (rs.next()) {
                ReestablecerContra r = new ReestablecerContra();
                r.setCedula(rs.getString(1));
                r.setNombres(rs.getString(2));
                r.setApellidos(rs.getString(3));
                r.setEmail(rs.getString(4));
                r.setPrioridad(rs.getString(5));
                r.setRol(rs.getInt(6));
                r.setDescripcionRol(rs.getString(7));
                listadoUsuarios.add(r);
            }
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return listadoUsuarios;
    }
    
}