/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.entity.ReestablecerContra;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.sev.conexion.Conexion;
import com.sev.entity.Usuario;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
public class ReestablecerDAO implements Serializable {

    public boolean editContra(ReestablecerContra r, Usuario u) throws SQLException {
        boolean done=false;
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update usuario set clave=?,estadoclave=?"
                + " where cedula=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, r.getPassword());
            pst.setInt(2, 1);
            pst.setString(3, r.getCedula());
            pst.executeUpdate();
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("usuario", "Reestablece clave", u);
            done=true;
        } catch (Exception e) {
            System.out.println("DAO REESTABLECER: " + e.getMessage());
            done=false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public List<ReestablecerContra> findAll() throws SQLException {
        Conexion con = new Conexion();
        List<ReestablecerContra> listadoUsuarios = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select u.cedula,u.nombres,u.apellidos,u.email,u.prioridad,r.IDROL,r.DESCRIPCION,"
                + "u.estadoclave,u.activo from USUARIO u "
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
