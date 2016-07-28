/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.entity.AsignaProspecto;
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
public class AsignaProspectoDAO implements Serializable{
    public List<AsignaProspecto> findAll() throws SQLException {
        Conexion con = new Conexion();
        List<AsignaProspecto> listadoProspecto = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "  select p.IDCANCAP, p.CEDULA, p.NOMBRES, p.APELLIDOS, p.CELULAR, p.CASA, p.CORREO, p.ESTABLECIMIENTO, c.DESCRIPCION, "
                + "(select case" +
"		 when p.CEDULA = pu.IDPROSPECTO and pu.IDUSUARIO = u.CEDULA then u.NOMBRES +' '+u.APELLIDOS" +
"		 else 'Sin asignar'" +
"		 end " +
"		 from usuario as u, PROSPECTOUSUARIO as pu) as Ejecutivo, p.FECHA_CREAC" +
                       "  from PROSPECTO p, CANALCAPTACION c" +
                       "  where p.IDCANCAP = c.IDCANAL ";
        pst = con.getConnection().prepareStatement(query);
        try {
            rs = pst.executeQuery();
            while (rs.next()) {
                AsignaProspecto pro = new AsignaProspecto();
                pro.setIdcanal(rs.getInt(1));
                pro.setCedula(rs.getString(2));
                pro.setNombres(rs.getString(3));
                pro.setApellidos(rs.getString(4));
                pro.setCelular(rs.getString(5));
                pro.setCasa(rs.getString(6));
                pro.setEmail(rs.getString(7));
                pro.setEstablecimientoProveniente(rs.getString(8));
                pro.setDescripcionCanal(rs.getString(9));
                pro.setEjecutivo(rs.getString(10));
                pro.setFecha_creac(rs.getDate(11));
                listadoProspecto.add(pro);
            }
        } catch (Exception e) {
            System.out.println("DAO PROSPECTO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return listadoProspecto;
    }
    
    public List<AsignaProspecto> findAllSin() throws SQLException {
        Conexion con = new Conexion();
        List<AsignaProspecto> listadoProspectoSin = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select p.IDCANCAP, p.CEDULA, p.NOMBRES, p.APELLIDOS, p.CELULAR, p.CASA, " +
                       "p.CORREO, p.ESTABLECIMIENTO, c.DESCRIPCION, p.FECHA_CREAC " +
                       "from PROSPECTO p, CANALCAPTACION c, usuario u " +
                       "where p.IDCANCAP = c.IDCANAL " +
                       "and p.CEDULA not in (select pu.IDPROSPECTO from PROSPECTOUSUARIO pu)";
        pst = con.getConnection().prepareStatement(query);
        try {
            rs = pst.executeQuery();
            while (rs.next()) {
                AsignaProspecto pro = new AsignaProspecto();
                pro.setIdcanal(rs.getInt(1));
                pro.setCedula(rs.getString(2));
                pro.setNombres(rs.getString(3));
                pro.setApellidos(rs.getString(4));
                pro.setCelular(rs.getString(5));
                pro.setCasa(rs.getString(6));
                pro.setEmail(rs.getString(7));
                pro.setEstablecimientoProveniente(rs.getString(8));
                pro.setDescripcionCanal(rs.getString(9));
                pro.setFecha_creac(rs.getDate(10));
                listadoProspectoSin.add(pro);
            }
        } catch (Exception e) {
            System.out.println("DAO PROSPECTO Sin: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return listadoProspectoSin;
    }
    
    public void asignarProspecto(AsignaProspecto apro, Usuario usu) throws SQLException {
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into prospectousuario values(?,?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, apro.getCedula());
            pst.setString(2, usu.getCedula());
            pst.executeUpdate(); 
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO ASIGNA PROSPECTO: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }
    
    public void editAsignacion(AsignaProspecto apro, Usuario us) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update prospectousuario set idusuario=?"
                + " where idprospecto=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, us.getCedula());
            pst.setString(2, apro.getCedula());
            
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO USUARIO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
}
