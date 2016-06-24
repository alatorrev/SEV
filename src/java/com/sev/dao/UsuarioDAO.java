/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

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
public class UsuarioDAO implements Serializable {

    public void createUsuario(Usuario us) throws SQLException {
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into usuario values(?,?,?,?,?,?,?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, us.getCedula());
            pst.setString(2, us.getNombres());
            pst.setString(3, us.getApellidos());
            pst.setString(4, us.getEmail());
            pst.setString(5, us.getNickname());
            pst.setString(6, us.getPassword());
            pst.setString(7, us.getPrioridad());
            pst.executeUpdate();
            
            PreparedStatement pstUsuarioRol = con.getConnection().prepareStatement(
            "insert into usuariorol values(?,?)");
            pstUsuarioRol.setString(1, us.getCedula());
            pstUsuarioRol.setInt(2, us.getRol());
            pstUsuarioRol.executeUpdate();
//            ResultSet rs = pst.getGeneratedKeys();
//
//            while (rs.next()) {
//                System.out.println("KEY GENERADA "+rs.getLong(1));
//            }

            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }

    public void editUsuario(Usuario us) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update usuario set nombres=?,apellidos=?,email=?,nickname=?,clave=?,prioridad=?"
                + " where cedula=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, us.getNombres());
            pst.setString(2, us.getApellidos());
            pst.setString(3, us.getEmail());
            pst.setString(4, us.getNickname());
            pst.setString(5, us.getPassword());
            pst.setString(6, us.getPrioridad());
            pst.setString(7, us.getCedula());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }

    public void deleteUsuario(Usuario us) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "delete from usuario where cedula=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, us.getCedula());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }

    public Usuario readUsuario(String cedula) throws SQLException {
        Conexion con = new Conexion();
        Usuario us = null;
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from usuario where cedula=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, cedula);
            rs = pst.executeQuery();
            while (rs.next()) {
                us = new Usuario();
                us.setCedula(rs.getString(1));
                us.setNombres(rs.getString(2));
                us.setApellidos(rs.getString(3));
                us.setEmail(rs.getString(4));
                us.setNickname(rs.getString(5));
                us.setPrioridad(rs.getString(7));
            }
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return us;
    }

    public List<Usuario> findAll() throws SQLException {
        Conexion con = new Conexion();
        List<Usuario> listadoUsuarios = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select u.cedula,u.nombres,u.apellidos,u.email,u.prioridad,r.IDROL,r.DESCRIPCION from USUARIO u "
                + "inner join USUARIOROL ru on u.CEDULA=ru.IDUSUARIO inner join ROL r on ru.IDROL=r.IDROL";
        pst = con.getConnection().prepareStatement(query);
        try {
            rs = pst.executeQuery();
            while (rs.next()) {
                Usuario us = new Usuario();
                us.setCedula(rs.getString(1));
                us.setNombres(rs.getString(2));
                us.setApellidos(rs.getString(3));
                us.setEmail(rs.getString(4));
                us.setPrioridad(rs.getString(5));
                us.setRol(rs.getInt(6));
                us.setDescripcionRol(rs.getString(7));
                listadoUsuarios.add(us);
            }
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return listadoUsuarios;
    }

    public Usuario loginAction(String email, String contrasena) throws SQLException {
        Conexion con = new Conexion();
        Usuario us = new Usuario();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from usuario where email=? and clave=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, email);
            pst.setString(2, contrasena);
            rs = pst.executeQuery();
            while (rs.next()) {
                us.setCedula(rs.getString(1));
                us.setNombres(rs.getString(2));
                us.setApellidos(rs.getString(3));
                us.setEmail(rs.getString(4));
                us.setNickname(rs.getString(5));
                us.setPrioridad(rs.getString(7));
                return us;
            }
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return null;
    }

}
