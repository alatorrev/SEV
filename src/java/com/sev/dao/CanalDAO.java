/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;
import com.sev.entity.CanalCaptacion;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.sev.conexion.Conexion;
import com.sev.entity.Usuario;
import com.sev.dao.BitacoraDAO;
/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
public class CanalDAO implements Serializable{
    public List<CanalCaptacion> findAll() throws SQLException {
        Conexion con = new Conexion();
        List<CanalCaptacion> listadoCanales = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from canalcaptacion where estado = 1";
        pst = con.getConnection().prepareStatement(query);
        try {
            rs = pst.executeQuery();
            while (rs.next()) {
                CanalCaptacion canal = new CanalCaptacion();
                canal.setIdCanalCaptacion(rs.getInt(1));
                canal.setDescripcion(rs.getString(2));
                listadoCanales.add(canal);
            }
        } catch (Exception e) {
            System.out.println("DAO CANALES: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return listadoCanales;
    }
    
    public void editCanal(CanalCaptacion canal, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update canalcaptacion set descripcion=?"
                + " where idcanal=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, canal.getDescripcion().toUpperCase());
            pst.setInt(2, canal.getIdCanalCaptacion());
            pst.executeUpdate();
            
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("canalcaptacion", "Edit", u);
        } catch (Exception e) {
            System.out.println("DAO CANAL: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public void deleteCanal(CanalCaptacion canal, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update canalcaptacion set estado = 0 where idcanal=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, canal.getIdCanalCaptacion());
            pst.executeUpdate();
            
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("canalcaptacion", "delete", u);
        } catch (Exception e) {
            System.out.println("DAO CANAL: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public void createCanal(CanalCaptacion canal, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into canalcaptacion values(?,1)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, canal.getDescripcion().toUpperCase());
            pst.executeUpdate();
            
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("canalcaptacion", "insert", u);
            
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO CANAL: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }
}
