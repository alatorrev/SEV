/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.entity.Prospecto;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sqlserver.Conexion;

/**
 *
 * @author usuario1
 */
public class ProspectoDAO implements Serializable{
    public List<Prospecto> findAll() throws SQLException {
        Conexion con = new Conexion();
        List<Prospecto> listadoProspecto = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "  select p.IDCANCAP, p.CEDULA, p.NOMBRES, p.APELLIDOS, p.CELULAR, p.CASA, p.CORREO, p.ESTABLECIMIENTO, p.RESPONSABLE, c.DESCRIPCION" +
                       "  from PROSPECTO p" +
                       "  inner join CANALCAPTACION c on p.IDCANCAP = c.IDCANAL";
        pst = con.getConnection().prepareStatement(query);
        try {
            rs = pst.executeQuery();
            while (rs.next()) {
                Prospecto pro = new Prospecto();
                pro.setIdcanal(rs.getInt(1));
                pro.setCedula(rs.getString(2));
                pro.setNombres(rs.getString(3));
                pro.setApellidos(rs.getString(4));
                pro.setCelular(rs.getString(5));
                pro.setCasa(rs.getString(6));
                pro.setEmail(rs.getString(7));
                pro.setEstablecimientoProveniente(rs.getString(8));
                pro.setCaptador(rs.getString(9));
                pro.setDescripcionCanal(rs.getString(10));
                listadoProspecto.add(pro);
            }
        } catch (Exception e) {
            System.out.println("DAO PROSPECTO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return listadoProspecto;
    }
    
    public void createProspecto(Prospecto pro) throws SQLException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into prospecto values(?,?,?,?,?,?,?,?,?,?,?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, pro.getCedula());
            pst.setInt(2, pro.getIdcanal());
            pst.setString(3, pro.getNombres());
            pst.setString(4, pro.getApellidos());
            pst.setString(5, pro.getCelular());
            pst.setString(6, pro.getCasa());
            pst.setString(7, pro.getEmail());
            pst.setString(8, pro.getEstablecimientoProveniente());
            pst.setString(9, pro.getCaptador());
            pst.setString(10, fmt.format(new Date()));
            pst.setString(11, null/*for now lolz*/);
            
            pst.executeUpdate(); 
//            ResultSet rs = pst.getGeneratedKeys();
//
//            while (rs.next()) {
//                System.out.println("KEY GENERADA "+rs.getLong(1));
//            }
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO PROSPECTO: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }
    
    public void editProspecto(Prospecto p) throws SQLException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update prospecto set idcancap=?,nombres=?,apellidos=?,celular=?,casa=?,correo=?, establecimiento=?, responsable=?, fecha_modif=?"
                + " where cedula=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, p.getIdcanal());
            pst.setString(2, p.getNombres());
            pst.setString(3, p.getApellidos());
            pst.setString(4, p.getCelular());
            pst.setString(5, p.getCasa());
            pst.setString(6, p.getEmail());
            pst.setString(7, p.getEstablecimientoProveniente());
            pst.setString(8, p.getCaptador());
            pst.setString(9, fmt.format(new Date()));
            pst.setString(10, p.getCedula());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO PROSPECTO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public void deleteProspecto(Prospecto p) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "delete from prospecto where cedula=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, p.getCedula());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO PROSPECTO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
     public void guardarProspecto(Prospecto pros) throws SQLException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into PROSPECTO" +
                       "(cedula,idcancap,nombres,apellidos,celular,casa,correo,establecimiento,responsable, fecha_creac, fecha_modif)" +
                       "values (?,(select IDCANAL from CANALCAPTACION where DESCRIPCION =?), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        //String query = "insert into prospecto values(?,?,?,?,?,?,?,?,?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, pros.getCedula());
            pst.setString(2, pros.getDescripcionCanal());
            pst.setString(3, pros.getNombres());
            pst.setString(4, pros.getApellidos());
            pst.setString(5, pros.getCelular());
            pst.setString(6, pros.getCasa());
            pst.setString(7, pros.getEmail());
            pst.setString(8, pros.getEstablecimientoProveniente());
            pst.setString(9, pros.getCaptador());
            pst.setString(10, fmt.format(new Date()));
            pst.setString(11, null/*for now lolz*/);
            pst.executeUpdate(); 
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO PROSPECTOCARGA: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }
}
