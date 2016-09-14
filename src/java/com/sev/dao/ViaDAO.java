/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;
import com.sev.entity.ViaComunicacion;
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
public class ViaDAO implements Serializable{
   public List<ViaComunicacion> findAll() throws SQLException {
        Conexion con = new Conexion();
        List<ViaComunicacion> listadoVias = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from viacomunicacion where estado = 1";
        pst = con.getConnection().prepareStatement(query);
        try {
            rs = pst.executeQuery();
            while (rs.next()) {
                ViaComunicacion via = new ViaComunicacion();
                via.setIdViaComunicacion(rs.getInt(1));
                via.setDescripcion(rs.getString(2));
                listadoVias.add(via);
            }
        } catch (Exception e) {
            System.out.println("DAO VIAS: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return listadoVias;
    }
    
    public void editVia(ViaComunicacion via, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update viacomunicacion set descripcion=?"
                + " where idviacom=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, via.getDescripcion());
            pst.setInt(2, via.getIdViaComunicacion());
            pst.executeUpdate();
            
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("viacomunicacion", "Edit", u);
        } catch (Exception e) {
            System.out.println("DAO VIA: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public void deleteVia(ViaComunicacion via, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update viacomunicacion set estado = 0 where idviacom=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, via.getIdViaComunicacion());
            pst.executeUpdate();
            
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("viacomunicacion", "delete", u);
        } catch (Exception e) {
            System.out.println("DAO VIA: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public void createVia(ViaComunicacion via, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into viacomunicacion values(?,1)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, via.getDescripcion());
            pst.executeUpdate();
            
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("viacomunicacion", "insert", u);
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO VIA: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    } 
}
