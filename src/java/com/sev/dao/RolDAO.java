/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.entity.Rol;
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
 * @author Axel Latorre, Jorge Castañeda
 */
public class RolDAO implements Serializable {

    public List<Rol> findAll() throws SQLException {
        Conexion con = new Conexion();
        List<Rol> listadoRoles = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from rol";
        pst = con.getConnection().prepareStatement(query);
        try {
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
            con.desconectar();
        }
        return listadoRoles;
    }
    
    public void editRol(Rol rol) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update rol set descripcion=?"
                + " where idrol=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, rol.getDescripcion());
            pst.setInt(2, rol.getIdRol());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO ROL: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public void deleteRol(Rol rol) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "delete from rol where idrol=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, rol.getIdRol());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO ROL: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public void createRol(Rol rol) throws SQLException {
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into rol values(?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, rol.getDescripcion());
            pst.executeUpdate();
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO ROL: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }

}
