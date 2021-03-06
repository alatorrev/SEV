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
 * Universidad Politécnica Salesiana
 *
 * @author Axel Latorre, Jorge Castañeda Tutor: Ing. Vanessa Jurado
 *
 */
public class UsuarioDAO implements Serializable {

    public boolean createUsuario(Usuario us,Usuario session) throws SQLException {
        boolean done = false;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String sql = "insert into usuario values(?,?,?,?,?,?,?,?,?,1)";
        pst = con.getConnection().prepareStatement(sql);
        try {
            pst.setString(1, us.getCedula());
            pst.setString(2, us.getNombres().toUpperCase());
            pst.setString(3, us.getApellidos().toUpperCase());
            pst.setString(4, us.getEmail().toLowerCase());
            pst.setString(5, us.getPassword());
            pst.setInt(6, 1); //valor que representa la clave debe ser cambiada.
            pst.setInt(7, 1); //valor que representa el usuario se puedo autenticar en el sistema.
            pst.setString(8, fmt.format(new Date()));
            pst.setString(9, null/*for now lolz*/);
            pst.executeUpdate();

            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("usuario", "Insert", session);
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public boolean editUsuario(Usuario us,Usuario session) throws SQLException {
        boolean done = false;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "update usuario set nombres=?,apellidos=?,email=?,fecha_modif=?,prioridad=?"
                + " where cedula=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, us.getNombres().toUpperCase());
            pst.setString(2, us.getApellidos().toUpperCase());
            pst.setString(3, us.getEmail().toLowerCase());
            pst.setString(4, fmt.format(new Date()));
            pst.setString(5, us.getPrioridad());
            pst.setString(6, us.getCedula());

            pst.executeUpdate();

            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("usuario", "Edit", session);
            con.getConnection().commit();
            done = true;
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
            con.getConnection().rollback();
            done = false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public boolean deleteUsuario(Usuario us,Usuario session) throws SQLException {
        boolean done=false;
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        //String query = "delete from usuario where cedula=?";
        String query = "update usuario set activo=? where cedula=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, 0);
            pst.setString(2, us.getCedula());
            pst.executeUpdate();

            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("usuario", "delete", session);

            con.getConnection().commit();
            done=true;
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
            con.getConnection().rollback();
            done=false;
        } finally {
            con.desconectar();
        }
        return done;
    }

    public Usuario readUsuario(String cedula) throws SQLException {
        Conexion con = new Conexion();
        Usuario us = null;
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from usuario where cedula=? and activo=1";
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
        String query = "select u.cedula,u.nombres,u.apellidos,u.email,u.prioridad,r.IDROL,r.DESCRIPCION "
                + "from USUARIO U left join USUARIOROL RU on U.CEDULA=RU.IDUSUARIO "
                + "LEFT JOIN ROL R ON R.IDROL=RU.IDROL "
                + "WHERE U.ACTIVO=? AND RU.ESTADO=1 OR RU.ESTADO IS NULL";
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

    public List<Usuario> findAllAsigna() {
        Conexion con = new Conexion();
        List<Usuario> listadoUsuarios = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select u.cedula,u.nombres,u.apellidos,u.email,u.prioridad,r.IDROL,r.DESCRIPCION from USUARIO u "
                + "inner join USUARIOROL ru on u.CEDULA=ru.IDUSUARIO inner join ROL r on ru.IDROL=r.IDROL "
                + "where u.ACTIVO=? and r.IDROL = 3 and r.estado=1";

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

    public Usuario loginAction(String email, String contrasena, Usuario u, int idrol) throws SQLException {
        Conexion con = new Conexion();
        Usuario us = new Usuario();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select u.CEDULA,u.NOMBRES,u.APELLIDOS,u.EMAIL,u.PRIORIDAD,r.IDROL,r.DESCRIPCION,u.ESTADOCLAVE,"
                + "u.ACTIVO from usuario u "
                + "inner join USUARIOROL ur on ur.IDUSUARIO=u.CEDULA "
                + "inner join ROL r on r.IDROL=ur.IDROL where u.EMAIL=? and u.CLAVE=? and r.idrol=? and u.ACTIVO=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, email);
            pst.setString(2, contrasena);
            pst.setInt(3, idrol);
            pst.setInt(4, 1);
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
                BitacoraDAO daoBitacora = new BitacoraDAO();
                daoBitacora.crearRegistro("usuario", "Login", us);
                return us;
            }
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return null;
    }

    public List<Usuario> completeUsuarioMethod(String pattern) throws SQLException {
        System.out.println(pattern);
        List<Usuario> lista = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from usuario where upper(APELLIDOS+' '+NOMBRES) like upper(?) and activo!=0";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, pattern.trim().concat("%"));
            rs = pst.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setCedula(rs.getString(1));
                u.setNombres(rs.getString(2));
                u.setApellidos(rs.getString(3));
                u.setEmail(rs.getString(4));
                u.setEstadoClave(rs.getInt(6));
                u.setActivo(rs.getInt(7));
                u.setFecha_crea(rs.getDate(8));
                u.setFecha_modif(rs.getDate(9));
                lista.add(u);
            }
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            con.desconectar();
            System.out.println(lista.size());
        }
        return lista;
    }
}
