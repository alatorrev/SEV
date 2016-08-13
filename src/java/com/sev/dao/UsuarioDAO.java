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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.sev.conexion.Conexion;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Axel Latorre, Jorge Castañeda
 */
public class UsuarioDAO implements Serializable {

    public void createUsuario(Usuario us) throws SQLException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into usuario values(?,?,?,?,?,?,?,?,?,?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, us.getCedula());
            pst.setString(2, us.getNombres());
            pst.setString(3, us.getApellidos());
            pst.setString(4, us.getEmail());
            pst.setString(5, us.getPassword());
            pst.setInt(6, 1); //valor que representa la clave debe ser cambiada.
            pst.setInt(7,1); //valor que representa el usuario se puedo autenticar en el sistema.
            pst.setString(8, fmt.format(new Date()));
            pst.setString(9, null/*for now lolz*/);
            pst.setString(10, us.getPrioridad());
            pst.executeUpdate();

            PreparedStatement pstUsuarioRol = con.getConnection().prepareStatement(
                    "insert into usuariorol values(?,?)");
            pstUsuarioRol.setString(1, us.getCedula());
            pstUsuarioRol.setInt(2, us.getIdRol());
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
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "update usuario set nombres=?,apellidos=?,email=?,fecha_modif=?,prioridad=?"
                + " where cedula=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, us.getNombres());
            pst.setString(2, us.getApellidos());
            pst.setString(3, us.getEmail());
            pst.setString(4, fmt.format(new Date()));
            pst.setString(5, us.getPrioridad());
            pst.setString(6, us.getCedula());

            PreparedStatement pstUsuarioRol = con.getConnection().prepareStatement(
                    "update usuariorol set idrol=? where idusuario=?");
            pstUsuarioRol.setInt(1, us.getIdRol());
            pstUsuarioRol.setString(2, us.getCedula());
            pstUsuarioRol.executeUpdate();

            pst.executeUpdate();
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
            con.getConnection().rollback();
            con.desconectar();
        }
    }

    public void deleteUsuario(Usuario us) throws SQLException {
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        //String query = "delete from usuario where cedula=?";
        String query="update usuario set activo=? where cedula=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1,0);
            pst.setString(2, us.getCedula());
            pst.executeUpdate();
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
            con.getConnection().rollback();
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
                us.setPrioridad(rs.getString(6));
            }
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return us;
    }

    public List<Usuario> findAll() {
        Conexion con = new Conexion();
        List<Usuario> listadoUsuarios = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select u.cedula,u.nombres,u.apellidos,u.email,u.prioridad,r.IDROL,r.DESCRIPCION from USUARIO u "
                + "inner join USUARIOROL ru on u.CEDULA=ru.IDUSUARIO inner join ROL r on ru.IDROL=r.IDROL where u.ACTIVO=?";

        try {
            pst = con.getConnection().prepareStatement(query);
            pst.setInt(1, 1); //todos los usuario que pueden autenticarse en el sistema.
            rs = pst.executeQuery();
            while (rs.next()) {
                Usuario us = new Usuario();
                us.setCedula(rs.getString(1));
                us.setNombres(rs.getString(2));
                us.setApellidos(rs.getString(3));
                us.setEmail(rs.getString(4));
                us.setPrioridad(rs.getString(5));
                us.setIdRol(rs.getInt(6));
                us.setDescripcionRol(rs.getString(7));
                listadoUsuarios.add(us);
            }
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listadoUsuarios;
    }

    public Usuario loginAction(String email, String contrasena) throws SQLException {
        Conexion con = new Conexion();
        Usuario us = new Usuario();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select u.CEDULA,u.NOMBRES,u.APELLIDOS,u.EMAIL,u.PRIORIDAD,r.IDROL,r.DESCRIPCION,u.ESTADOCLAVE,"
                + "u.ACTIVO from usuario u "
                + "inner join USUARIOROL ur on ur.IDUSUARIO=u.CEDULA "
                + "inner join ROL r on r.IDROL=ur.IDROL where u.EMAIL=? and u.CLAVE=? and u.ACTIVO=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, email);
            pst.setString(2, contrasena);
            pst.setInt(3, 1);
            rs = pst.executeQuery();
            while (rs.next()) {
                us.setCedula(rs.getString(1));
                us.setNombres(rs.getString(2));
                us.setApellidos(rs.getString(3));
                us.setEmail(rs.getString(4));
                us.setPrioridad(rs.getString(5));
                us.setIdRol(rs.getInt(6));
                us.setDescripcionRol(rs.getString(7));
                us.setEstadoClave(rs.getInt(8));
                us.setActivo(rs.getInt(9));
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
