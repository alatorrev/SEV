/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.entity.Rol;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sev.conexion.Conexion;
import com.sev.entity.Usuario;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
public class RolDAO implements Serializable {

    public List<Rol> findAll() {
        Conexion con = new Conexion();
        List<Rol> listadoRoles = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from rol where estado = 1";
        try {
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt(1));
                rol.setDescripcion(rs.getString(2));
                listadoRoles.add(rol);
            }
        } catch (Exception e) {
            System.out.println("DAO ROL: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoRoles;
    }

    public void editRol(Rol rol, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update rol set descripcion=?"
                + " where idrol=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, rol.getDescripcion().toUpperCase());
            pst.setInt(2, rol.getIdRol());
            pst.executeUpdate();
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("rol", "Edit", u);
        } catch (Exception e) {
            System.out.println("DAO ROL: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }

    public void deleteRol(Rol rol, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update rol set estado = 0 where idrol=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, rol.getIdRol());
            pst.executeUpdate();
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("rol", "delete", u);
        } catch (Exception e) {
            System.out.println("DAO ROL: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }

    public void createRol(Rol rol, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into rol values(?,1)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, rol.getDescripcion().toUpperCase());
            pst.executeUpdate();
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("rol", "insert", u);
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO ROL: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }

}
