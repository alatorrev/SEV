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
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into prospecto values(?,?,?,?,?,?,?,?,?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, pro.getIdcanal());
            pst.setString(2, pro.getCedula());
            pst.setString(3, pro.getNombres());
            pst.setString(4, pro.getApellidos());
            pst.setString(5, pro.getCelular());
            pst.setString(6, pro.getCasa());
            pst.setString(7, pro.getEmail());
            pst.setString(8, pro.getEstablecimientoProveniente());
            pst.setString(9, pro.getCaptador());
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
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update prospecto set idcancap=?,cedula=?,nombres=?,apellidos=?,celular=?,casa=?,correo=?, establecimiento=?, responsable=? "
                + " where idprospecto=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, p.getIdcanal());
            pst.setString(2, p.getCedula());
            pst.setString(3, p.getNombres());
            pst.setString(4, p.getApellidos());
            pst.setString(5, p.getCelular());
            pst.setString(6, p.getCasa());
            pst.setString(7, p.getEmail());
            pst.setString(8, p.getEstablecimientoProveniente());
            pst.setString(9, p.getCaptador());
            pst.setInt(10, p.getIdprospecto());
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
}
